package com.github.uucomma.neohammers.xplat;

import com.github.uucomma.neohammers.xplat.service.Services;

import java.util.function.Supplier;

public enum EnvironmentType {
    CLIENT,
    SERVER;

    public static void run(EnvironmentType environment, Supplier<Runnable> runnable) {
        if (environment == Services.PLATFORM.getEnvironment()) {
            runnable.get().run();
        }
    }
}
