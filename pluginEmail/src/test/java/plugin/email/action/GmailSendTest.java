package plugin.email.action;

import md.dev.options.exception.MissingMandatoryOptionsException;
import md.dev.options.exception.OptionMismatchTypeException;
import md.dev.pipeline.TriggerOutputProcessor;
import md.dev.webapi.configuration.WebApiConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import plugin.email.action.gmail.GmailSender;

public class GmailSendTest extends BaseActionUnitTest {
    private final String EXISTING_GLOBAL_VAR = "JAVA_HOME";
    private final String DUMMY_TO = "foo@bar.com";
    private final String DUMMY_FROM = "bar@foo.com";
    private final String DUMMY_SUBJECT = "foo-subject";
    private final String DUMMY_TEXT = "foo-text";


    @Before
    public void setUp() {
        super.init();
        action = new GmailSender(webApi);
    }

    @Test
    public void shouldProcessNextInChain() {
        TriggerOutputProcessor triggerOutputProcessor = Mockito.mock(TriggerOutputProcessor.class);
        action.setNext(triggerOutputProcessor);
        setMandatoryDummyOptions();
        action.loadOptions(options);
        action.process(triggerOutput);
        Mockito.verify(triggerOutputProcessor, Mockito.times(1)
        ).process(triggerOutput);
    }

    @Test
    public void shouldAcceptUnknownOption() {
        setMandatoryDummyOptions();
        insertInMockedOptions("foo", "bar");
        action.loadOptions(options);
    }

    @Test(expected = MissingMandatoryOptionsException.class)
    public void shouldThrowExceptionIfMissingOption() {
        TriggerOutputProcessor triggerOutputProcessor = Mockito.mock(TriggerOutputProcessor.class);
        action.setNext(triggerOutputProcessor);
        action.process(triggerOutput);
        Mockito.verify(triggerOutputProcessor, Mockito.times(1)
        ).process(triggerOutput);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("clientIdVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("clientSecretVar", false);
        insertInMockedOptions("to", "foo@bar.com");
        insertInMockedOptions("from", "bar@foo.com");
        insertInMockedOptions("subject", "foo-subject");
        insertInMockedOptions("text", "foo-text");
        action.loadOptions(options);
    }

    @Test
    public void shouldActionCorrectly() {
        setMandatoryDummyOptions();
        action.loadOptions(options);
        action.doAction(triggerOutput);

        ArgumentCaptor<WebApiConfiguration> argument =
                ArgumentCaptor.forClass(WebApiConfiguration.class);
        Mockito.verify(webApi, Mockito.times(1)).
                configure(argument.capture());
        Assert.assertEquals(DUMMY_TEXT, argument.getValue().get("text"));
        Assert.assertEquals(DUMMY_FROM, argument.getValue().get("from"));
        Assert.assertEquals(DUMMY_TO, argument.getValue().get("to"));
        Assert.assertEquals(DUMMY_SUBJECT, argument.getValue().get("subject"));
    }

    @Test
    public void shouldActionMultipleTimes() {
        insertInMockedOptions("clientIdVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("clientSecretVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("to", DUMMY_TO + "," + DUMMY_TO + "," + DUMMY_TO);
        insertInMockedOptions("from", DUMMY_FROM);
        insertInMockedOptions("subject", DUMMY_SUBJECT);
        insertInMockedOptions("text", DUMMY_TEXT);
        action.loadOptions(options);

        action.process(triggerOutput);

        Mockito.verify(webApi, Mockito.times(3)).perform();
    }

    @Test
    public void shouldActionCorrectlyFormattedText() {
        final String FORMATTED_TEXT = "foo %s";
        final String TRIGGER_OUTPUT_VALUE = "hello";
        insertInMockedTriggerOutput(TRIGGER_OUTPUT_VALUE);
        insertInMockedOptions("clientIdVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("clientSecretVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("to", DUMMY_TO);
        insertInMockedOptions("from", DUMMY_FROM);
        insertInMockedOptions("subject", DUMMY_SUBJECT);
        insertInMockedOptions("text", FORMATTED_TEXT);
        action.loadOptions(options);

        action.process(triggerOutput);

        ArgumentCaptor<WebApiConfiguration> argument =
                ArgumentCaptor.forClass(WebApiConfiguration.class);
        Mockito.verify(webApi, Mockito.times(1)).
                configure(argument.capture());
        Assert.assertEquals(
                String.format(FORMATTED_TEXT, TRIGGER_OUTPUT_VALUE),
                argument.getValue().get("text")
        );
    }


    private void setMandatoryDummyOptions() {
        insertInMockedOptions("clientIdVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("clientSecretVar", EXISTING_GLOBAL_VAR);
        insertInMockedOptions("to", DUMMY_TO);
        insertInMockedOptions("from", DUMMY_FROM);
        insertInMockedOptions("subject", DUMMY_SUBJECT);
        insertInMockedOptions("text", DUMMY_TEXT);
    }

}