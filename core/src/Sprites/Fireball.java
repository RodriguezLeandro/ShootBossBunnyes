package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 06/01/2017.
 */

public class Fireball {

    public World world;

    public Body body;

    private MainGameScreen mainGameScreen;

    private Sprite sprite;

    private Vector2 vector2PositionFireball;

    public boolean fireToRight;

    public Fireball(MainGameScreen mainGameScreen, float x, float y, boolean fireToRight) {
        this.mainGameScreen = mainGameScreen;

        this.fireToRight = fireToRight;

        world = mainGameScreen.getWorld();

        sprite = new Sprite(mainGameScreen.getTextureAtlasTools().findRegion("fireball"));

        vector2PositionFireball = new Vector2(x,y);

        sprite.setBounds(x,y,12 / MegamanMainClass.PixelsPerMeters, 12 / MegamanMainClass.PixelsPerMeters);

        defineFireball();

    }

    public void update(float delta){
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,body.getPosition().y - sprite.getWidth() / 2);
    }

    public void defineFireball(){

        if (fireToRight) {
            BodyDef bodyDef = new BodyDef();

            //Aqui esta la magia del posicionamiento.
            bodyDef.position.set(vector2PositionFireball);

            bodyDef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bodyDef);

            body.setGravityScale(0);

            body.applyLinearImpulse(new Vector2(5f, 0), vector2PositionFireball, true);

            FixtureDef fixtureDef = new FixtureDef();

            CircleShape circleShape = new CircleShape();

            circleShape.setRadius(6 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(0, 0));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_SENSOR_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.ZERO_SENSOR_BIT;

            body.createFixture(fixtureDef).setUserData(this);

        }else {
            BodyDef bodyDef = new BodyDef();

            //La idea es que el tamaño de sprite.getWidth * 2 sea igual o casi igual a .
            //Megaman.Sprite.getWidht * 2, pero como casi es lo mismo, lo dejamos asi.
            bodyDef.position.set(vector2PositionFireball.x - sprite.getWidth() * 2,vector2PositionFireball.y);

            bodyDef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bodyDef);

            body.setGravityScale(0);

            body.applyLinearImpulse(new Vector2(-5f, 0), vector2PositionFireball, true);

            FixtureDef fixtureDef = new FixtureDef();

            CircleShape circleShape = new CircleShape();

            circleShape.setRadius(6 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(0, 0));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_SENSOR_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.ZERO_SENSOR_BIT;

            body.createFixture(fixtureDef).setUserData(this);
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    public void dispose(){
        world.destroyBody(body);
    }

}
