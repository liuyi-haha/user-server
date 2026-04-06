package com.liuyi.user.adapter.repository.persistence.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor  // 添加这个
@Table(name = "user_info")
public class UserDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 9, nullable = false)
    private String userId;
    @Column(length = 10, nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String phone;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String avatarId;

    public UserDO() {
    }
}
