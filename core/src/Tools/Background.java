package Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 14/01/2017.
 */

public class Background {

    private Sprite sprite;

    //Quizas luego al constructor le mando un texturepacker o algo asi para que se dibuje con mejor rendimiento.
    public Background(){
        sprite = new Sprite(new TextureRegion(new Texture("background.png")));
        sprite.setSize(800 / MegamanMainClass.PixelsPerMeters, 600 / MegamanMainClass.PixelsPerMeters);

    }

    public void draw(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);
    }


}
