package plugin.youtube.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Data
public class YoutubeVideoList {
    private List<YoutubeVideo> videos;


    public YoutubeVideoList() {
        videos = new ArrayList<>();
    }


    public int size() {
        return videos.size();
    }


    public boolean add(YoutubeVideo youtubeVideo) {
        return videos.add(youtubeVideo);
    }


    public YoutubeVideo get(int index) {
        return videos.get(index);
    }


    public void sort(Comparator<? super YoutubeVideo> c) {
        videos.sort(c);
    }


    public Stream<YoutubeVideo> stream() {
        return videos.stream();
    }
}
