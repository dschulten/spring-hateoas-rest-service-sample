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
import org.springframework.hateoas.client.Browsable;
import org.springframework.hateoas.client.Browser;
import org.springframework.hateoas.util.Args;
import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpMethod;

public class CommonsHttpClient4Browser implements Browser {

    private HttpClient httpClient = new DefaultHttpClient();

    private URI entryPoint;

    private Browsable currentResource;

    public CommonsHttpClient4Browser(URI entryPoint) {
        super();
        this.entryPoint = entryPoint;
    }

    public Browsable getCurrentResource() {
        try {
            if (currentResource == null) {
                ResponseHandler<Browsable> responseHandler = new BrowsableResponseHandler();
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
    public Browser followRel(String rel) {
        try {
            ResponseHandler<Browsable> responseHandler = new BrowsableResponseHandler();
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

    public Browser submitForm(String name,
            Args values) {
        try {
            // NavigableResponseHandler returns xhtml or xforms or whatever
            // navigator, capable of handling its content
            ResponseHandler<Browsable> responseHandler = new BrowsableResponseHandler();

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
