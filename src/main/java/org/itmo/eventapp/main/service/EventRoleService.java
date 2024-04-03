package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.repository.EventRoleRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventRoleService {
    private final EventRoleRepository eventRoleRepository;

    EventRole save(EventRole eventRole) {
        return eventRoleRepository.save(eventRole);
    }
}
