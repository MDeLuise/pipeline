package plugin.youtube.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class YoutubeVideo implements Serializable {
    private String url;
    private Date publishedDate;
    private String title;
    private String videoId;
    private String channelId;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof YoutubeVideo)) {
            return false;
        }
        return videoId.equals(((YoutubeVideo) o).videoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId);
    }

    @Override
    public String toString() {
        return "{" +
                "url:'" + url + '\'' +
                ", publishedDate: '" + publishedDate + '\'' +
                ", title:'" + title + '\'' +
                ", videoId:'" + videoId + '\'' +
                ", channelId:'" + channelId + '\'' +
                '}';
    }
}
