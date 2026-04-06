package com.liuyi.user.adapter.repository.persistence.mapper;

import com.liuyi.user.adapter.repository.persistence.data.UserDO;
import com.liuyi.user.domain.user.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDO toData(User user)
    {
        return UserDO.builder()
                .userId(user.getUserId().value())
                .phone(user.getPhone().getPhone())
                .nickname(user.getNickname().getNickname())
                .description(user.getDescription().getDescription())
                .avatarId(user.getAvatar().getFileId())
                .build();
    }

    // 数据模型 -> 领域模型
    public User toDomain(UserDO userDO)
    {
        return User.of(userDO.getUserId(), userDO.getNickname(), userDO.getPhone(), userDO.getDescription(), userDO.getAvatarId());
    }
}
