package ru.itmo.andrew.smirnov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineGrouperTest {

    final List<Set<Set<String>>> testCases = List.of(
            Set.of(
                    Set.of(
                            "\"111\";\"123\";\"222\"",
                            "\"\";\"123\";\"100\"",
                            "\"3000\";\"\";\"100\""
                    )
            ),
            Set.of(
                    Set.of(
                            "\"100\";\"200\";\"300\""
                    ),
                    Set.of(
                            "\"300\";\"100\";\"200\""
                    )
            )
    );
    @Test
    public void testGroup() {
        for(var testCase: testCases) {
            var lineGrouper = new LineGrouper();
            testCase.forEach(testSet -> testSet.forEach(lineGrouper::addLine));

            var path = Path.of("output_test.txt");
            try (final var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                lineGrouper.writeToFile(writer);
            } catch (IOException e) {
                System.err.printf("Error occurred while writing to file %s\n", path);
            }

            try (final var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                var linesList = reader.lines().toList();
                Assertions.assertEquals(Long.toString(testCase.stream().filter(s -> s.size() > 2).count()),
                        linesList.get(0));

                Set<Set<String>> resSet = new HashSet<>();
                Set<String> currSet = new HashSet<>();
                for (String line: linesList.subList(1, linesList.size())) {
                    if (line.startsWith("Group")) {
                        currSet = new HashSet<>();
                        resSet.add(currSet);
                    } else {
                        currSet.add(line);
                    }
                }

                deepEquals(resSet, testCase);
            } catch (IOException e) {
                System.err.printf("Error occurred while writing to file %s\n", path);
            }
        }
    }

    private void deepEquals(Set<Set<String>> a, Set<Set<String>> b) {
        assert a.size() == b.size();
        for (var subSet: a) {
            assert b.contains(subSet);
        }
    }
}
