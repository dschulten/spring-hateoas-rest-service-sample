package org.springframework.hateoas.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Arg implements
        Map.Entry<String, List<? extends Object>> {

    private String key;
    private List<? extends Object> valueList;

    public static Arg anArg(String key, Object... values) {
        Arg arg = new Arg();
        arg.setKey(key);
        arg.setValue(Arrays.asList(values));
        return arg;
    }

    private void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public List<? extends Object> getValue() {
        return valueList;
    }

    public List<? extends Object> setValue(List<? extends Object> valueList) {
        List<? extends Object> replaced = this.valueList;
        this.valueList = valueList;
        return replaced;
    }

}