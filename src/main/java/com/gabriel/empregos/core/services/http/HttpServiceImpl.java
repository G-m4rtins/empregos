package com.gabriel.empregos.core.services.http;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class HttpServiceImpl implements HttpService {

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Override
    public <T> Optional<T> getPathVariable(String name, Class<T> type) {
        var attrs = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        var pathVariables = objectMapper.convertValue(attrs, new TypeReference<Map<String, String>>() {});
        var value = pathVariables.getOrDefault(name, null);
        if (value == null) {
            return Optional.empty();
        }

        return Optional.of(objectMapper.convertValue(value, type));
    }

}
