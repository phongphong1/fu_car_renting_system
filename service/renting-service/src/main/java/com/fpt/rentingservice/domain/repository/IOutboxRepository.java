package com.fpt.rentingservice.domain.repository;

import com.fpt.rentingservice.domain.model.OutboxEvent;

public interface IOutboxRepository {

    OutboxEvent save(OutboxEvent event);
    
}