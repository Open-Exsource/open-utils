package net.exsource.openutils.io;

import java.util.List;
import java.util.Map;

public record Value(Object object) {

    public String asString() {
        return (String) object;
    }

    public Boolean asBoolean() {
        return Boolean.parseBoolean(asString());
    }

    public Integer asInteger() {
        return Integer.parseInt(asString());
    }

    public Double asDouble() {
        return Double.parseDouble(asString());
    }

    public Float asFloat() {
        return Float.parseFloat(asString());
    }

    public Long asLong() {
        return Long.parseLong(asString());
    }

    public Short asShort() {
        return Short.parseShort(asString());
    }

    public Byte asByte() {
        return Byte.parseByte(asString());
    }

    public Object[] asArray() {
        return null;
    }

    public List<Object> asList() {
        return null;
    }

    public Map<String, Object> asMap() {
        return null;
    }

}
