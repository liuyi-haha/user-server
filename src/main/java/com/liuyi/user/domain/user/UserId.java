package com.liuyi.user.domain.user;

import org.liuyi.common.domain.exception.DomainException;
import org.liuyi.common.domain.object.Identity;

import java.util.UUID;
import java.util.regex.Pattern;

public final class UserId implements Identity<String> {
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[1-9][0-9]{8}$");

    private final String id;

    public UserId() {
        this(UserIdGenerator.nextUserId());
    }

    public UserId(String id) {
        validate(id);
        this.id = id;
    }

    @Override
    public String value() {
        return id;
    }

    private void validate(String id) {
        if (id == null || !USER_ID_PATTERN.matcher(id).matches()) {
            throw new DomainException("用户ID必须是9位数字字符串，且首位不能为0");
        }
    }
}

final class UserIdGenerator {
    private static final long BASE = 100_000_000L;
    private static final long RANGE = 900_000_000L;

    private UserIdGenerator() {
    }

    public static String nextUserId() {
        String hex = UUID.randomUUID().toString().replace("-", "");
        long raw = Long.parseUnsignedLong(hex.substring(0, 15), 16);
        long id = BASE + Math.floorMod(raw, RANGE);
        return Long.toString(id);
    }
}
