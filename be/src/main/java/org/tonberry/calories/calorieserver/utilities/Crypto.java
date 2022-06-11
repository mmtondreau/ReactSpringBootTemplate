package org.tonberry.calories.calorieserver.utilities;

import com.google.common.hash.Hashing;
import lombok.NonNull;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class Crypto {
    public static String encodeBase64(@NonNull String plaintext) {
        return new String(Base64.encodeBase64(plaintext.getBytes()));
    }
    public static String decodeBase64(@NonNull String encodedString) {
        return new String(Base64.decodeBase64(encodedString.getBytes()));
    }

    @SuppressWarnings("UnstableApiUsage") // Hsshing marked BETA
    public static String hashSha256(String plaintext) {
        return "SHA256:" + Hashing.sha256()
                .hashString(plaintext, StandardCharsets.UTF_8)
                .toString();
    }
}
