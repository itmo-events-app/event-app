package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.RegistrationRequest;
import org.itmo.eventapp.main.model.entity.enums.RegistrationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Integer> {

    Optional<RegistrationRequest> getRegistrationRequestByEmail(String email);

    List<RegistrationRequest> getRegistrationRequestsByStatus(RegistrationRequestStatus status);

    List<RegistrationRequest> findAll();

    Optional<RegistrationRequest> findRegistrationRequestById(Integer id);

    boolean existsByEmail(String login);
}
