package vn.ute.utescore.utils;

import java.util.Set;

public class CustomerChatPolicy {
    private static final Set<String> BLOCKED = Set.of(
        "mật khẩu", "password", "cvv", "cmnd", "cccd",
        "số điện thoại", "email", "tài khoản", "thanh toán"
    );

    public static boolean isSensitive(String msg) {
        String m = msg.toLowerCase();
        return BLOCKED.stream().anyMatch(m::contains);
    }
}
