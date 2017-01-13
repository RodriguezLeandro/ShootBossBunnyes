package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 13/01/2017.
 */

public abstract class MisileObject {

    public World world;

    public Body body;

    protected MainGameScreen mainGameScreen;

    protected Sprite sprite;

    protected Vector2 vector2Position;

    public MisileObject(MainGameScreen mainGameScreen, float x, float y){

        this.mainGameScreen = mainGameScreen;

        world = mainGameScreen.getWorld();

        vector2Position = new Vector2(x,y);
    }

    public void update(float delta){
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,body.getPosition().y - sprite.getHeight() / 2);
    }
    public void draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }
    public void dispose(){
        world.destroyBody(body);
    }

}
