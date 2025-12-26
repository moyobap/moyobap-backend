package com.moyobab.server.participant.service;

import com.moyobab.server.event.dto.GroupOrderEventType;
import com.moyobab.server.event.dto.GroupOrderSummaryDto;
import com.moyobab.server.event.dto.ParticipantEventDto;
import com.moyobab.server.event.service.GroupOrderEventPublisher;
import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.grouporder.entity.GroupOrder;
import com.moyobab.server.grouporder.mapper.GroupOrderSummaryMapper;
import com.moyobab.server.grouporder.repository.GroupOrderRepository;
import com.moyobab.server.participant.dto.ParticipantJoinRequestDto;
import com.moyobab.server.participant.dto.ParticipantUpdateAmountRequestDto;
import com.moyobab.server.participant.entity.Participant;
import com.moyobab.server.participant.exception.ParticipantErrorCase;
import com.moyobab.server.participant.repository.ParticipantRepository;
import com.moyobab.server.user.entity.User;
import com.moyobab.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final GroupOrderRepository groupOrderRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    private final GroupOrderEventPublisher eventPublisher;
    private final GroupOrderSummaryMapper summaryMapper;

    @Transactional
    public void joinGroup(Long groupOrderId, Long userId, ParticipantJoinRequestDto request) {

        if (userId == null) {
            throw new ApplicationException(ParticipantErrorCase.LOGIN_REQUIRED);
        }

        if (request.getOrderAmount() <= 0) {
            throw new ApplicationException(ParticipantErrorCase.INVALID_ORDER_AMOUNT);
        }

        GroupOrder groupOrder = groupOrderRepository.findById(groupOrderId)
                .orElseThrow(() -> new ApplicationException(ParticipantErrorCase.GROUP_ORDER_NOT_FOUND));

        if (groupOrder.isClosed()) {
            throw new ApplicationException(ParticipantErrorCase.GROUP_ORDER_CLOSED);
        }

        if (participantRepository.existsByGroupOrderIdAndUserId(groupOrderId, userId)) {
            throw new ApplicationException(ParticipantErrorCase.ALREADY_JOINED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ParticipantErrorCase.USER_NOT_FOUND));

        Participant participant = Participant.builder()
                .groupOrder(groupOrder)
                .user(user)
                .orderAmount(request.getOrderAmount())
                .paid(false)
                .build();

        participantRepository.save(participant);

        // 그룹 상세에 참여자 추가 이벤트 발행
        ParticipantEventDto participantEvent = ParticipantEventDto.builder()
                .participantId(participant.getId())
                .userId(user.getId())
                .nickname(user.getNickname())
                .orderAmount(participant.getOrderAmount())
                .paid(participant.isPaid())
                .build();

        eventPublisher.publishToGroupDetail(groupOrderId, GroupOrderEventType.PARTICIPANT_JOINED, participantEvent);

        // 메인 리스트에 요약 갱신 이벤트 발행 (인원,총액 변경)
        List<Participant> participants = participantRepository.findByGroupOrderId(groupOrderId);
        long totalAmount = participants.stream().mapToLong(Participant::getOrderAmount).sum();

        GroupOrderSummaryDto summary = summaryMapper.toSummary(groupOrder, participants.size(), totalAmount);

        eventPublisher.publishToGroupList(GroupOrderEventType.GROUP_UPDATED, summary);

        log.info("[WS] PARTICIPANT_JOINED + GROUP_UPDATED published. groupOrderId={}, participantId={}",
                groupOrderId, participant.getId());
    }

    @Transactional
    public void updateMyOrderAmount(Long groupOrderId, Long userId, ParticipantUpdateAmountRequestDto request) {

        if (userId == null) {
            throw new ApplicationException(ParticipantErrorCase.LOGIN_REQUIRED);
        }

        if (request.getOrderAmount() <= 0) {
            throw new ApplicationException(ParticipantErrorCase.INVALID_ORDER_AMOUNT);
        }

        GroupOrder groupOrder = groupOrderRepository.findById(groupOrderId)
                .orElseThrow(() -> new ApplicationException(ParticipantErrorCase.GROUP_ORDER_NOT_FOUND));

        if (groupOrder.isClosed()) {
            throw new ApplicationException(ParticipantErrorCase.GROUP_ORDER_CLOSED);
        }

        Participant participant = participantRepository.findByGroupOrderIdAndUserId(groupOrderId, userId)
                .orElseThrow(() -> new ApplicationException(ParticipantErrorCase.PARTICIPATION_NOT_FOUND));

        // 금액 업데이트
        participant.updateOrderAmount(request.getOrderAmount());

        // 금액 변경
        ParticipantEventDto payload = ParticipantEventDto.builder()
                .participantId(participant.getId())
                .userId(participant.getUser().getId())
                .nickname(participant.getUser().getNickname())
                .orderAmount(participant.getOrderAmount())
                .paid(participant.isPaid())
                .build();

        eventPublisher.publishToGroupDetail(groupOrderId, GroupOrderEventType.PARTICIPANT_AMOUNT_UPDATED, payload);

        // 그룹 요약 갱신
        List<Participant> participants = participantRepository.findByGroupOrderId(groupOrderId);
        long totalAmount = participants.stream().mapToLong(Participant::getOrderAmount).sum();

        GroupOrderSummaryDto summary = summaryMapper.toSummary(groupOrder, participants.size(), totalAmount);
        eventPublisher.publishToGroupList(GroupOrderEventType.GROUP_UPDATED, summary);

        log.info("[WS] PARTICIPANT_AMOUNT_UPDATED + GROUP_UPDATED published. groupOrderId={}, participantId={}",
                groupOrderId, participant.getId());
    }
}
