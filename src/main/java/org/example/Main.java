package org.example;

import java.io.*;

public class Main {

    static String output_path = "";
    static String output_prefix = "";

    static boolean append_mode = false;

    static int stats_mode = 0;  // 0 - none, 1 - short, 2 - full

    static String wrong_key = "";

    static boolean ints_first_written = false;
    static boolean floats_first_written = false;
    static boolean strings_first_written = false;

    public static void main(String[] args) {

        int input_index = read_options(args);

        if (check_errors(input_index)) {
            System.exit(0);
        }

        Stats stat = new Stats();

        if (stats_mode == 0) {
            for (int i = input_index; i < args.length; i++) {
                file_processing(args[i]);
            }
        }
        else {
            for (int i = input_index; i < args.length; i++) {
                file_processing(args[i], stat);
            }
        }

        switch (stats_mode) {
            case 0 -> UserMessage.completion(output_path);
            case 1 -> UserMessage.short_stats(stat, output_path);
            case 2 -> UserMessage.full_stats(stat, output_path);
        }

    }

    static int read_options(String[] opts) {
        int result = -1;

        for (int i = 0; i < opts.length; i++) {
            String temp = opts[i];

            switch (temp) {
                case "-a":
                    append_mode = true;
                    break;
                case "-s":
                    if (stats_mode != 2) stats_mode = 1;
                    break;
                case "-f":
                    stats_mode = 2;
                    break;
                case "-o":
                    if (i+1 < opts.length && !opts[i+1].startsWith("-")) {
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
                    return result;
            }
        }

        return result;
    }

    static boolean check_errors(int res) {
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

    static void file_processing(String file) {

        File ints = new File(output_path+output_prefix+"integers.txt");
        File floats = new File(output_path+output_prefix+"floats.txt");
        File strings = new File(output_path+output_prefix+"strings.txt");

        try(BufferedReader fr = new BufferedReader(new FileReader(file))) {
            String line = fr.readLine().stripTrailing();
            while (line != null) {

                line = line.replace("\n", "");

                Types type = define(line);
                switch (type) {
                    case Types.Integer:
                        write_file(ints, line, append_mode || ints_first_written);
                        if (!ints_first_written) ints_first_written = true;
                        break;
                    case Types.Float:
                        write_file(floats, line, append_mode || floats_first_written);
                        if (!floats_first_written) floats_first_written = true;
                        break;
                    case Types.String:
                        write_file(strings, line, append_mode || strings_first_written);
                        if (!strings_first_written) strings_first_written = true;
                        break;
                }
                line = fr.readLine();
            }
        } catch (IOException e) {
            UserMessage.raise_error(e.getMessage());
        }

    }

    static void file_processing(String file, Stats stat) {
        File ints = new File(output_path+output_prefix+"integers.txt");
        File floats = new File(output_path+output_prefix+"floats.txt");
        File strings = new File(output_path+output_prefix+"strings.txt");

        try(BufferedReader fr = new BufferedReader(new FileReader(file))) {
            String line = fr.readLine();
            while (line != null) {

                line = line.replace("\n", "");
                Types type = define(line);

                switch (type) {
                    case Types.Integer:
                        write_file(ints, line, append_mode || ints_first_written);
                        stat_gathering(stat, line, Types.Integer);
                        if (!ints_first_written) ints_first_written = true;
                        break;
                    case Types.Float:
                        write_file(floats, line, append_mode || floats_first_written);
                        stat_gathering(stat, line, Types.Float);
                        if (!floats_first_written) floats_first_written = true;
                        break;
                    case Types.String:
                        write_file(strings, line, append_mode || strings_first_written);
                        stat_gathering(stat, line, Types.String);
                        if (!strings_first_written) strings_first_written = true;
                        break;
                }
                line = fr.readLine();
            }
        } catch (IOException e) {
            UserMessage.raise_error(e.getMessage());
        }
    }

    static void stat_gathering(Stats stat, String value, Types type) {
        switch (type) {
            case Types.Integer -> {
                long true_value = Long.decode(value);
                stat.ints++;
                if (stats_mode == 2) {
                    stat.num_sum += true_value;
                    if (true_value > stat.num_max) stat.num_max = true_value;
                    if (true_value < stat.num_min) stat.num_min = true_value;
                }
            }
            case Types.Float -> {
                double true_value = Double.parseDouble(value);
                stat.floats++;
                if (stats_mode == 2) {
                    stat.num_sum += true_value;
                    if (true_value > stat.num_max) stat.num_max = true_value;
                    if (true_value < stat.num_min) stat.num_min = true_value;
                }
            }
            case Types.String -> {
                stat.strings++;
                if (stats_mode == 2) {
                    int len = value.length();
                    if (len > stat.longest) stat.longest = len;
                    if (len < stat.shortest) stat.shortest = len;
                }
            }
        }
    }

    static void write_file(File file, String value, boolean appending) {

        try (FileWriter fr = new FileWriter(file, appending)) {
            fr.write(value.concat("\n"));
            fr.flush();
        } catch (IOException e) {
            UserMessage.raise_error(e.getMessage());
        }

    }

    static Types define(String value) {
        String digits = "-?\\d+";
        String floats = "-?\\d+\\.\\d+([eE]?-?\\d*)?";

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