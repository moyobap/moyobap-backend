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

    private String email;

    private String password;

    private String nickname;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<GroupOrder> groupOrders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Participant> participants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Favorite> favorites;
}
