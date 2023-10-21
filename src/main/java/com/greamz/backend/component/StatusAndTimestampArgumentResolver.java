package com.greamz.backend.component;

import com.greamz.backend.annotations.WithStatusAndTimestamp;
import com.greamz.backend.common.ResponseWrapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;

public class StatusAndTimestampArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WithStatusAndTimestamp.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return new ResponseWrapper("SUCCESS", LocalDateTime.now(), parameter.getMethod().invoke(parameter.getDeclaringClass().newInstance()));
    }


}
