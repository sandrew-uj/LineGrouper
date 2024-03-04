package ru.itmo.andrew.smirnov;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Class, which encapsulates all mechanics for grouping lines
 */
public class LineGrouper {
    /**
     * Property, which maps index and value of column to it's DSU number
     */
    private final List<Map<Float, Integer>> columnValToDSU = new ArrayList<>();

    /**
     * List of lines, which added in LineGrouper
     */
    private final List<String> lines = new ArrayList<>();

    /**
     * DSU
     */
    private final DSU dsu = new DSU();

    /**
     * LineParser, which helps parse lines
     */
    private final LineParser lineParser = new LineParser();

    /**
     * Adds new line to LineGrouper
     * @param line new line
     */
    public void addLine(String line) {
        var parsedLine = lineParser.parseLine(line);
        if (!lineParser.isSensibleString(parsedLine)) {     // check if string is sensible to add to LineGrouper
            return;
        }

        int currDSUIdx = lines.size();
        dsu.makeSet(currDSUIdx);        // make set of index in lines
        lines.add(line);
        for (int i = 0; i < parsedLine.size(); i++) {
            Float columnVal = parsedLine.get(i);
            if (columnValToDSU.size() <= i) {
                columnValToDSU.add(new HashMap<>());        // extend the columnValToDSU if needed
            }

            if (Objects.nonNull(columnVal)) {
                Integer dsuIdx = columnValToDSU.get(i).get(columnVal);
                if (Objects.nonNull(dsuIdx)) {
                    dsu.union(dsuIdx, currDSUIdx);          // union new set and existing one, because they have equal values on column
                }
                columnValToDSU.get(i).put(columnVal, dsu.find(currDSUIdx));
            }
        }
    }

    /**
     * Writes result of grouping in file
     * @param writer buffered writer which writes result
     * @throws IOException if error while writing occurs
     */
    public void writeToFile(BufferedWriter writer) throws IOException {
        var groupedIndexes = new HashMap<Integer, List<Integer>>();

        IntStream.range(0, lines.size()).forEach(idx -> {
            int p = dsu.find(idx);
            var list = groupedIndexes.get(p);
            if (Objects.nonNull(list)) {
                list.add(idx);
            } else {
                groupedIndexes.put(p, new ArrayList<>(List.of(idx)));
            }
        });

        writer.write(groupedIndexes.values().stream().filter(list -> list.size() >= 2).count() + "\n");

        var sorted = groupedIndexes.values().stream()
                .sorted(Comparator.<List<Integer>>comparingInt(List::size).reversed())
                .toList();


        for (int i = 0; i < sorted.size(); i++) {
            writer.write(String.format("Group %d\n", i + 1));
            for(int lineIdx: sorted.get(i)) {
                writer.write(lines.get(lineIdx) + "\n");
            }
        }
    }
}
