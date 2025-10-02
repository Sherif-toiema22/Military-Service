package com.sherif.gettingthedifferenceelement;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class FileDiff {
    public static void main(String[] args) throws IOException {
        // Read files explicitly with UTF-8 encoding
        List<String> file1Lines = Files.readAllLines(Paths.get("file1.txt"), StandardCharsets.UTF_8);
        List<String> file2Lines = Files.readAllLines(Paths.get("file2.txt"), StandardCharsets.UTF_8);

        Set<String> set1 = new HashSet<>(file1Lines);
        Set<String> set2 = new HashSet<>(file2Lines);

        Set<String> diff1 = new HashSet<>(set1);
        diff1.removeAll(set2);  // In file1 but not in file2

        Set<String> diff2 = new HashSet<>(set2);
        diff2.removeAll(set1);  // In file2 but not in file1

        // Ensure console supports UTF-8
        PrintStream out = new PrintStream(System.out, true, "UTF-8");

        out.println("Lines in file1 but not in file2:");
        diff1.forEach(out::println);

        out.println("\nLines in file2 but not in file1:");
        diff2.forEach(out::println);
    }
}
