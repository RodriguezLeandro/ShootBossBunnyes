package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 13/01/2017.
 */

public class RayCast extends MisileObject{

    public RayCast(MainGameScreen mainGameScreen, float x, float y) {
        super(mainGameScreen, x, y);

        sprite = new Sprite(new TextureRegion(new Texture("fireball.png")));

        sprite.setSize(128 / MegamanMainClass.PixelsPerMeters,128 / MegamanMainClass.PixelsPerMeters);

        defineRaycast();

    }

    public void update(float delta){

        body.setActive(true);

        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,body.getPosition().y - sprite.getHeight() / 2);
    }

    public void defineRaycast(){
        BodyDef bodyDef = new BodyDef();

        //Aqui esta la magia del posicionamiento.
        bodyDef.position.set(vector2Position);

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        body.setGravityScale(0);

        //Como no aplicamos impulso, el runningright flag es innecesario.
        //     body.applyLinearImpulse(new Vector2(5f, 0),body.getWorldCenter(), true);
        body.setActive(false);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape circleShape = new CircleShape();

        circleShape.setRadius(6 / MegamanMainClass.PixelsPerMeters);

        circleShape.setPosition(new Vector2(0, 0));

        fixtureDef.shape = circleShape;

        //Por el momento lo dejamos como enemy bit, luego veremos.
        fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public Sprite getSprite(){
        return sprite;
    }

}
