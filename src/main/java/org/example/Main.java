package org.example;

import java.io.*;
import java.lang.reflect.Type;

public class Main {

    static String output_path = "";
    static String output_prefix = "";

    static boolean append_mode = false;

    static int stats_mode = 0;  // 0 - none, 1 - short, 2 - full

    static String wrong_key = "";

    public static void main(String[] args) {

        int input_index = read_options(args);

        if (check_errors(input_index, args.length)) {
            System.exit(0);
        }

    }

    static int read_options(String[] opts) {
        int result = 0;

        for (int i = 0; i < opts.length; i++) {
            String temp = opts[i];

            switch (temp) {
                case "-a":
                    append_mode = true;
                    break;
                case "-s":
                    stats_mode = 1;
                    break;
                case "-f":
                    stats_mode = 2;
                    break;
                case "-o":
                    if (i+1 < opts.length) {
                        i++;
                        output_path = opts[i];
                        if (!output_path.endsWith("/")) {
                            output_path += "/";
                        }
                        break;
                    }
                    else {
                        return -2;
                    }
                case "-p":
                    if (i+1 < opts.length) {
                        i++;
                        output_prefix = opts[i];
                        break;
                    }
                    else {
                        return -3;
                    }
                default:
                    if (opts[i].startsWith("-")) {
                        wrong_key = opts[i];
                        return -4;
                    }
                    result = i;
                    break;
            }
        }

        return result == 0 ? result : -1;
    }

    static boolean check_errors(int res, int all) {
        if (res < 0) {

            switch (res){
                case -1:
                    UserMessage.NoInput();
                    break;
                case -2:
                    UserMessage.MissingPath();
                    break;
                case -3:
                    UserMessage.MissingPrefix();
                    break;
                case -4:
                    UserMessage.InvalidKey(wrong_key);
                    break;
            }

            return true;
        }

        return false;
    }

    static void file_processing(String[] files) {

        File ints = new File(output_path+output_prefix+"integers.txt");
        File floats = new File(output_path+output_prefix+"floats.txt");
        File strings = new File(output_path+output_prefix+"strings.txt");

        boolean ints_created = false;
        boolean floats_created = false;
        boolean strings_created = false;

        for (String s : files) {

            try(BufferedReader fr = new BufferedReader(new FileReader(s))) {

            String line = fr.readLine();
            if (line == null) {
                continue;
            }

            while (line != null) {
                define(line)
            }


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void write_file(File file, String value, Types type) {

    }

    static Types define(String value) {
        String digits = "(-?)\\d+[eE]?-?\\d*";
        String floats = "(-?)\\d+\\.\\d+([eE]?-?\\d*)?";

        if (value.matches(digits)) {
            return Types.Integer;
        } else if (value.matches(floats)) {
            return Types.Float;
        }
        else {
            return Types.String;
        }
    }

    enum Types {
        Integer,
        Float,
        String
    }

}