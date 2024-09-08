package utils;

import java.util.UUID;

public class CommonFunc {
    private static String generateActivityId() {
        return "a-" + UUID.randomUUID().toString();
    }
}
