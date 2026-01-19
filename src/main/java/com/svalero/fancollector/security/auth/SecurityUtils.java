package com.svalero.fancollector.security.auth;

import org.springframework.security.core.Authentication;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static String email(Authentication auth) {
        return auth != null ? auth.getName() : null;
    }

    public static boolean hasRol(Authentication auth, String rol) {
        if (auth == null || auth.getAuthorities() == null) return false;

        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + rol));
    }

    public static boolean isAdmin(Authentication auth) {
        return hasRol(auth, "ADMIN");
    }

    public static boolean isMods(Authentication auth) {
        return hasRol(auth, "MODS");
    }

    public static boolean isUser(Authentication auth) {
        return hasRol(auth, "USER");
    }
}