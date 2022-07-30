package plugin.youtube.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import md.dev.state.StateHandler;
import md.dev.trigger.Trigger;
import md.dev.trigger.output.TriggerOutputImpl;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiResponse;
import org.junit.Assert;
import org.mockito.Mockito;
import plugin.base.action.PrintAction;
import plugin.base.processor.transformer.ObjectToStringTransformer;
import plugin.youtube.model.YoutubeVideo;
import plugin.youtube.model.YoutubeVideoList;
import plugin.youtube.trigger.VideoTrigger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IntegrationSteps {
    private static String expectedExceptionName;
    private static Pipeline pipeline = new PipelineImpl();
    private static List<Trigger> triggers = new ArrayList<>();
    private static OptionsLoadableEntity lastEntity;
    private static ByteArrayOutputStream byteArrayOutputStreamForTest;
    private static PrintStream defaultStdout;
    private static WebApi webApi;
    private static WebApiResponse webApiResponse;
    private static String youtubeVideoList;


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

    @When("I start the pipeline")
    public void iStartThePipeline() {
        defaultStdout = System.out;
        byteArrayOutputStreamForTest = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStreamForTest));
        triggers.forEach(Trigger::startListening);
    }

    @And("I cleanup the environment")
    public void iCleanupTheEnvironment() {
        iCleanupThePipeline();
        StateHandler.removeAllStates();
    }

    @And("I cleanup the pipeline")
    public void iCleanupThePipeline() {
        triggers.forEach(trigger -> trigger.unlinkPipeline(pipeline));
        byteArrayOutputStreamForTest = new ByteArrayOutputStream();
        System.setOut(defaultStdout);
        pipeline = new PipelineImpl();
        triggers = new ArrayList<>();
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
        defaultStdout.println(givePrintedByPrintAction());
        Assert.assertEquals(0, givePrintedByPrintAction().length());
    }

    @Then("I see {string} printed")
    public void iSeeStringPrinted(String expected) {
        Assert.assertEquals(expected + "\n", givePrintedByPrintAction());
    }

    @Given("I have the trigger \"video\" with {int} new video(s)")
    public void iHaveTheTriggerWithNewVideos(int num) throws JsonProcessingException {
        youtubeVideoList = getDummyYoutubeVideoList(num);
        webApi = Mockito.mock(WebApi.class);
        webApiResponse = Mockito.mock(WebApiResponse.class);
        Mockito.when(webApi.perform()).thenAnswer(foo -> webApiResponse);
        Mockito.when(webApiResponse.isOk()).thenAnswer(foo -> true);
        Mockito.when(webApiResponse.getResponse()).thenAnswer(foo -> youtubeVideoList);

        VideoTrigger videoTrigger = new VideoTrigger(new TriggerOutputImpl<>(), webApi);
        pipeline.addTrigger(videoTrigger);
        triggers.add(videoTrigger);
        lastEntity = videoTrigger;
    }

    @Given("I have the trigger \"video\" with the same new videos")
    public void iHaveTheTriggerWithTheSameNewVideos() {
        VideoTrigger videoTrigger = new VideoTrigger(new TriggerOutputImpl<>(), webApi);
        pipeline.addTrigger(videoTrigger);
        triggers.add(videoTrigger);
        lastEntity = videoTrigger;
    }


    private Trigger createTrigger(String triggerId) {
        throw new NamedElementNotFoundException(triggerId);
    }

    private Action createAction(String actionId) {
        return switch (actionId) {
            case "print" -> new PrintAction();
            default -> throw new NamedElementNotFoundException(actionId);
        };
    }

    private Filter createFilter(String filterId) {
        throw new NamedElementNotFoundException(filterId);
    }

    private Transformer createTransformer(String transformerId) {
        return switch (transformerId) {
            case "objToStr" -> new ObjectToStringTransformer();
            default -> throw new NamedElementNotFoundException(transformerId);
        };
    }

    private String givePrintedByPrintAction() {
        return byteArrayOutputStreamForTest.toString();
    }

    private String getDummyYoutubeVideoList(int num) throws JsonProcessingException {
        YoutubeVideoList youtubeVideoList = new YoutubeVideoList();
        for (int i = 0; i < num; i++) {
            YoutubeVideo youtubeVideo = new YoutubeVideo();
            youtubeVideo.setVideoId("video" + i);
            youtubeVideo.setChannelId("channel" + i);
            youtubeVideo.setTitle("title" + i);
            youtubeVideo.setUrl("foo" + i);
            youtubeVideo.setPublishedDate(new Date());

            youtubeVideoList.add(youtubeVideo);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(youtubeVideoList);
    }
}
