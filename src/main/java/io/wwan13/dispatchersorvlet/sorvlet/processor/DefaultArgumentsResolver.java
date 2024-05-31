package io.wwan13.dispatchersorvlet.sorvlet.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wwan13.dispatchersorvlet.exception.ResolveArgumentException;
import io.wwan13.dispatchersorvlet.sorvlet.ArgumentsResolver;
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandler;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.KeyParameter;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestBody;
import io.wwan13.dispatchersorvlet.sorvlet.dto.request.SocketRequest;

import java.lang.reflect.Parameter;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class DefaultArgumentsResolver implements ArgumentsResolver {

    private final static ObjectMapper objectMapper = new ObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String KEY_PARAMETER_DELIMITER = "_";
    private static final String KEY_PARAMETER_FORMAT = "{%s}";

    public Object[] resolve(RequestHandler handler, SocketRequest request) {
        Parameter[] parameters = handler.getHandlerParameters();
        int parametersLength = parameters.length;
        Object[] arguments = new Object[parametersLength];

        for (int i = 0; i < parametersLength; i++) {
            Parameter parameter = parameters[i];
            arguments[i] = resolveArgument(parameter, request, handler);
        }

        return arguments;
    }

    private Object resolveArgument(
            Parameter parameter,
            SocketRequest request,
            RequestHandler handler
    ) {
        if (parameter.isAnnotationPresent(RequestBody.class)) {
            return resolveRequestBody(request.body(), parameter);
        }

        if (parameter.isAnnotationPresent(KeyParameter.class)) {
            return resolveKeyParameter(handler.requestKey(), request.key(), parameter);
        }

        throw new ResolveArgumentException();
    }

    private Object resolveRequestBody(Object requestBody, Parameter parameter) {
        return objectMapper.convertValue(requestBody, parameter.getType());
    }

    private Object resolveKeyParameter(
            String registeredKey,
            String requestKey,
            Parameter parameter
    ) {
        String keyParameter = String.format(KEY_PARAMETER_FORMAT, parameter.getName());
        int keyParameterIndex = List.of(registeredKey.split(KEY_PARAMETER_DELIMITER))
                .indexOf(keyParameter);

        String givenValue = List.of(requestKey.split(KEY_PARAMETER_DELIMITER))
                .get(keyParameterIndex);
        return objectMapper.convertValue(givenValue, parameter.getType());
    }
}
