package org.acme.constant;

public interface ValueEnum {
    String value();

    static <T extends Enum<T> & ValueEnum> T fromValue(String v,Class<T> tClass) {
        for(T value : tClass.getEnumConstants()) {
            if(value.value().equals(v)) {
                return value;
            }
        }
        return null;
    }
}
