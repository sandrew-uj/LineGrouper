package ru.itmo.andrew.smirnov;

import java.util.*;
import java.util.regex.Pattern;

public class LineParser {
    private static final String REGEXP_LINE_FORMAT = "(\"[0-9]*\";)*\"[0-9]*\"";

    private final Set<List<Long>> hashes = new HashSet<>();

    private Long parseLong(String s) {
        if (s.isEmpty() || Objects.equals(s, "\"\"")) {
            return null;
        }
        try {
            return Long.parseLong(s.substring(1, s.length() - 1));
        } catch (NumberFormatException e) {
            System.err.printf("Unsupported string got: %s\n", s);
            return null;
        }
    }

    public List<Long> parseLine(String line) {
        if (!Pattern.matches(REGEXP_LINE_FORMAT, line)) {
            System.err.printf("Invalid line got %s\n", line);
            return Collections.emptyList();
        }

        var split = line.split(";");
        return Arrays.stream(split)
                .map(this::parseLong)
                .toList();
    }

    public boolean isSensibleString(List<Long> parsedLine) {
        if (hashes.contains(parsedLine)) {
            System.out.println("Dublicate: " + parsedLine + "\n");
            return false;
        }
        for(Long num: parsedLine) {
            if (Objects.nonNull(num)) {
                hashes.add(parsedLine);
                return true;
            }
        }
        return false;
    }
}
