package com.example.ordermanagement.common.web;

import com.example.ordermanagement.common.domain.entity.IdempotencyRecord;
import com.example.ordermanagement.common.repository.IdempotencyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private final IdempotencyRepository idempotencyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String idempotencyKey = request.getHeader("Idempotency-Key");

        if (idempotencyKey == null || isSafeMethod(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Processing idempotency key: {}", idempotencyKey);

        Optional<IdempotencyRecord> existingRecordOpt = idempotencyRepository.findById(idempotencyKey);

        if (existingRecordOpt.isPresent()) {
            IdempotencyRecord existingRecord = existingRecordOpt.get();
            if (existingRecord.getResponseStatus() == null) {
                // Processing in progress
                response.sendError(HttpServletResponse.SC_CONFLICT, "Request with this Idempotency-Key is currently being processed");
                return;
            } else {
                // Return cached response
                log.info("Returning cached response for key: {}", idempotencyKey);
                response.setStatus(existingRecord.getResponseStatus());
                response.setContentType("application/json"); // Assuming JSON for now
                if (existingRecord.getResponseBody() != null) {
                    response.getWriter().write(existingRecord.getResponseBody());
                }
                return;
            }
        }

        // Try to lock the key
        try {
            IdempotencyRecord newRecord = IdempotencyRecord.builder()
                    .key(idempotencyKey)
                    .build(); // status null means processing
            idempotencyRepository.saveAndFlush(newRecord);
        } catch (DataIntegrityViolationException e) {
            // Race condition: another thread inserted it just now
            response.sendError(HttpServletResponse.SC_CONFLICT, "Request with this Idempotency-Key is currently being processed");
            return;
        }

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(request, responseWrapper);
        } finally {
            // Update the record with response
            int status = responseWrapper.getStatus();
            String body = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

            // Only cache successful or specific error codes? 
            // Requirement says "Return cached response on duplicate requests".
            // Usually we cache 2xx, 4xx (client errors). 5xx might be retried.
            // For now, cache everything except maybe 500? Let's cache everything to be safe on side effects.
            
            try {
                IdempotencyRecord record = idempotencyRepository.findById(idempotencyKey).orElseThrow();
                record.setResponseStatus(status);
                record.setResponseBody(body);
                idempotencyRepository.save(record);
            } catch (Exception e) {
                log.error("Failed to save idempotency response for key: {}", idempotencyKey, e);
            }

            responseWrapper.copyBodyToResponse();
        }
    }

    private boolean isSafeMethod(String method) {
        return HttpMethod.GET.matches(method) || HttpMethod.HEAD.matches(method) || HttpMethod.OPTIONS.matches(method);
    }
}
