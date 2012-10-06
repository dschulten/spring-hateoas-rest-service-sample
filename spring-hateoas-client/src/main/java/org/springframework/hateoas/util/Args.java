package org.springframework.hateoas.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Args extends LinkedHashMap<String, List<? extends Object>> {

    private static final long serialVersionUID = -6154640701723286699L;

    public static Args from(String key, Object... values) {
        Args args = new Args();
        args.put(key, Arrays.asList(values));
        return args;
    }

    public static Args from(Arg... args) {
        Args ret = new Args();
        for (Arg arg : args) {
            ret.put(arg.getKey(), arg.getValue());
        }
        return ret;
    }

    public static Args from(String key, Object value) {
        Args args = new Args();
        args.put(key, Arrays.asList(value));
        return args;
    }

    public static Args from(String key1, Object value1, String key2,
            Object value2) {
        Args args = new Args();
        args.put(key1, Arrays.asList(value1));
        args.put(key2, Arrays.asList(value2));
        return args;
    }

    public static Args from(String key1, Object value1, String key2,
            Object value2, String key3, Object value3) {
        Args args = new Args();
        args.put(key1, Arrays.asList(value1));
        args.put(key2, Arrays.asList(value2));
        args.put(key3, Arrays.asList(value3));

        return args;

    }

}
