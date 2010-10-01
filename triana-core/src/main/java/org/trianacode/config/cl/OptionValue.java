package org.trianacode.config.cl;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Oct 1, 2010
 */
public class OptionValue {

    private Option option;
    private List<String> values;

    public OptionValue(Option option, List<String> values) {
        this.option = option;
        this.values = values;
    }

    public Option getOption() {
        return option;
    }

    public List<String> getValues() {
        return values;
    }

    public boolean hasValue() {
        return values != null && values.size() > 0;
    }

    public String getValue() {
        if (hasValue()) {
            return values.get(0);
        }
        return null;
    }
}
