package ru.itmo.andrew.smirnov;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        if (Objects.isNull(args) || args.length == 0) {
            System.err.println("Invalid arguments");
            return;
        }
        final Path inputPath = Path.of(args[0]);
        final LineGrouper lineGrouper = new LineGrouper();

        try (final var reader = Files.newBufferedReader(inputPath)) {
            reader.lines().forEach(lineGrouper::addLine);
        } catch (IOException e) {
            System.err.printf("Error occurred while reading from file %s\n", inputPath);
        }

        final Path outputPath = args.length > 1 && Objects.nonNull(args[1]) ?
                Path.of(args[1]) :
                Path.of("output.txt");
        try (final var writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            lineGrouper.writeToFile(writer);
        } catch (IOException e) {
            System.err.printf("Error occurred while writing to file %s\n", outputPath);
        }
    }
}