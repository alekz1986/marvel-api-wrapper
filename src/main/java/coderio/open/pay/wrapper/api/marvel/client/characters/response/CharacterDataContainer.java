package coderio.open.pay.wrapper.api.marvel.client.characters.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CharacterDataContainer {

    private Integer offset;
    private Integer limit;
    private Integer total;
    private Integer count;
    private List<Character> results;

}
