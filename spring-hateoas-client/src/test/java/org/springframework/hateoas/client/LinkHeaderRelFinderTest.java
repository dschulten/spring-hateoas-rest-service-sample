package org.springframework.hateoas.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public class LinkHeaderRelFinderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFindRels() throws Exception {

	}

	@Test
	public void testRelsFromLinkHeaderSimpleLink() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		String headerValue = "<http://example.com/TheBook/chapter2>; " + "rel=\"previous\"; "
				+ "title=\"previous chapter\"";
		headers.set("Link", headerValue);
		LinkHeaderRelFinder linkHeaderRelFinder = new LinkHeaderRelFinder(headers);

		MultiValueMap<String, Link> relsFromLinkHeader = linkHeaderRelFinder.findRels();

		assertTrue("rel previous not found", relsFromLinkHeader.containsKey("previous"));
		Link link = relsFromLinkHeader.get("previous").get(0);
		assertEquals("http://example.com/TheBook/chapter2", link.getHref());
		assertEquals("previous", link.getRel());
	}

	@Test
	public void testRelsFromLinkHeaderExtensionRel() throws Exception {

		String extensionRel = "http://example.net/foo";
		HttpHeaders headers = new HttpHeaders();
		String extensionRelLink = "</>; rel=\"" + extensionRel + "\"";
		headers.set("Link", extensionRelLink);

		LinkHeaderRelFinder linkHeaderRelFinder = new LinkHeaderRelFinder(headers);

		MultiValueMap<String, Link> relsFromLinkHeader = linkHeaderRelFinder.findRels();

		assertTrue("rel " + extensionRel + " not found", relsFromLinkHeader.containsKey(extensionRel));
		Link link = relsFromLinkHeader.get(extensionRel).get(0);
		assertEquals("/", link.getHref());
		assertEquals(extensionRel, link.getRel());

	}

	@Test
	public void testRelsFromLinkHeaderMultiLink() throws Exception {

		String previousRel = "previous";
		String nextRel = "next";
		HttpHeaders headers = new HttpHeaders();
		String multiLink = "</TheBook/chapter2>;" + "rel=\"previous\"; title*=UTF-8'de'letztes%20Kapitel,"
				+ "</TheBook/chapter4>;" + "rel=\"next\"; title*=UTF-8'de'n%c3%a4chstes%20Kapitel";
		headers.set("Link", multiLink);
		LinkHeaderRelFinder linkHeaderRelFinder = new LinkHeaderRelFinder(headers);

		MultiValueMap<String, Link> relsFromLinkHeader = linkHeaderRelFinder.findRels();

		assertTrue("rel " + previousRel + " not found", relsFromLinkHeader.containsKey(previousRel));
		Link linkPrevious = relsFromLinkHeader.get(previousRel).get(0);
		assertEquals("/TheBook/chapter2", linkPrevious.getHref());
		assertEquals(previousRel, linkPrevious.getRel());

		assertTrue("rel " + nextRel + " not found", relsFromLinkHeader.containsKey(nextRel));
		Link linkNext = relsFromLinkHeader.get(nextRel).get(0);
		assertEquals("/TheBook/chapter4", linkNext.getHref());
		assertEquals(nextRel, linkNext.getRel());

	}

}
