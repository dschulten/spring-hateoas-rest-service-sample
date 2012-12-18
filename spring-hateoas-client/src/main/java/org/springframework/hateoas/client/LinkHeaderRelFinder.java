package org.springframework.hateoas.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class LinkHeaderRelFinder implements RelFinder {

	private static final String HEADER_LINK = "Link";
	private static Log LOG = LogFactory.getLog(LinkHeaderRelFinder.class);

	private HttpHeaders headers;

	public LinkHeaderRelFinder(HttpHeaders headers) {
		super();
		this.headers = headers;
	}

	public MultiValueMap<String, Link> findRels() {

		MultiValueMap<String, Link> ret = new LinkedMultiValueMap<String, Link>();

		List<String> linkHeaders = headers.get(HEADER_LINK);
		if (linkHeaders == null)
			return ret;

		String uriReference = null;
		boolean trimTokens = true;
		boolean ignoreEmptyTokens = true;
		for (String linkHeader : linkHeaders) {
			Map<String, String> linkParams = new HashMap<String, String>();

			String[] linkValues = linkHeader.split(",");
			for (String linkValue : linkValues) {
				String[] linkParts = StringUtils.tokenizeToStringArray(linkValue, "<>;", trimTokens, ignoreEmptyTokens);

				// String[] linkParts = linkValue.split(";");
				uriReference = linkParts[0];

				// build link params
				for (int i = 1; i < linkParts.length; i++) {
					String[] tokens = StringUtils.tokenizeToStringArray(linkParts[i], "=\"", trimTokens, ignoreEmptyTokens);
					linkParams.put(tokens[0], tokens[1]);
				}
				String rel = linkParams.get("rel");
				Link link = new Link(uriReference, rel);
				ret.add(rel, link);
//				if (replaced != null) {
//					LOG.warn("found duplicate rel " + rel + ", replacing " + replaced + " by " + link);
//				}
			}
		}
		if (uriReference == null)
			throw new IllegalArgumentException("Link header found without URI-Reference");
		return ret;
	}
}
