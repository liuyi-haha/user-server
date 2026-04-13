package com.liuyi.user.domain.user;

import org.liuyi.common.domain.exception.DomainException;

import java.util.regex.Pattern;

public record Phone(String phone) {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");

    public Phone(String phone) {
        this.phone = phone;
        validate(phone);
    }

    private void validate(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new DomainException("手机号必须是11位数字");
        }
    }
}
