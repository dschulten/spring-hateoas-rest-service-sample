package org.springframework.hateoas.client;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;

public interface BrowsableResource {

    void process(InputStream content, HttpHeaders httpHeaders);

    FormRequest getFormRequest(String formName, Map<String, List<? extends Object>> args);

    Link getRel(String rel);

    String toString();

}
