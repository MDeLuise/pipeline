package plugin.base.action;

import md.dev.action.AbstractAction;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.plugin.annotation.Action;
import md.dev.trigger.output.TriggerOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Action(
    id = "bScript",
    inputType = String.class,
    description = "Run a local bash script and print in the stdout."
)
public class BashScriptAction extends AbstractAction<String> {
    private String scriptPath;
    private String args;


    @Override
    public void doAction(TriggerOutput<String> triggerOutput) {
        Process process;
        try {
            List<String> cmdList = new ArrayList<>();
            cmdList.add("sh");
            cmdList.add(scriptPath);
            if (args != null) {
                cmdList.add(args);
            }
            cmdList.add(triggerOutput.getValue());
            ProcessBuilder pb = new ProcessBuilder(cmdList);
            process = pb.start();

            process.waitFor();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException | InterruptedException e) {
            log.error("error while running script {}", scriptPath, e);
            throw new PipelineGenericException(e.getMessage());
        }
    }


    @Override
    public void initializeClassOptions() {
    }


    @Override
    protected void loadInstanceOptions(Options options) {
        if (options.has("script")) {
            scriptPath = (String) options.get("script");
        }
        if (options.has("args")) {
            args = (String) options.get("args");
        }
    }


    @Override
    protected Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
            new OptionDescription(
                "script",
                "Path of the script to run.",
                String.class,
                "",
                true
            ),
            new OptionDescription(
                "args",
                "Parameters passed to the script.",
                String.class,
                "",
                false
            )
        ));
    }
}
