package plugin.youtube.trigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import plugin.youtube.model.YoutubeVideo;
import plugin.youtube.model.YoutubeVideoList;

import java.util.Date;

public class VideoTriggerTest extends BaseWebTriggerUnitTest {

    @Before
    public void setup() {
        super.init();
        trigger = new VideoTrigger(triggerOutput, webApi);
    }

    @Test
    public void shouldTriggerPipeline() throws JsonProcessingException {
        insertInMockedOptions("channelId", "foo");
        setWebResponseValue(getDummyYoutubeVideoListString());
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(pipeline, Mockito.timeout(threadStartWaitTime)).process(Mockito.any());
    }


    @Test
    public void shouldTriggerPipelineOneTimeOnly() throws JsonProcessingException {
        insertInMockedOptions("repeat", 1);
        insertInMockedOptions("channelId", "foo");
        setWebResponseValue(getDummyYoutubeVideoListString());
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline,
                Mockito.timeout(threadStartWaitTime).times(1)
        ).process(Mockito.any());
    }

    @Test
    public void shouldTriggerPipelineSpecifiedTimes() throws JsonProcessingException {
        setWebResponseValue(getDummyYoutubeVideoListString());
        int specifiedTimes = 5;
        insertInMockedOptions("repeat", specifiedTimes);
        insertInMockedOptions("period", 1);
        insertInMockedOptions("onChange", false);
        insertInMockedOptions("channelId", "foo");
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline,
                Mockito.timeout(10000).times(specifiedTimes)
        ).process(Mockito.any());
    }

    @Test
    public void shouldLoadDelayOption() throws JsonProcessingException {
        setWebResponseValue(getDummyYoutubeVideoListString());
        insertInMockedOptions("delay", 10);
        insertInMockedOptions("channelId", "foo");
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline,
                Mockito.timeout(7000).times(0)
        ).process(Mockito.any());
    }

    @Test
    public void shouldLoadPeriodOption() throws JsonProcessingException {
        setWebResponseValue(getDummyYoutubeVideoListString());
        insertInMockedOptions("period", 10);
        insertInMockedOptions("channelId", "foo");
        trigger.loadOptions(options);
        trigger.linkPipeline(pipeline);
        trigger.startListening();
        Mockito.verify(
                pipeline,
                Mockito.timeout(8000).times(1)
        ).process(Mockito.any());
    }

    @Test
    public void shouldTriggerWithCorrectValue() throws JsonProcessingException {
        setWebResponseValue(getDummyYoutubeVideoListString());
        insertInMockedOptions("channelId", "foo");
        trigger.loadOptions(options);
        trigger.startListening();
        ArgumentCaptor<YoutubeVideo> argument =
                ArgumentCaptor.forClass(YoutubeVideo.class);
        Mockito.verify(triggerOutput, Mockito.timeout(threadStartWaitTime)).setValue(argument.capture());
        Assert.assertEquals("video0", argument.getValue().getVideoId());
    }


    private YoutubeVideoList getDummyYoutubeVideoList() {
        YoutubeVideo youtubeVideo = new YoutubeVideo();
        youtubeVideo.setVideoId("video0");
        youtubeVideo.setChannelId("channel0");
        youtubeVideo.setTitle("title");
        youtubeVideo.setUrl("foo");
        youtubeVideo.setPublishedDate(new Date());

        YoutubeVideoList youtubeVideoList = new YoutubeVideoList();
        youtubeVideoList.add(youtubeVideo);
        return youtubeVideoList;
    }

    private String getDummyYoutubeVideoListString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(getDummyYoutubeVideoList());
    }

}