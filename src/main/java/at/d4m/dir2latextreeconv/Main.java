package at.d4m.dir2latextreeconv;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FilenameFilter;


/**
 * @author Christoph Muck
 */
public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addRequiredOption("d", "dir", true, "input directory");
        options.addOption("e", "exclude", true, "exclude regex");

        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("something", options);
            System.exit(1);
            return;
        }

        String directoryPath = line.getOptionValue('d');

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.err.println("-d path is not a directory!");
            System.exit(2);
            return;
        }

        String excludeRegex = line.getOptionValue('e') != null ? line.getOptionValue('e') : "";

        StringBuilder builder = new StringBuilder();
        getDirtree(directory, builder, 1, (file, name) -> !name.matches(excludeRegex));
        System.out.print("\\dirtree{%");
        System.out.println(builder.toString());
        System.out.print("}");
    }

    private static void getDirtree(File dir, StringBuilder builder, int recursionLevel, FilenameFilter filter) {
        builder.append("\n").append(".").append(recursionLevel).append(" ").append(dir.getName()).append(".");
        for (File file : dir.listFiles(filter)) {
            if (file.isDirectory()) {
                getDirtree(file, builder, recursionLevel + 1, filter);
            } else {
                builder.append("\n").append(".").append(recursionLevel + 1).append(" ").append(file.getName()).append(".");
            }
        }
    }
}
