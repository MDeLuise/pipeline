package plugin.youtube.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rometools.rome.io.FeedException;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.webapi.AbstractWebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;
import plugin.youtube.model.YoutubeVideoList;

import java.io.IOException;


public class YouTubeVideoUploadedWebApi extends AbstractWebApi {
    private String channelId;


    @Override
    public WebApiResponse perform() {
        YoutubeVideoList videos;
        try {
            videos = YouTubeVideoFetcher.getLastVideos(channelId);
        } catch (IOException | FeedException e) {
            log.error("error while fetching last videos", e);
            throw new PipelineGenericException(e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return new WebApiResponse(200, objectMapper.writeValueAsString(videos));
        } catch (JsonProcessingException e) {
            log.error("error while writing feed to string", e);
            throw new PipelineGenericException(e.getMessage());
        }
    }


    @Override
    public void configure(WebApiConfiguration configuration) {
        if (configuration.contains("channelId")) {
            channelId = (String) configuration.get("channelId");
        }
    }

}
