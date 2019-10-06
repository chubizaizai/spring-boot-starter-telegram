package com.github.kshashov.telegram.handler.response;

import com.github.kshashov.telegram.api.TelegramRequest;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

/**
 * Add support for {@link String} return type
 */
@Component
public class BotResponseBodyMethodProcessor implements BotHandlerMethodReturnValueHandler {
    final private ConversionService conversionService;

    public BotResponseBodyMethodProcessor(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public BaseRequest handleReturnValue(Object returnValue, MethodParameter returnType, TelegramRequest telegramRequest) {
        String outputValue = null;
        Class<?> valueType;

        if (returnValue instanceof CharSequence) {
            outputValue = returnValue.toString();
        } else {
            valueType = getReturnValueType(returnValue, returnType);
            if (conversionService.canConvert(valueType, String.class)) {
                outputValue = conversionService.convert(returnValue, String.class);
            } else if (conversionService.canConvert(returnType.getParameterType(), String.class)) {
                outputValue = conversionService.convert(returnType.getParameterType(), String.class);
            }
        }

        if (outputValue != null) {
            if (telegramRequest.getChat() != null) {
                return new SendMessage(telegramRequest.getChat().id(), outputValue);
            }
        }

        return null;
    }

    private Class<?> getReturnValueType(Object value, MethodParameter returnType) {
        return (value != null ? value.getClass() : returnType.getParameterType());
    }

}
