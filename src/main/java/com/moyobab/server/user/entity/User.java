package com.moyobab.server.user.entity;

import com.moyobab.server.favorite.entity.Favorite;
import com.moyobab.server.global.entity.BaseEntity;
import com.moyobab.server.grouporder.entity.GroupOrder;
import com.moyobab.server.participant.entity.Participant;
import com.moyobab.server.payment.entity.Payment;
import com.moyobab.server.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 320)
    private String email;

    private String password;

    @Column(name = "nickname", nullable = false, unique = true, length = 30)
    private String nickname;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @OneToMany(mappedBy = "creator")
    @Builder.Default
    private List<GroupOrder> groupOrders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Favorite> favorites = new ArrayList<>();

    public static User createUser(String email, String password, String username, String nickname, LocalDate birthDate, String phone, LoginType loginType) {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .birthDate(birthDate)
                .phoneNumber(phone)
                .loginType(loginType)
                .build();
    }
}
