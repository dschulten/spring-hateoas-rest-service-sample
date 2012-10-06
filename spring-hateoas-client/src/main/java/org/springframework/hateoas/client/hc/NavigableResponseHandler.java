package org.springframework.hateoas.client.hc;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.hateoas.client.BrowsableResource;
import org.springframework.hateoas.client.NavigatorRegistry;
import org.springframework.http.HttpHeaders;

public class NavigableResponseHandler implements
        ResponseHandler<BrowsableResource> {

    private NavigatorRegistry navigatorRegistry;

    public BrowsableResource handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {

        HttpEntity entity = response.getEntity();
        Header contentType = entity.getContentType();
        HeaderElement[] elements = contentType.getElements();
        if (elements.length != 1) {
            throw new IllegalArgumentException(
                    "more than one response mime type currently not supported");
        }
        String mimeType = elements[0].getName();

        BrowsableResource navigable = navigatorRegistry.getNavigator(mimeType);

        // navigator.setURL(response.);

        HttpHeaders httpHeaders = toHttpHeaders(response.getAllHeaders());

        navigable.process(entity.getContent(), httpHeaders);

        return navigable;

    }

    private HttpHeaders toHttpHeaders(Header[] allHeaders) {
        // TODO Auto-generated method stub
        return null;
    }

    public NavigatorRegistry getNavigatorRegistry() {
        return navigatorRegistry;
    }

    @Required
    public void setNavigatorRegistry(NavigatorRegistry navigatorRegistry) {
        this.navigatorRegistry = navigatorRegistry;
    }

}
