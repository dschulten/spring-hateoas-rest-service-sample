package org.springframework.hateoas.client.hc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.client.Action;
import org.springframework.hateoas.client.Browsable;
import org.springframework.hateoas.client.Browser;
import org.springframework.hateoas.client.FormRequest;
import org.springframework.hateoas.client.Identifier;
import org.springframework.hateoas.client.Agent;
import org.springframework.hateoas.client.SubmitFormAction;
import org.springframework.hateoas.util.Args;
import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpMethod;

public class CommonsHttpClient4Browser implements Browser {

	private HttpClient httpClient = new DefaultHttpClient();

	private URI entryPoint;

	private URI context;

	private Browsable currentResource;

	public CommonsHttpClient4Browser(URI entryPoint) {
		super();
		// TODO remove entryPoint for context?
		this.entryPoint = entryPoint;
		this.context = entryPoint;
	}

	public Browsable getCurrentResource() {
		try {
			if (currentResource == null) {
				ResponseHandler<Browsable> responseHandler = new BrowsableResponseHandler();
				System.out.println("starting at URI " + entryPoint);
				HttpGet httpGet = createGet(entryPoint);
				currentResource = httpClient.execute(httpGet, responseHandler);
				setCurrentContext(entryPoint);
			}
			return currentResource;
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}

	private HttpGet createGet(URI uri) {
		HttpGet httpGet = new HttpGet(uri);
		httpGet.addHeader("Accept", "text/html, application/rdf+xml, application/xml, application/json");
		return httpGet;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Follows a rel, for well-known rels see String constants in {@link Link}
	 */
	public Browser followRel(String rel) {
		Link target = getCurrentResource().getRel(rel);
		System.out.println("following rel " + rel + " to " + target.getHref());
		currentResource = followLink(target);

		return this;
	}

	private Browsable followLink(Link target) {
		try {
			ResponseHandler<Browsable> responseHandler = new BrowsableResponseHandler();
			URI uri = new URI(target.getHref());
			HttpGet httpGet = createGet(uri);
			Browsable browsable = httpClient.execute(httpGet, responseHandler);
			setCurrentContext(uri);
			return browsable;
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}

	private void setCurrentContext(URI uri) throws URISyntaxException {
		context = new URI(uri.getScheme() + "://" + uri.getAuthority());
	}

	public Browser submitForm(String name, Args values) {
		try {
			// ResponseHandler returns xhtml or xforms or whatever
			// browsable, capable of handling its content
			ResponseHandler<Browsable> responseHandler = new BrowsableResponseHandler();

			FormRequest formRequest = getCurrentResource().getFormRequest(name, values);
			System.out.println("submitting form " + name + " with " + formRequest);
			URI uri = formRequest.getURI();
			if (!uri.isAbsolute()) {
				uri = new URI(context.toASCIIString() + uri.toASCIIString());
			}
			HttpMethod method = formRequest.getMethod();
			switch (method) {
			case GET: {
				uri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), formRequest.getRequestBody(), null);
				HttpGet httpGet = createGet(uri);
				currentResource = httpClient.execute(httpGet, responseHandler);
				break;
			}
			case POST: {
				HttpPost httpPost = new HttpPost(uri);
				StringEntity entity = new StringEntity(formRequest.getRequestBody(), ContentType.create(
						formRequest.getContentType(), formRequest.getEncoding()));
				httpPost.setEntity(entity);
				currentResource = httpClient.execute(httpPost, responseHandler);
				break;
			}
			default:
				throw new IllegalArgumentException("unsupported http method found for form");
			}
			setCurrentContext(uri);
			return this;
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}


	public URI getCurrentContext() {
		return context;
	}

}
