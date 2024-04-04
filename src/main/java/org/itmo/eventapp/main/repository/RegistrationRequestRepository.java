package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.RegistrationRequest;
import org.itmo.eventapp.main.model.entity.enums.RegistrationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Integer> {

    Optional<RegistrationRequest> getRegistrationRequestByEmail(String email);
    List<RegistrationRequest> getRegistrationRequestsByStatus(RegistrationRequestStatus status);
}
