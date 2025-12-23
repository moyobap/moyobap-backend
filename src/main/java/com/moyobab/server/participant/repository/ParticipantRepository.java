package com.moyobab.server.participant.repository;

import com.moyobab.server.participant.entity.Participant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @EntityGraph(attributePaths = {"groupOrder"})
    List<Participant> findByGroupOrderId(Long groupOrderId);

    boolean existsByGroupOrderIdAndUserId(Long groupOrderId, Long userId);
    Optional<Participant> findByGroupOrderIdAndUserId(Long groupOrderId, Long userId);
}
