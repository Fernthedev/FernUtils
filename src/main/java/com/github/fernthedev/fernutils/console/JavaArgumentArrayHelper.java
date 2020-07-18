package com.github.fernthedev.fernutils.console;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.*;
import java.util.function.Consumer;

public class JavaArgumentArrayHelper {

    private final String[] args;
    private final Map<String, Consumer<Queue<String>>> finderMap = new HashMap<>();

    public JavaArgumentArrayHelper(String[] args) {
        this.args = args;
    }

    public JavaArgumentArrayHelper handle(String arg, Consumer<Queue<String>> queue) {
        finderMap.put(arg, queue);

        return this;
    }

    public void apply() {
        Map<String, Function1<Queue<String>, Unit>> newMap = new HashMap<>();

        finderMap.forEach((s, queueConsumer) -> newMap.put(s, strings -> {
            queueConsumer.accept(strings);
            return Unit.INSTANCE;
        }));

        ArgumentArrayUtils.parseArguments(args, newMap);

    }

}
