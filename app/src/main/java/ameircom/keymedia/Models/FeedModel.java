package ameircom.keymedia.Models;

import java.io.Serializable;

/**
 * Created by sotra on 2/4/2017.
 */
public class FeedModel implements Serializable {
    String text  , img  ,   type;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
