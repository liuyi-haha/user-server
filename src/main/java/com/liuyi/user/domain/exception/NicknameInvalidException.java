package com.liuyi.user.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class NicknameInvalidException extends DomainException {
    public NicknameInvalidException() {
        super("昵称必须是1到10位，且只能包含中文、英文、数字和下划线");
    }
}

