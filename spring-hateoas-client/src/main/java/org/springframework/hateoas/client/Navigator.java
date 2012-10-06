package org.springframework.hateoas.client;

import org.springframework.hateoas.util.Args;

public interface Navigator {

    /**
     * Follows a link marked with the given rel
     *
     * @param rel
     * @return Navigator
     */
    public Navigator followRel(String rel);

    /**
     * Submits a form which has the given name, filling in the given values.
     *
     * @return
     */
    public Navigator submitForm(String name, Args values);



}
