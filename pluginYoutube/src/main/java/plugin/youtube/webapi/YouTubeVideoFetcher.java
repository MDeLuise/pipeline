package plugin.youtube.webapi;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import plugin.youtube.model.YoutubeVideo;
import plugin.youtube.model.YoutubeVideoList;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;

public class YouTubeVideoFetcher {
    private static final String ENDPOINT = "https://www.youtube.com/feeds/videos.xml?channel_id=%s";

    public static YoutubeVideoList getLastVideos(String channelId)
            throws IOException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        String computedEndpoint = String.format(ENDPOINT, channelId);
        SyndFeed feed = input.build(new XmlReader(new URL(computedEndpoint)));
        YoutubeVideoList videos = new YoutubeVideoList();
        for (SyndEntry entry : feed.getEntries()) {
            videos.add(convertEntryToVideo(entry));
        }
        videos.sort(Comparator.comparing(YoutubeVideo::getPublishedDate));
        return videos;
    }

    private static YoutubeVideo convertEntryToVideo(SyndEntry entry) {
        YoutubeVideo youTubeVideo = new YoutubeVideo();
        youTubeVideo.setUrl(entry.getLink());
        youTubeVideo.setTitle(entry.getTitle());
        youTubeVideo.setPublishedDate(entry.getPublishedDate());
        youTubeVideo.setVideoId(extractVideoId(entry.getLink()));
        youTubeVideo.setChannelId(extractChannelId(entry.getAuthors().get(0).getUri()));
        return youTubeVideo;
    }

    private static String extractVideoId(String link) {
        return link.split("=")[1];
    }

    private static String extractChannelId(String link) {
        String[] explodedLink = link.split("/");
        return explodedLink[explodedLink.length - 1];
    }
}
