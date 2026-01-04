package com.example.ordermanagement.common.repository;

import com.example.ordermanagement.common.domain.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord, String> {
}