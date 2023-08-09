package coderio.open.pay.wrapper.api.marvel.client.characters.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CharacterDataWrapper {

    private Integer code;
    private String status;
    private String copyright;
    private String attributionText;
    private String attributionHTML;
    private CharacterDataContainer data;
    private String etag;


}
