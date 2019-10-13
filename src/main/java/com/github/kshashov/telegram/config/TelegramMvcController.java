package com.github.kshashov.telegram.config;

import com.github.kshashov.telegram.api.bind.annotation.BotRequest;

/**
 * An interface from which all classes marked with annotation {@link BotRequest} must
 * inherit
 *
 * @see BotRequest
 * @see TelegramControllerBeanPostProcessor
 */
public interface TelegramMvcController {
    String getToken();
}
