package org.example;

public class UserMessage {

    public static void NoInput() {
            System.out.println("Input files are not specified.\n" +
                    "Please run this program with path to at least ont input file at the end of parameter list.");
    }

    public static void MissingPrefix() {
        System.out.println("Key -p is present, but no prefix was specified\n" +
                "Please run this program with prefix specified after key -p");
    }

    public static void MissingPath() {
        System.out.println("Key -o is present, but no path was specified\n" +
                "Please run this program with output path specified after key -o");
    }

    public static void InvalidKey(String key) {
        System.out.printf("No such key %s%n", key);
    }
}
