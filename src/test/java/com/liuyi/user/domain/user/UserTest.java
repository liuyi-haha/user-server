package com.liuyi.user.domain.user;

import com.liuyi.user.domain.exception.NicknameInvalidException;
import org.junit.jupiter.api.Test;
import org.liuyi.common.domain.exception.DomainException;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    void 昵称格式正确时_createUser_成功() {
        var validNicknames = java.util.List.of("ab", "abcd_efghi");

        for (var nickname : validNicknames) {
            assertThatCode(() -> User.createUser(nickname, "13800138000", "avatar-id", new byte[0]))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    void 昵称格式不正确时_createUser_抛异常() {
        var invalidNicknames = java.util.List.of("", "abcdefghijk", "nick-name", "name 1", "@昵称");

        for (var nickname : invalidNicknames) {
            assertThatThrownBy(() -> User.createUser(nickname, "13800138000", "avatar-id", new byte[0]))
                    .isInstanceOf(NicknameInvalidException.class)
                    .hasMessage("昵称必须是1到10位，且只能包含中文、英文、数字和下划线");
        }
    }

    @Test
    void 昵称格式不正确时_of_抛异常() {
        assertThatThrownBy(() -> User.of("123456789", "bad name", "13800138000", "desc", "avatar-id"))
                .isInstanceOf(NicknameInvalidException.class)
                .hasMessage("昵称必须是1到10位，且只能包含中文、英文、数字和下划线");
    }

    @Test
    void 手机号格式正确时_createUser_成功() {
        var validPhones = java.util.List.of("13800138000", "19912345678");

        for (var phone : validPhones) {
            assertThatCode(() -> User.createUser("昵称", phone, "avatar-id", new byte[0]))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    void 手机号格式不正确时_createUser_抛异常() {
        var invalidPhones = java.util.List.of("", "1234567890", "123456789012", "1380013800a", "1380013-000");

        for (var phone : invalidPhones) {
            assertThatThrownBy(() -> User.createUser("昵称", phone, "avatar-id", new byte[0]))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("手机号必须是11位数字");
        }
    }

    @Test
    void 手机号格式不正确时_of_抛异常() {
        assertThatThrownBy(() -> User.of("123456789", "昵称", "1380013800a", "desc", "avatar-id"))
                .isInstanceOf(DomainException.class)
                .hasMessage("手机号必须是11位数字");
    }

    @Test
    void 账号不正确时_of_抛异常() {
        var invalidAccounts = java.util.List.of("", "abc", "012345678", "12345678", "1234567890", "1380013800a");

        for (var account : invalidAccounts) {
            assertThatThrownBy(() -> User.of(account, "昵称", "13800138000", "desc", "avatar-id"))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("用户ID必须是9位数字字符串，且首位不能为0");
        }
    }
}