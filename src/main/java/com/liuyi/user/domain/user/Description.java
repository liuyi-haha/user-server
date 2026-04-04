package com.liuyi.user.domain.user;

import lombok.Getter;
import org.liuyi.common.domain.exception.DomainException;
@Getter
public final class Description {
    private static final int MAX_LENGTH = 20;

    private final String description;

    public Description() {
        this("");
    }

    public Description(String desc) {
        validate(desc);
        this.description = desc;
    }

    private void validate(String desc) {
        if (desc == null) {
            throw new DomainException("个性签名不能为空");
        }
        if (desc.length() > MAX_LENGTH) {
            throw new DomainException("个性签名不能超过20个字符");
        }
    }
}

