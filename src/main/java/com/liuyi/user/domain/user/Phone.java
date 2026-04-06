package com.liuyi.user.domain.user;

import lombok.Getter;
import org.liuyi.common.domain.exception.DomainException;

import java.util.regex.Pattern;

@Getter
public final class Phone {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");

    private final String phone;

    public Phone(String phone) {
        validate(phone);
        this.phone = phone;
    }

    private void validate(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new DomainException("手机号必须是11位数字");
        }
    }
}
