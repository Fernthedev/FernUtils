package com.github.fernthedev.fernutils;

import com.github.fernthedev.fernutils.console.ArgumentArrayUtils;
import kotlin.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArgumentTestJava {

    private static final String[] args = {"-test", "arg", "-something", "thisisvalue"};

    @DisplayName("Java Argument test 1 for Java 9")
    @Test
    public void testArgumentTest1Java9() {
        AtomicBoolean wasFound = new AtomicBoolean(false);

        ArgumentArrayUtils.parseArguments(
                args,
                Map.entry("w", strings -> {
                    System.out.println("The argument w was found");
                    return Unit.INSTANCE;
                }),
                Map.entry("-test", strings -> {
                    System.out.println("The argument -test was found");
                    wasFound.set(true);
                    return Unit.INSTANCE;
                })
        );

        Assertions.assertTrue(wasFound.get(), "Did not find argument -test in parse");
    }

    @DisplayName("Java Argument test 2 using Builder")
    @Test
    public void testArgumentTest2() {
        AtomicBoolean wasFound = new AtomicBoolean(false);

        ArgumentArrayUtils.parseArguments(args)
                .handle("w", strings -> System.out.println("The argument w was found"))
                .handle("-test", strings -> {
                    System.out.println("The argument -test was found");
                    wasFound.set(true);
                }).apply();

        Assertions.assertTrue(wasFound.get(), "Did not find argument -test in parse");
    }

}
