package com.liuyi.user.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException() {
        super("用户不存在");
    }
}
