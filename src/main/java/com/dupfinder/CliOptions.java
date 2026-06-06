package com.dupfinder;
import org.apache.commons.cli.*;
import java.nio.file.Path;

public final class CliOptions {
    private CliOptions() {
    }

    public static ScanConfig parse(String[] args) throws ParseException {
        Options options = options();
        CommandLine cmd = new DefaultParser().parse(options, args);

        if(cmd.hasOption("help")){
            printHelp(options);
            return null;
        }
        if(!cmd.hasOption("dir")){
            throw new ParseException("Missing option: -dir");
        }
        long minSize= parseMinSize(cmd.getOptionValue("minsize", "0"));
        Path jsonReport= cmd.hasOption("report-json")
                ? Path.of(cmd.getOptionValue("report-json"))
                : null;
        Path htmlReport= cmd.hasOption("report-html")
                ? Path.of(cmd.getOptionValue("report-html"))
                : null;

        return new ScanConfig(Path.of(cmd.getOptionValue("dir")), minSize,
                cmd.hasOption("dry-run"), cmd.hasOption("delete"), jsonReport, htmlReport);
    }
    public static void printHelp(){
        printHelp(options());
    }
    private static Options options() {
        Options options = new Options();
        options.addOption(Option.builder("d").longOpt("dir").hasArg().argName("path").desc("Scan Directory").build());
        options.addOption(Option.builder("m").longOpt("minsize").hasArg().argName("bytes").desc("Skip files smaller than this many bytes").build());
        options.addOption(Option.builder("n").longOpt("dry-run").desc("Show deletions without deleting files").build());
        options.addOption(Option.builder().longOpt("delete").desc("Ask which duplicate files to delete").build());
        options.addOption(Option.builder().longOpt("report-json").hasArg().argName("path").desc("Write JSON report").build());
        options.addOption(Option.builder().longOpt("report-html").hasArg().argName("path").desc("Write HTML report").build());
        options.addOption(Option.builder("h").longOpt("help").desc("Show this help message").build());
        return options;
    }
    private static long parseMinSize(String raw) throws ParseException{
        try{
            long value= Long.parseLong(raw);
            if (value < 0) throw new ParseException("-minsize cannot be negative");
            return value;
        } catch (NumberFormatException e) {
            throw new ParseException("-minsize must be a whole number");
        }
    }
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar target/dupfinder-1.0.0.jar --dir <path> [options]", options);
    }
}