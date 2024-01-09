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
@Table(name = "SmsCodes")
public class SmsCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String code;
    private String phone;

    @Override
    public String toString() {
        return "SmsCode{" +
                "id=" + id +
                ", user=" + user +
                ", code='" + code + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
