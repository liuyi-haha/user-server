package com.liuyi.user.application.event;

import org.liuyi.common.application.ApplicationEvent;

public class UserRegisteredEvent implements ApplicationEvent {
    private final String userId;
    private final String phone;
    private final String hashedPassword;

    public UserRegisteredEvent(String userId, String phone, String hashedPassword) {
        this.userId = userId;
        this.phone = phone;
        this.hashedPassword = hashedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    @Override
    public String eventId() {
        return "";
    }
}
