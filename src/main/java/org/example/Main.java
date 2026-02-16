package org.example;

import java.util.Arrays;
import java.util.Iterator;

public class Main {

    static String output_path = "";
    static String output_prefix = "";

    static boolean append_mode = false;
    static boolean short_stats = false;

    static int stats_mode = 0;  // 0 - none, 1 - short, 2 - full

    static int options_end = 0;

    public static void main(String[] args) {

        int input_index = read_options(args);

        if (check_errors(input_index) == 1) {
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
                        return -4;
                    }
                    result = i;
                    break;
            }
        }

        return result == 0 ? result : -1;
    }

    static int check_errors(int res) {
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
                    UserMessage.InvalidKey();
                    break;
            }

            return 1;
        }
        return 0;
    }

}