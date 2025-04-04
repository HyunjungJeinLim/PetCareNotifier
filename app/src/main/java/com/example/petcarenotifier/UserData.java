package com.example.petcarenotifier;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    public static Map<String, String> users = new HashMap<>();

    static {
        users.put("user1", "password123");
    }
}