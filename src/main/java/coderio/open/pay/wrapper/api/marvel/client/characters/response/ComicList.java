package coderio.open.pay.wrapper.api.marvel.client.characters.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ComicList {

    private Integer available;
    private Integer returned;
    private String collectionURI;
    private List<ComicSummary> items;


}
