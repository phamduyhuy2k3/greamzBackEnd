package com.greamz.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CookieUtils {
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    System.out.println("cookie.getValue() = " + cookie.getValue());
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    public static Cookie addCookie(HttpServletResponse response, String name, String value, Duration maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) maxAge.getSeconds());
        response.addCookie(cookie);
        return cookie;
    }
    public static HttpHeaders addCookieToResponse(HttpServletResponse response, String name, String value, Duration maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) maxAge.getSeconds());
        response.addCookie(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.getName() + "=" + cookie.getValue());
        return headers;
    }
    public static HttpHeaders addCookieToResponse(HttpServletResponse response, List<String> name, List<String> value, Duration maxAge) {
        HttpHeaders headers = new HttpHeaders();
        for (int i = 0; i < name.size(); i++) {
            Cookie cookie = new Cookie(name.get(i), value.get(i));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) maxAge.getSeconds());
            response.addCookie(cookie);
            headers.add(HttpHeaders.SET_COOKIE, cookie.getName() + "=" + cookie.getValue());
        }
        return headers;
    }
    public static HttpHeaders addCookieToResponse(HttpServletResponse response, Map<String,Object> keyValueCookie, Duration maxAge) {
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, Object> entry : keyValueCookie.entrySet()) {
            Cookie cookie = new Cookie(entry.getKey(), entry.getValue().toString());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) maxAge.getSeconds());
            response.addCookie(cookie);
            headers.add(HttpHeaders.SET_COOKIE, cookie.getName() + "=" + cookie.getValue());
        }
        return headers;
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(String cookieValue, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookieValue)));
    }
}
