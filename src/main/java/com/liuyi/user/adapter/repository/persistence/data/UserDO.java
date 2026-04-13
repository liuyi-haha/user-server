package com.liuyi.user.adapter.repository.persistence.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 9, nullable = false)
    private String userId;
    @Column(length = 10, nullable = false)
    private String nickname;
    @Column(length = 11, unique = true, nullable = false)
    private String phone;
    @Column(length = 20, nullable = false)
    private String description;
    @Column(length = 20, nullable = false)
    private String avatarId;
}
