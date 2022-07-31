package plugin.base.action;

import md.dev.action.AbstractAction;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.plugin.annotation.Action;
import md.dev.trigger.output.TriggerOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Action(
    id = "print",
    inputType = String.class,
    description = "Print string on stdout or in a file."
)
public class PrintAction extends AbstractAction<String> {
    private String text;
    private File file;


    @Override
    public void doAction(TriggerOutput<String> triggerOutput) {
        String toFormat = "";
        if (triggerOutput != null && triggerOutput.getValue() != null) {
            toFormat = triggerOutput.getValue();
        }
        PrintStream printStreamToUse = System.out;
        if (file != null) {
            try {
                printStreamToUse = new PrintStream(file);
            } catch (FileNotFoundException e) {
                log.error("Error while writing to file {}", file, e);
            }
        }
        printStreamToUse.printf(text + "%n", toFormat);
        if (file != null) {
            printStreamToUse.close();
        }
    }


    @Override
    public Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "text",
                "If not null print this text, otherwise print the action's input.",
                String.class,
                "null",
                false
            ),
            new OptionDescription(
                "filePath",
                "If not null print to given file.",
                String.class,
                "null",
                false
            )
        ));
    }


    @Override
    public void initializeClassOptions() {
        text = "%s";
    }


    @Override
    public void loadInstanceOptions(Options options) {
        if (options.has("text")) {
            text = (String) options.get("text");
        }
        if (options.has("file")) {
            file = new File((String) options.get("file"));
        }
    }
}
