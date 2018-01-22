package Utilities;

import javafx.scene.image.Image;

public class CardsImageManager {

    private final String CARD_DEFAULT_IMAGE_PATH = "/Resources/cards/card.png";
    private final String CARD_DIR_PATH = "/Resources/cards/";

    public Image GetCardImage(String card)
    {
        if(card == null || card == "") {
            return new Image(CARD_DEFAULT_IMAGE_PATH);
        }
        else {
            return new Image(CARD_DIR_PATH+card+".png");
        }
    }
}
