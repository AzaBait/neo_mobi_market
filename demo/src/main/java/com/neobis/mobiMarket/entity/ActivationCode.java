package com.neobis.mobiMarket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activationCodes")
public class ActivationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String code;
    private String email;

    @Override
    public String toString() {
        return "SmsCode{" +
                "id=" + id +
                ", user=" + user +
                ", code='" + code + '\'' +
                ", phone='" + email + '\'' +
                '}';
    }
}
