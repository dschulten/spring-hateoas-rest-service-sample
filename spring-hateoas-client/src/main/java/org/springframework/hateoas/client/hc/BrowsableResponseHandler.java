package org.springframework.hateoas.client.hc;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.hateoas.client.Browsable;
import org.springframework.hateoas.client.BrowsableRegistry;
import org.springframework.http.HttpHeaders;

public class BrowsableResponseHandler implements ResponseHandler<Browsable> {

	private BrowsableRegistry browsableRegistry = new BrowsableRegistry();

	public Browsable handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() >= 300) {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		// BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		// String line = null;
		// StringBuilder sb = new StringBuilder();
		// while(null != (line = bufferedReader.readLine())) {
		// System.out.println(line);
		// sb.append(line);
		// }
		// inputStream = new ByteArrayInputStream(sb.toString().getBytes());

		Header contentType = entity.getContentType();
		HeaderElement[] elements = contentType.getElements();
		if (elements.length != 1) {
			throw new IllegalArgumentException("more than one response mime type currently not supported");
		}
		String mimeType = elements[0].getName();

		Browsable browsable = browsableRegistry.getBrowsable(mimeType);

		// navigator.setURL(response.);

		HttpHeaders httpHeaders = toHttpHeaders(response.getAllHeaders());

		browsable.process(inputStream, httpHeaders);

		return browsable;

	}

	private HttpHeaders toHttpHeaders(Header[] allHeaders) {
		HttpHeaders ret = new HttpHeaders();
		for (Header header : allHeaders) {
			ret.add(header.getName(), header.getValue());
		}
		return ret;
	}

	public BrowsableRegistry getBrowsableRegistry() {
		return browsableRegistry;
	}

	@Required
	public void setBrowsableRegistry(BrowsableRegistry browsableRegistry) {
		this.browsableRegistry = browsableRegistry;
	}

}
