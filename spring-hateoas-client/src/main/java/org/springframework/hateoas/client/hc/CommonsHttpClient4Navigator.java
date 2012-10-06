package org.springframework.hateoas.client.hc;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.client.FormRequest;
import org.springframework.hateoas.client.BrowsableResource;
import org.springframework.hateoas.client.Navigator;
import org.springframework.hateoas.util.Args;
import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpMethod;

public class CommonsHttpClient4Navigator implements Navigator {

    private HttpClient httpClient = new DefaultHttpClient();

    private URI entryPoint;

    private BrowsableResource currentResource;

    public CommonsHttpClient4Navigator(URI entryPoint) {
        super();
        this.entryPoint = entryPoint;
    }

    public BrowsableResource getCurrentResource() {
        try {
            if (currentResource == null) {
                ResponseHandler<BrowsableResource> responseHandler = new NavigableResponseHandler();
                HttpGet httpGet = new HttpGet(entryPoint);
                currentResource = httpClient.execute(httpGet, responseHandler);
            }
            return currentResource;
        } catch (Exception e) {
            throw Failure.asUnchecked(e);
        }
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Follows a rel, for well-known rels see String constants in {@link Link}
     */
    public Navigator followRel(String rel) {
        try {
            ResponseHandler<BrowsableResource> responseHandler = new NavigableResponseHandler();
            Link target = getCurrentResource().getRel(rel);

            // TODO use options to see what methods are supported - currently, use GET
            URI uri;
            uri = new URI(target.getHref());
            HttpGet httpGet = new HttpGet(uri);
            currentResource = httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            throw Failure.asUnchecked(e);
        }
        return this;
    }

    public Navigator submitForm(String name,
            Args values) {
        try {
            // NavigableResponseHandler returns xhtml or xforms or whatever
            // navigator, capable of handling its content
            ResponseHandler<BrowsableResource> responseHandler = new NavigableResponseHandler();

            FormRequest formRequest = getCurrentResource().getFormRequest(name,
                    values);
            URI uri = formRequest.getURI();
            HttpMethod method = formRequest.getMethod();
            switch (method) {
            case GET: {
                HttpGet httpGet = new HttpGet(uri);
                currentResource = httpClient.execute(httpGet, responseHandler);
                break;
            }
            case POST: {
                HttpPost httpPost = new HttpPost(uri);
                StringEntity entity = new StringEntity("important message",
                        ContentType.create(formRequest.getContentType(),
                                formRequest.getEncoding()));
                httpPost.setEntity(entity);
                currentResource = httpClient.execute(httpPost, responseHandler);
                break;
            }
            default:
                throw new IllegalArgumentException(
                        "unsupported http method found for form");
            }
            return this;
        } catch (Exception e) {
            throw Failure.asUnchecked(e);
        }
    }

}
