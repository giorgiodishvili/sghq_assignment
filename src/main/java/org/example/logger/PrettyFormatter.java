package org.example.logger;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PrettyFormatter extends Formatter {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(final LogRecord record) {
        StringBuilder sb = new StringBuilder();

        sb.append("[").append(record.getLevel()).append("] ");
        sb.append("[").append(Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH).getTime()).append("] ");
        sb.append("[").append(record.getSourceClassName()).append(".");
        sb.append(record.getSourceMethodName()).append("] ");

        sb.append(record.getMessage());
        sb.append(LINE_SEPARATOR);

        if (record.getThrown() != null) {
            sb.append("Exception: ").append(record.getThrown().toString());
            sb.append(LINE_SEPARATOR);
        }

        return sb.toString();
    }
}
