package com.greamz.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class GlobalState {
    @Value("${application.frontend.url}")
    public String FRONTEND_URL;
}
