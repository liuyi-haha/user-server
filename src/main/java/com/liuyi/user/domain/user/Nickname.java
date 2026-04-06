package com.liuyi.user.domain.user;

import com.liuyi.user.domain.exception.NicknameInvalidException;
import lombok.Getter;

import java.util.regex.Pattern;
@Getter
public final class Nickname {
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[\\p{IsHan}A-Za-z0-9_]{1,10}$");

    private final String nickname;

    public Nickname(String nickname) {
        validate(nickname);
        this.nickname = nickname;
    }

    private void validate(String nickname) {
        if (nickname == null || !NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new NicknameInvalidException();
        }
    }
}
