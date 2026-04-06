package com.liuyi.user.application.fixture;

import com.liuyi.user.domain.user.User;
import lombok.Builder;
import lombok.Data;

public class UserTextFixture {
    private static final String normalUserId = "123456789";
    private static final String normalAvatar = "avatar";
    private static final String normalFileId = "fagagagagaga";
    private static final String normalNickname = "张三";
    private static final String normalDescription = "";
    private static final String normalPhone = "15011111111";


    @Builder
    @Data
    public static class UserDTO
    {
        private String userId;
        private String avatar;
        private String avatarFileId;
        private String nickname;
        private String description;
        private String phone;
        private String hashedPassword;

        public User toDomain()
        {
            return User.of(userId, nickname, phone, description, avatarFileId);
        }
    }

    static public UserDTO.UserDTOBuilder buildNormalUser()
    {
        return UserDTO.builder()
                .userId(normalUserId)
                .avatar(normalAvatar)
                .avatarFileId(normalFileId)
                .nickname(normalNickname)
                .description(normalDescription)
                .phone(normalPhone)
                .hashedPassword("hashedPassword");
    }

}
