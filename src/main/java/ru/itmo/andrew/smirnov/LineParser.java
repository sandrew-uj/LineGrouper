package ru.itmo.andrew.smirnov;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Class that encapsulates all working with line parsing
 */
public class LineParser {
    /**
     * Regexp, which should be fit line
     */
    private static final String REGEXP_LINE_FORMAT = "((\"[0-9.]*\")?;)*(\"[0-9.]*\")?";

    /**
     * Lines, which already was in this LineParser
     */
    private final Set<List<Float>> cache = new HashSet<>();

    /**
     * Function for parsing long value of line
     * @param s string like "123" or ""
     * @return long value of string or null if there's no long value
     */
    private Float parseFloat(String s) {
        if (s.isEmpty() || Objects.equals(s, "\"\"")) {
            return null;
        }
        try {
            return Float.parseFloat(s.substring(1, s.length() - 1));
        } catch (NumberFormatException e) {
            System.err.printf("Unsupported string got: %s\n", s);
            return null;
        }
    }

    /**
     * Method for parsing line
     * @param line line
     * @return list of long values
     */
    public List<Float> parseLine(String line) {
        if (!Pattern.matches(REGEXP_LINE_FORMAT, line)) {       // check if line fits regexp
            System.err.printf("Invalid line got %s\n", line);
            return Collections.emptyList();
        }

        var split = line.split(";");
        return Arrays.stream(split)
                .map(this::parseFloat)
                .toList();
    }

    /**
     * Check if this String is "Sensible"
     * It means that it doesn't contain only nulls, not empty, and not in cache yet
     * @param parsedLine list of long values of line
     * @return true if string sensible false otherwise
     */
    public boolean isSensibleString(List<Float> parsedLine) {
        if (cache.contains(parsedLine)) {
            return false;
        }
        for(Float num: parsedLine) {
            if (Objects.nonNull(num)) {
                cache.add(parsedLine);
                return true;
            }
        }
        return false;
    }
}
