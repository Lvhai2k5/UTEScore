package vn.ute.utescore.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Tiện ích hỗ trợ tạo và xác thực chữ ký (HMAC-SHA512) cho giao dịch VNPAY.
 *
 * <p>Hàm {@link #hmacSHA512(String, String)} dùng để sinh chữ ký
 * cho dữ liệu gửi đi, theo đúng chuẩn của VNPAY.
 * Hàm {@link #verifySignature(Map, String)} giúp kiểm tra chữ ký trả về
 * khi VNPAY redirect hoặc gửi IPN.</p>
 *
 * @author
 * @since 2025
 */
public class CustomerVnPayUtil {

    /**
     * Sinh chữ ký HMAC-SHA512.
     *
     * @param key  Chuỗi bí mật (vnp_HashSecret)
     * @param data Chuỗi dữ liệu cần ký (dạng key=value&key=value)
     * @return Chữ ký ở dạng hex chữ thường
     */
    public static String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) return null;
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký HmacSHA512", e);
        }
    }

    /**
     * Xác minh chữ ký trả về từ VNPAY.
     *
     * @param vnpParams   Map chứa toàn bộ tham số trả về từ VNPAY (bao gồm cả vnp_SecureHash)
     * @param hashSecret  Chuỗi bí mật (vnp_HashSecret)
     * @return true nếu chữ ký hợp lệ, false nếu sai
     */
    public static boolean verifySignature(Map<String, String> vnpParams, String hashSecret) {
        if (vnpParams == null || vnpParams.isEmpty() || hashSecret == null) {
            return false;
        }

        // Lấy chữ ký được gửi về và loại bỏ khỏi map
        String vnp_SecureHash = vnpParams.remove("vnp_SecureHash");
        if (vnp_SecureHash == null || vnp_SecureHash.isEmpty()) return false;

        // Sắp xếp các key theo thứ tự a-z
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        // Ghép chuỗi theo format key=value&key=value...
        StringJoiner hashData = new StringJoiner("&");
        for (String name : fieldNames) {
            String value = vnpParams.get(name);
            if (value != null && !value.isBlank()) {
                hashData.add(name + "=" + value);
            }
        }

        // Tạo lại chữ ký và so sánh
        String calculatedHash = hmacSHA512(hashSecret, hashData.toString());
        return calculatedHash.equalsIgnoreCase(vnp_SecureHash);
    }
}
