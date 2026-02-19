package org.example;

public class UserMessage {

    public static void NoInput() {
            System.out.println("Unable to proceed: Input files are not specified.\n" +
                    "Please run this program with path to at least ont input file at the end of parameter list.");
    }

    public static void MissingPrefix() {
        System.out.println("Unable to proceed: Key -p is present, but no prefix was specified\n" +
                "Please run this program with prefix specified after key -p");
    }

    public static void MissingPath() {
        System.out.println("Unable to proceed: Key -o is present, but no path was specified\n" +
                "Please run this program with output path specified after key -o");
    }

    public static void InvalidKey(String key) {
        System.out.printf("Unable to proceed: No such key %s\n", key);
    }

    public static void completion(String path) {

        System.out.printf("""
                Data sorting is complete.
                Resulting files located in %s
                """, path.isEmpty() ? "program's directory" : path);
    }

    public static void short_stats(Stats stat, String path) {
        System.out.printf("""
                Data sorting is complete.
                Integers: %d
                Floats: %d
                Strings: %d
                """, stat.ints, stat.floats, stat.strings);
        System.out.printf("Resulting files located in %s\n", path.isEmpty() ? "program's directory" : path);
    }

    public static void full_stats(Stats stat, String path) {
        System.out.printf("""
                Data sorting is complete.
                ----Lines written----
                Integers: %d
                Floats: %d
                Strings: %d
                
                ----Additional info----
                Biggest number: %f
                Smallest number: %f
                Sum: %f
                Average: %f
                Longest string length: %d
                Shortest string length: %d
                """, stat.ints, stat.floats, stat.strings,
                     stat.num_max, stat.num_min, stat.num_sum, (stat.ints + stat.floats)/stat.num_sum,
                     stat.longest, stat.shortest);
        System.out.printf("Resulting files located in %s\n", path.isEmpty() ? "program's directory" : path);
    }
}
