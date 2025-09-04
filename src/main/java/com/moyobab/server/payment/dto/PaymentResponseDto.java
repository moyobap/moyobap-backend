package com.moyobab.server.payment.dto;

import com.moyobab.server.payment.entity.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private Long id;
    private String orderId;
    private String paymentKey;
    private Long participantId;
    private PaymentStatus status;
    private Long amount;
    private String paymentMethod;
    private LocalDateTime approvedAt;
}
