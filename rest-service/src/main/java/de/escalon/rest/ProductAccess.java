package de.escalon.rest;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class ProductAccess {

    public Iterable<? extends Product> getProductsOfPerson(Long personId) {

        return Arrays.asList(new Product(1L,
                "Starfleet Standard Tricorder"), new Product(2L,
                "Flux Capacitor"), new Product(1L, "Phase Calibrator"));

    }

}
