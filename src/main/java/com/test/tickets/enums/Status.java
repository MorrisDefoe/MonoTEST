package com.test.tickets.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Status {
    ERROR,
    PROCESSED,
    COMPLETED;

    private static final List<Status> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Status randomStatus()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
