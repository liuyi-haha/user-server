package com.liuyi.user.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class InvalidAccountException extends DomainException {
    public InvalidAccountException() {
        super("账号格式不正确");
    }
}
