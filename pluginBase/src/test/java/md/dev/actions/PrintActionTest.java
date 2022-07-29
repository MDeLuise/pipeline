package md.dev.actions;

import md.dev.options.exception.OptionMismatchTypeException;
import md.dev.pipeline.TriggerOutputProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.action.PrintAction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;


public class PrintActionTest extends BaseActionUnitTest {


    @Before
    public void init() {
        super.init();
        action = new PrintAction();
    }

    @Test
    public void shouldPrintToStdoutByDefault() {
        PrintStream defaultStdout = System.out;
        String text = "foo";
        insertInMockedOptions("text", text);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        action.loadOptions(options);
        action.doAction(triggerOutput);
        Assert.assertEquals(text + "\n", outContent.toString());
        System.setOut(defaultStdout);
    }

    @Test
    public void shouldWriteToFile() throws IOException {
        String text = "foo";
        insertInMockedOptions("text", text);
        String filePath = "src/test/resources/print-output.txt";
        insertInMockedOptions("file", filePath);
        action.loadOptions(options);
        action.doAction(triggerOutput);
        File file = new File(filePath);
        Assert.assertEquals(text + "\n", Files.readString(file.toPath()));
    }

    @Test
    public void shouldPrintToStdoutPassedObj() {
        PrintStream defaultStdout = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String triggerOutputValue = "42";
        insertInMockedTriggerOutput(triggerOutputValue);
        System.setOut(new PrintStream(outContent));
        action.doAction(triggerOutput);
        System.setOut(defaultStdout);
        Assert.assertEquals(triggerOutputValue + "\n", outContent.toString());
    }

    @Test
    public void shouldWriteToFilePassedObj() throws IOException {
        String triggerOutputValue = "42";
        insertInMockedTriggerOutput(triggerOutputValue);
        String filePath = "src/test/resources/print-output.txt";
        insertInMockedOptions("file", filePath);
        action.loadOptions(options);
        action.doAction(triggerOutput);
        File file = new File(filePath);
        Assert.assertEquals(triggerOutputValue + "\n", Files.readString(file.toPath()));
    }

    @Test
    @Ignore
    //FIXME Method threw 'org.mockito.exceptions.base.MockitoException' exception. Cannot evaluate md.dev.pipeline.TriggerOutputProcessor$MockitoMock$1096645034.toString()
    public void shouldProcessNextInChain() {
        TriggerOutputProcessor triggerOutputProcessor = Mockito.mock(TriggerOutputProcessor.class);
        Mockito.when(triggerOutputProcessor.toString()).thenReturn("AA");
        insertInMockedOptions("text", "foo");
        action.loadOptions(options);
        action.setNext(triggerOutputProcessor);
        action.process(triggerOutput);
        Mockito.verify(
                triggerOutputProcessor,
                Mockito.times(1)
        ).process(triggerOutput);
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        action.loadOptions(options);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("text", 42);
        action.loadOptions(options);
    }

}
