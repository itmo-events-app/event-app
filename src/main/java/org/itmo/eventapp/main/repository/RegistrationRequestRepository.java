package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Integer> {

    Optional<RegistrationRequest> findRegistrationRequestById(Integer id);

    boolean existsByEmail(String login);
}
