package ${package}.integration;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import md.dev.action.Action;
import md.dev.factory.exception.NamedElementNotFoundException;
import md.dev.processor.filter.Filter;
import md.dev.processor.transformer.Transformer;
import md.dev.options.Options;
import md.dev.options.OptionsImpl;
import md.dev.options.description.OptionsLoadableEntity;
import md.dev.pipeline.Pipeline;
import md.dev.pipeline.PipelineImpl;
import md.dev.trigger.Trigger;
import md.dev.trigger.output.TriggerOutputImpl;
import org.junit.Assert;
import plugin.base.action.PrintAction;
import plugin.base.processor.transformer.ObjectToStringTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class IntegrationSteps {
    private static String expectedExceptionName;
    private static Pipeline pipeline = new PipelineImpl();
    private static List<Trigger> triggers = new ArrayList<>();
    private static OptionsLoadableEntity lastEntity;
    private static ByteArrayOutputStream byteArrayOutputStreamForTest;
    private static PrintStream defaultStdout;


    @And("I wait {int} second(s)")
    public void iWaitSeconds(int seconds) throws InterruptedException {
        TimeUnit.SECONDS.sleep(seconds);
    }

    @Given("I expect the exception {string}")
    public void iExpectTheException(String exceptionName) {
        expectedExceptionName = exceptionName;
    }

    static public void checkException(String raisedExceptionName) {
        Assert.assertEquals(expectedExceptionName, raisedExceptionName);
    }

    @Given("I have a trigger {string}")
    public void iHaveATrigger(String triggerId) {
        Trigger trigger = createTrigger(triggerId);
        pipeline.addTrigger(trigger);
        triggers.add(trigger);
        lastEntity = trigger;
    }

    @Given("I have an action {string}")
    public void iHaveAnAction(String actionId) {
        Action action = createAction(actionId);
        pipeline.addElement(action);
        lastEntity = action;
    }

    @Given("I have a filter {string}")
    public void iHaveAFilter(String filterId) {
        Filter filter = createFilter(filterId);
        pipeline.addElement(filter);
        lastEntity = filter;
    }

    @Given("I have a transformer {string}")
    public void iHaveATransformer(String transformerId) {
        Transformer transformer = createTransformer(transformerId);
        pipeline.addElement(transformer);
        lastEntity = transformer;
    }

    @Then("I see {string} printed from print action")
    public void iSeeInTheStdout(String expected) {
        Assert.assertEquals(expected, givePrintedByPrintAction());
    }

    @When("I start the pipeline")
    public void iStartThePipeline() {
        defaultStdout = System.out;
        byteArrayOutputStreamForTest = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStreamForTest));
        triggers.forEach(Trigger::startListening);
    }

    @And("I cleanup the environment")
    public void iCleanupTheEnvironment() throws InterruptedException {
        iCleanupThePipeline();
        StateHandler.removeAllStates();
    }

    @And("I cleanup the pipeline")
    public void iCleanupThePipeline() throws InterruptedException {
        triggers.forEach(trigger -> trigger.unlinkPipeline(pipeline));
        iWaitSeconds(1);
        byteArrayOutputStreamForTest = new ByteArrayOutputStream();
        System.setOut(defaultStdout);
        pipeline = new PipelineImpl();
        triggers = new ArrayList<>();
    }

    @Then("I see an ip printed")
    public void iSeeAnIpPrinted() throws IOException {
        String zeroTo255 = "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
        String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        Pattern p = Pattern.compile(regex + "\n");
        Assert.assertTrue(p.matcher(givePrintedByPrintAction()).matches());
    }

    @Given("I load options")
    public void iLoadOptions(DataTable dataTable) {
        Options options = new OptionsImpl();
        for (String key : dataTable.asMap().keySet()) {
            options.add(key, dataTable.asMap().get(key));
        }
        lastEntity.loadOptions(options);
    }

    @Then("I see something printed")
    public void iSeeSomethingPrinted() {
        Assert.assertTrue(givePrintedByPrintAction().length() > 0);
    }

    @Then("I see nothing printed")
    public void iSeeNothingPrinted() {
        Assert.assertEquals(0, givePrintedByPrintAction().length());
    }

    @Then("I see {string} printed")
    public void iSeeStringPrinted(String expected) {
        Assert.assertEquals(expected + "\n", givePrintedByPrintAction());
    }

    private Trigger createTrigger(String triggerId) {
        return switch (triggerId) {
            case "ip" -> new IpPeriodicTrigger(new TriggerOutputImpl<>(), new IpWebApi());
            case "page" -> new WebPageTrigger(new TriggerOutputImpl<>(), new SimpleHttpGetWebApi());
            case "void" -> new OneTimeTrueTrigger(new TriggerOutputImpl<>());
            case "echo" -> new PeriodicEchoTrigger(new TriggerOutputImpl<>());
            default -> throw new NamedElementNotFoundException(triggerId);
        };
    }

    private Action createAction(String actionId) {
        return switch (actionId) {
            case "print" -> new PrintAction();
            default -> throw new NamedElementNotFoundException(actionId);
        };
    }

    private Filter createFilter(String filterId) {
        return switch (filterId) {
            case "eq" -> new EqualsFilter();
            case "gt" -> new GreaterThenFilter();
            case "lt" -> new LessThenFilter();
            default -> throw new NamedElementNotFoundException(filterId);
        };
    }

    private Transformer createTransformer(String transformerId) {
        return switch (transformerId) {
            case "strToInt" -> new StringToIntegerTransformer();
            case "objToStr" -> new ObjectToStringTransformer();
            default -> throw new NamedElementNotFoundException(transformerId);
        };
    }

    private String givePrintedByPrintAction() {
        return byteArrayOutputStreamForTest.toString();
    }

    // ...
}
