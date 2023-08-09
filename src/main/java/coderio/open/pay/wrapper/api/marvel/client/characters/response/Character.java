package coderio.open.pay.wrapper.api.marvel.client.characters.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
public class Character {

    private Integer id;
    private String name;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime modified;

    private String resourceURI;
    private List<Url> urls;
    private Image thumbnail;
    private ComicList comics;
    private StoryList stories;
    private EventList events;
    private SeriesList series;

}
