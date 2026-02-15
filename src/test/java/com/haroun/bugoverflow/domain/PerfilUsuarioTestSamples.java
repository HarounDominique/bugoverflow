package com.haroun.bugoverflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PerfilUsuarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PerfilUsuario getPerfilUsuarioSample1() {
        return new PerfilUsuario()
            .id(1L)
            .nombreVisible("nombreVisible1")
            .bio("bio1")
            .github("github1")
            .webPersonal("webPersonal1")
            .avatarUrl("avatarUrl1");
    }

    public static PerfilUsuario getPerfilUsuarioSample2() {
        return new PerfilUsuario()
            .id(2L)
            .nombreVisible("nombreVisible2")
            .bio("bio2")
            .github("github2")
            .webPersonal("webPersonal2")
            .avatarUrl("avatarUrl2");
    }

    public static PerfilUsuario getPerfilUsuarioRandomSampleGenerator() {
        return new PerfilUsuario()
            .id(longCount.incrementAndGet())
            .nombreVisible(UUID.randomUUID().toString())
            .bio(UUID.randomUUID().toString())
            .github(UUID.randomUUID().toString())
            .webPersonal(UUID.randomUUID().toString())
            .avatarUrl(UUID.randomUUID().toString());
    }
}
