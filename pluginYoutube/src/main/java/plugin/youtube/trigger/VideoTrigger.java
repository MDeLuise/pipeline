package plugin.youtube.trigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import md.dev.options.Options;
import md.dev.options.description.OptionDescription;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.plugin.annotation.Trigger;
import md.dev.plugin.exception.RuntimeMissingMandatoryOption;
import md.dev.trigger.AbstractPeriodicWebTrigger;
import md.dev.trigger.output.TriggerOutput;
import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;
import plugin.youtube.model.YoutubeVideo;
import plugin.youtube.model.YoutubeVideoList;
import plugin.youtube.webapi.YouTubeVideoUploadedWebApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Trigger(
        id = "video",
        webApi = YouTubeVideoUploadedWebApi.class,
        outputType = YoutubeVideo.class,
        description = "Send in the pipeline the uploaded videos."
)
public class VideoTrigger extends AbstractPeriodicWebTrigger<YoutubeVideo> {
    private Set<String> channelsIds;
    private Map<String, YoutubeVideo> channelIdToLastVideo;
    private boolean triggerOnlyIfChanged;


    public VideoTrigger(
            TriggerOutput<YoutubeVideo> triggerOutputToUse,
            WebApi webApi) {
        super(triggerOutputToUse, webApi);
    }

    @Override
    public void loadState() {
        String savedMap = getProperty("channelIdToLastVideo");
        if (savedMap != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                channelIdToLastVideo = objectMapper.readValue(
                        savedMap, new TypeReference<>() { }
                );
            } catch (JsonProcessingException e) {
                log.warn("cannot read state", e);
            }
        }
    }

    @Override
    public void saveState() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            setProperty(
                    "channelIdToLastVideo",
                    objectMapper.writeValueAsString(channelIdToLastVideo)
            );
        } catch (JsonProcessingException e) {
            log.warn("cannot save state", e);
        }
    }

    @Override
    protected Collection<OptionDescription> acceptedClassOptions() {
        return new ArrayList<>(Arrays.asList(
                new OptionDescription(
                        "channelId",
                        "List of channels ids to check (separated by comma) (<a " +
                                "href='https://vabs.github" +
                                ".io/youtube-channel-name-converter/'>convert username to id</a>)",
                        String.class,
                        "null",
                        false
                ),
                new OptionDescription(
                        "file",
                        "File's path containing channels ids to check (one per line)",
                        String.class,
                        "null",
                        false
                ),
                new OptionDescription(
                        "onChange",
                        "if true trigger only on new videos.",
                        java.lang.Boolean.class,
                        "true",
                        false
                )
        ));
    }

    @Override
    protected void loadInstanceOptions(Options options) {
        if (options != null && options.has("channelId")) {
            setChannelsIds(options.getString("channelId"));
        }
        if (options != null && options.has("file")) {
            setChannelsIdsFromFile(options.getString("file"));
        }
        if (options != null && options.has("onChange")) {
            triggerOnlyIfChanged = (boolean) options.get("onChange");
        }

        if (options != null && !options.has("channelId") && !options.has("file")) {
            throw new RuntimeMissingMandatoryOption("channelId", "file");
        }
    }


    @Override
    protected void initializeClassOptions() {
        channelsIds = new HashSet<>();
        channelIdToLastVideo = new HashMap<>();
        triggerOnlyIfChanged = true;
    }

    @Override
    protected void listen() {
        ObjectMapper objectMapper = new ObjectMapper();
        for (String channelId : channelsIds) {
            WebApiConfiguration webApiConfiguration = new WebApiConfiguration();
            webApiConfiguration.insert("channelId", channelId);
            webApi.configure(webApiConfiguration);

            WebApiResponse webApiResponse;
            try {
                webApiResponse = webApi.perform();
            } catch (Exception e) {
                log.error("error while fetching new videos", e);
                throw new PipelineGenericException(e.getMessage());
            }
            if (webApiResponse != null && webApiResponse.isOk()) {
                YoutubeVideoList videos;
                try {
                    videos = objectMapper.readValue(
                            webApiResponse.getResponse(), YoutubeVideoList.class);
                } catch (JsonProcessingException e) {
                    log.error("error while read new videos to string", e);
                    throw new PipelineGenericException(e.getMessage());
                }
                if (videos.size() > 0) {
                    triggerPipelineIfNeeded(channelId, videos);
                }
            }
        }
        saveState();
    }

    private void setChannelsIds(String channelsIdsString) {
        channelsIds.addAll(Arrays.asList(channelsIdsString.split(",")));
    }

    private void setChannelsIdsFromFile(String file) {
        try {
            channelsIds.addAll(Files.readAllLines(new File(file).toPath()));
        } catch (IOException e) {
            log.error("cannot read file {}", file);
            e.printStackTrace();
        }
    }

    private void triggerPipelineIfNeeded(String channelId, YoutubeVideoList fetchedVideos) {
        List<YoutubeVideo> videoToSendInThePipeline;
        if (!triggerOnlyIfChanged || !channelIdToLastVideo.containsKey(channelId)) {
            videoToSendInThePipeline = fetchedVideos.stream().toList();
        } else {
            videoToSendInThePipeline =
                    fetchedVideos.stream().
                            filter(
                                ytVideo -> ytVideo.getPublishedDate().
                                        compareTo(channelIdToLastVideo.get(channelId).
                                                getPublishedDate()) > 0).
                            toList();
        }
        YoutubeVideo lastVideo = fetchedVideos.get(fetchedVideos.size() - 1);
        channelIdToLastVideo.put(channelId, lastVideo);

        videoToSendInThePipeline.forEach(ytVideo -> {
            triggerOutput.setValue(ytVideo);
            triggerPipelines();
        });
    }
}
