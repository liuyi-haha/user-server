package com.liuyi.user.domain.user;

import java.util.regex.Pattern;

import lombok.Getter;
import org.liuyi.common.domain.exception.DomainException;
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
            throw new DomainException("昵称必须是1到10位，且只能包含中文、英文、数字和下划线");
        }
    }
}

