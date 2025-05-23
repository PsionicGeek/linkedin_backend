package org.psionicgeek.linkedin.postservice.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        List<String> allowedPaths = Arrays.asList("/v3/api-docs", "/swagger-ui", "/webjars","actuator");
        String path = request.getURI().getPath();
        boolean isAllowedPath = allowedPaths.stream().anyMatch(path::contains);
        if (isAllowedPath) {
            return body;
        }

        if(body instanceof ApiResponse<?>) {
            return body;
        }

        return new ApiResponse<>(body);
    }
}
