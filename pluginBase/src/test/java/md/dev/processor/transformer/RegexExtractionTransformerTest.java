package md.dev.processor.transformer;

import md.dev.options.exception.OptionMismatchTypeException;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.trigger.output.TriggerOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.processor.transformer.RegexExtractionTransformer;

public class RegexExtractionTransformerTest extends BaseTransformerTest {

    @Before
    public void init() {
        super.init();
        transformer = new RegexExtractionTransformer();
    }

    @Test
    public void shouldCallNextInPipelineCorrectly() {
        String all = "aaa42";
        insertInMockedOptions("pattern", "\\d+");
        insertInMockedTriggerOutput(all);

        transformer.loadOptions(options);

        TriggerOutputProcessor triggerOutputProcessor = Mockito.mock(TriggerOutputProcessor.class);
        transformer.setNext(triggerOutputProcessor);

        transformer.process(triggerOutput);
        Mockito.verify(triggerOutputProcessor).process(Mockito.any());
    }

    @Test
    public void shouldParseCorrectly1() {
        String all = "aaa42";
        insertInMockedOptions("pattern", ".*");
        transformer.loadOptions(options);

        insertInMockedTriggerOutput(all);
        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals(all, transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldParseCorrectly2() {
        insertInMockedOptions("pattern", "$b");
        transformer.loadOptions(options);

        insertInMockedTriggerOutput("aaa42");
        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals("", transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldParseCorrectly3() {
        insertInMockedOptions("pattern", "\\d+");
        transformer.loadOptions(options);

        insertInMockedTriggerOutput("aaa42");
        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals("42", transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldParseCorrectly4() {
        insertInMockedOptions("pattern", "\\d+");
        transformer.loadOptions(options);

        insertInMockedTriggerOutput("<span class=\"js-billed-annually\">$6 USD</span>\n" +
                "<span class=\"js-billed-annually\">$12 USD</span>");
        TriggerOutput<String> transformedTriggerOutput = transformer.transform(triggerOutput);
        Assert.assertEquals("6", transformedTriggerOutput.getValue());
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        transformer.loadOptions(options);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("pattern", 10);
        transformer.loadOptions(options);
    }
}
