package com.liuyi.user.domain.user;

import lombok.Getter;
import org.liuyi.common.domain.object.Avatar;

@Getter
public final class User {
    private final UserId userId;
    private final Nickname nickname;
    private final Phone phone;
    private final Description description;
    private final Avatar avatar;

    private User(UserId userId, Nickname nickname, Phone phone, Description description, Avatar avatar) {
        this.userId = userId;
        this.nickname = nickname;
        this.phone = phone;
        this.description = description;
        this.avatar = avatar;
    }

    public static User createUser(String nickname, String phone, String avatarFileId, byte[] avatar) {
        return new User(
                new UserId(),
                new Nickname(nickname),
                new Phone(phone),
                new Description(),
                new Avatar(avatarFileId, avatar)
        );
    }

    public static User of(String userId, String nickname, String phone, String desc, String avatarFileId) {
        return new User(
                new UserId(userId),
                new Nickname(nickname),
                new Phone(phone),
                new Description(desc),
                new Avatar(avatarFileId)
        );
    }
}
