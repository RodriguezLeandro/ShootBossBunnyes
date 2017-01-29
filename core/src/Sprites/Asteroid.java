package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 26/01/2017.
 */

public class Asteroid extends MisileObject {

    private boolean destroyAsteroid;

    private boolean justOnce;

    public Asteroid(MainGameScreen mainGameScreen, float x, float y) {
        super(mainGameScreen, x, y);

        sprite = new Sprite(new TextureRegion(new Texture("asteroid.png")));

        sprite.setSize(50 / MegamanMainClass.PixelsPerMeters, 50 / MegamanMainClass.PixelsPerMeters);

        defineAsteroid();

        destroyAsteroid = false;

        justOnce = false;

    }

    public void setDestroyAsteroid(){
        destroyAsteroid = true;
    }

    public void defineAsteroid(){
        //La logica de la parte especial del body es la siguiente:
        //La idea es aplicar el impulso en direccion hacia el personaje enemigo.
        //En vez de aplicarlo horizontalmente, como con el fireball.

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

        circleShape.setRadius(25 / MegamanMainClass.PixelsPerMeters);

        circleShape.setPosition(new Vector2(0, 0));

        fixtureDef.shape = circleShape;

        //Por el momento lo dejamos como enemy bit, luego veremos.
        fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float delta){

        if (body.isActive()){

            if (justOnce){

            }else {
                //Aca tenemos que redirigir cada sprite para que ataque a megaman.
                Vector2 positionMegaman = mainGameScreen.getMegaman().body.getPosition();

                //El poder de las matematicas es muy OP.

                body.applyLinearImpulse(new Vector2(positionMegaman.x - body.getPosition().x, positionMegaman.y - body.getPosition().y - 1), body.getWorldCenter(), true);
                justOnce = true;
            }
        }

        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,body.getPosition().y - sprite.getWidth() / 2);

    }
    public float getPositionX(){
        return sprite.getX();
    }

    public boolean destroyAsteroid(){
        return destroyAsteroid;
    }

}
