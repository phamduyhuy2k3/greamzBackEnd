package com.greamz.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    public static Cookie getCookie(HttpServletRequest httpServletRequest, String cookieKey) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals(cookieKey)) {
                System.out.println(cookie.getValue());
                return cookie;
            }
        }

        return null;
    }

    public static void addCookie(HttpServletResponse httpServletResponse,
                                 String cookieKey,
                                 String cookieValue) {
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        httpServletResponse.addCookie(cookie);
    }
    public static void removeCookies(HttpServletRequest request,
                                     HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            removeCookie(response, cookie.getName());
        }
    }
    public static void removeCookie(HttpServletResponse response,
                                    String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
