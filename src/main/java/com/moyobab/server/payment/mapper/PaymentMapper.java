package com.moyobab.server.payment.mapper;

import com.moyobab.server.payment.dto.PaymentResponseDto;
import com.moyobab.server.payment.entity.Payment;

public class PaymentMapper {

    public static PaymentResponseDto toPaymentResponse(Payment payment) {
        return PaymentResponseDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .paymentKey(payment.getPaymentKey())
                .participantId(payment.getParticipantId())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .approvedAt(payment.getApprovedAt())
                .build();
    }
}
