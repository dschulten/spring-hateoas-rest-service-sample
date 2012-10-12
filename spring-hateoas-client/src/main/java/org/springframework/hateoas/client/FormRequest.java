package org.springframework.hateoas.client;

import java.net.URI;

import org.springframework.http.HttpMethod;

public class FormRequest {

	private HttpMethod method;
	private String requestBody;
	private URI uri;
	private String contentType;
	private String encoding = "UTF-8";

	public String getEncoding() {
		return encoding;
	}

	public URI getURI() {
		return uri;
	}

	public String getContentType() {
		return contentType;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public FormRequest(HttpMethod method, String requestBody, URI uri, String contentType, String encoding) {
		super();
		this.method = method;
		this.requestBody = requestBody;
		this.uri = uri;
		this.contentType = contentType;
		this.encoding = encoding;
	}

	@Override
	public String toString() {
		return "FormRequest [method=" + method + ", requestBody=" + requestBody + ", uri=" + uri + ", contentType="
				+ contentType + ", encoding=" + encoding + "]";
	}



}
