package com.example.ordermanagement.modules.ordering.infrastructure.outbox.repository;

import com.example.ordermanagement.modules.ordering.infrastructure.outbox.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    
    // Fetch pending events ordered by creation time to ensure strict ordering
    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxEvent.OutboxStatus status);
}
