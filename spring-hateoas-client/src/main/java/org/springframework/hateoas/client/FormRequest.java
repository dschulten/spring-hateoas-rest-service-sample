package org.springframework.hateoas.client;

import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.hateoas.util.Failure;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

public class FormRequest {

	private HttpMethod method;
	private URI uri;
	private String contentType;
	private String encoding = "UTF-8";
	private MultiValueMap<String, String> requestValues;

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

	public FormRequest(HttpMethod method, MultiValueMap<String, String> requestValues, URI uri, String contentType,
			String encoding) {
		super();
		this.method = method;
		this.requestValues = requestValues;
		this.uri = uri;
		this.contentType = contentType;
		this.encoding = encoding;
	}

	public String getRequestBody() {
		try {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, List<String>> entry : requestValues.entrySet()) {
				String key = entry.getKey();
				List<? extends Object> values = entry.getValue();
				for (Object value : values) {
					if (sb.length() > 0)
						sb.append("&");
					sb.append(URLEncoder.encode(key, encoding)).append("=");
					sb.append(URLEncoder.encode(value.toString(), encoding));
				}
			}
			return sb.toString();
		} catch (Exception e) {
			throw Failure.asUnchecked(e);
		}
	}

	@Override
	public String toString() {
		return "FormRequest [method=" + method + ", requestValues=" + requestValues + ", uri=" + uri + ", contentType="
				+ contentType + ", encoding=" + encoding + "]";
	}

}
