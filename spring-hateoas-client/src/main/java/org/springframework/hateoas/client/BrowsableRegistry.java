package org.springframework.hateoas.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.util.Failure;

public class BrowsableRegistry {

    Map<String, Class<? extends Browsable>> navigators = new HashMap<String, Class<? extends Browsable>>();

    public BrowsableRegistry() {
        super();
        this.navigators.put("text/xhtml", XHtmlBrowsable.class);
    }

    public Browsable getBrowsable(String mimeType) {
        try {
            Class<? extends Browsable> navigatorClass = navigators
                    .get(mimeType);

            if (navigatorClass == null)
                throw new IllegalStateException(
                        "no navigator class defined for " + mimeType);

            return navigatorClass.newInstance();
        } catch (Exception e) {
            throw Failure.asUnchecked(e);
        }

    }

    void addNavigator(String mimeType, Class<? extends Browsable> navigatorClass) {
        navigators.put(mimeType, navigatorClass);
    }
}
