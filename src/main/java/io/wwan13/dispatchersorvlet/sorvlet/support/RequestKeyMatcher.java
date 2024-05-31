package io.wwan13.dispatchersorvlet.sorvlet.support;

public class RequestKeyMatcher {

    private static final String KEY_DELIMITER = "_";
    private static final String KEY_PARAMETER_PREFIX = "{";
    private static final String KEY_PARAMETER_SUFFIX = "}";

    public boolean matches(String registered, String entered) {
        String[] registeredElements = registered.split(KEY_DELIMITER);
        String[] enteredElements = entered.split(KEY_DELIMITER);

        if (registeredElements.length != enteredElements.length) {
            return false;
        }

        for (int i = 0; i < registeredElements.length; i++) {
            String registeredElement = registeredElements[i];
            String enteredElement = enteredElements[i];

            if (!registeredElement.equals(enteredElement) && !isKeyParameter(registeredElement)) {
                return false;
            }
        }

        return true;
    }

    private boolean isKeyParameter(String registeredElement) {
        return registeredElement.startsWith(KEY_PARAMETER_PREFIX) &&
                registeredElement.endsWith(KEY_PARAMETER_SUFFIX);
    }
}
