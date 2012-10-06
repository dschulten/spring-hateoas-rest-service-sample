package org.springframework.hateoas.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.util.Failure;

public class NavigatorRegistry {

    Map<String, Class<? extends BrowsableResource>> navigators = new HashMap<String, Class<? extends BrowsableResource>>();

    public NavigatorRegistry() {
	super();
	this.navigators.put("text/xhtml", XHtmlNavigable.class);
    }

    public BrowsableResource getNavigator(String mimeType) {
	try {
	    Class<? extends BrowsableResource> navigatorClass = navigators
		    .get(mimeType);

	    if (navigatorClass == null)
		throw new IllegalStateException(
			"no navigator class defined for " + mimeType);

	    return navigatorClass.newInstance();
	} catch (Exception e) {
	    throw Failure.asUnchecked(e);
	}

    }

    void addNavigator(String mimeType,
	    Class<? extends BrowsableResource> navigatorClass) {
	navigators.put(mimeType, navigatorClass);
    }
}
