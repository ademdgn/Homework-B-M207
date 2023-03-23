package edu.estu;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class Options {
    @Argument(required = true, usage = "Name of the file")
    String[] fileNames;

    @Option(name = "-task", usage = "Task name to run", required = true)
    String task;

    @Option(name = "-r", aliases = "--reverse", usage = "Refers to reverse order")
    boolean reverse;

    @Option(name = "-u", aliases = {"--unique", "--uniq"}, usage = "Refers to unique values")
    boolean unique;

    @Option(name = "-topN", usage = "Number of items to be printed")
    int topN =5;

    @Option(name = "-start", usage = "Terms starts with given String.")
    String start;
}