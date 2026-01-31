package com.haroun.bugoverflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProyectoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Proyecto getProyectoSample1() {
        return new Proyecto().id(1L).titulo("titulo1").descripcion("descripcion1").urlrepo("urlrepo1");
    }

    public static Proyecto getProyectoSample2() {
        return new Proyecto().id(2L).titulo("titulo2").descripcion("descripcion2").urlrepo("urlrepo2");
    }

    public static Proyecto getProyectoRandomSampleGenerator() {
        return new Proyecto()
            .id(longCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .urlrepo(UUID.randomUUID().toString());
    }
}
