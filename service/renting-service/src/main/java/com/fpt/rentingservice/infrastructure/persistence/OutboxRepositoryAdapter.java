package com.fpt.rentingservice.infrastructure.persistence;

import com.fpt.rentingservice.domain.model.OutboxEvent;
import com.fpt.rentingservice.domain.repository.IOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxRepositoryAdapter implements IOutboxRepository {

    private final OutboxEventRepository jpaRepository;

    @Override
    public OutboxEvent save(OutboxEvent event) {
        return jpaRepository.save(event);
    }
}