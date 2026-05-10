package com.liuyi.user.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class PhoneRegisteredException extends DomainException {
    public PhoneRegisteredException() {
        super("手机号已被注册");
    }
}

