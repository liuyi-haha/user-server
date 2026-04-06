package com.liuyi.user.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class AvatarTooLargeException extends DomainException {
    public AvatarTooLargeException() {
        super("头像体积不能超过5MB");
    }
}
