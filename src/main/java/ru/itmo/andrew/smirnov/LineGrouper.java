package ru.itmo.andrew.smirnov;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class LineGrouper {
    private final List<Map<Long, Integer>> columnValToDSU = new ArrayList<>();
    private final List<String> lines = new ArrayList<>();
    private final DSU dsu = new DSU();

    private final LineParser lineParser = new LineParser();

    public void addLine(String line) {
        var parsedLine = lineParser.parseLine(line);
        if (!lineParser.isSensibleString(parsedLine)) {
            return;
        }

        int currDSUIdx = lines.size();
        dsu.makeSet(currDSUIdx);
        lines.add(line);
        for (int i = 0; i < parsedLine.size(); i++) {
            Long columnVal = parsedLine.get(i);
            if (columnValToDSU.size() <= i) {
                columnValToDSU.add(new HashMap<>());
            }

            if (Objects.nonNull(columnVal)) {
                Integer dsuIdx = columnValToDSU.get(i).get(columnVal);
                if (Objects.nonNull(dsuIdx)) {
                    dsu.union(dsuIdx, currDSUIdx);
                }
                columnValToDSU.get(i).put(columnVal, dsu.find(currDSUIdx));
            }
        }
    }

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
            writer.write(String.format("Группа %d\n", i + 1));
            for(int lineIdx: sorted.get(i)) {
                writer.write(lines.get(lineIdx) + "\n");
            }
        }
    }
}
