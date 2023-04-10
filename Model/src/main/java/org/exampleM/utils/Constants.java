package org.exampleM.utils;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DAY_START_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
    public static final DateTimeFormatter DAY_END_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:00");
}
