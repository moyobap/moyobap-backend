package com.moyobab.server.participant.repository;

import com.moyobab.server.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByGroupOrderId(Long groupOrderId);
}
