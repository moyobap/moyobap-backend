package com.moyobab.server.payment.entity;

public enum PaymentStatus {
    PENDING,    // 결제 대기 중
    SUCCESS,    // 결제 완료
    FAILED,     // 결제 실패
    CANCELLED   // 결제 취소
}
