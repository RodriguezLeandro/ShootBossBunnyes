package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 13/01/2017.
 */

public class RayCast extends MisileObject{

    public RayCast(MainGameScreen mainGameScreen, float x, float y, boolean makeBody) {
        super(mainGameScreen, x, y);

        sprite = new Sprite(new TextureRegion(new Texture("fireball.png")));

        sprite.setSize(128 / MegamanMainClass.PixelsPerMeters,128 / MegamanMainClass.PixelsPerMeters);

        //Solo creo cuerpo si eso es lo que me piden.
        if (makeBody) {
            defineRaycast();
        }
    }

    public void update(float delta){

        if (body != null) {
            //Si tengo un body creado, realizo todo "normalmente".
            //En realidad no, el chiste es ese, si cree un body es porque tengo que cambiar el mundo.
            //Se supone que el raycast este es de gravedad, por lo que tengo que cambiar la gravedad del mundo aca.
            body.setActive(true);
            //Es muy interesante la coincidencia? de que hay que ponerle un vector a la gravedad,
            //y que mejor vector que el posicion del cuerpo que creamos. Es perfecto.
            world.setGravity(new Vector2(10f,-30f));
            mainGameScreen.setGravityModifyOn();
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
        }else {
            sprite.setPosition(vector2Position.x - sprite.getWidth() / 2,vector2Position.y - sprite.getHeight() / 2);
        }
    }

    public Vector2 getPosition(){
        return vector2Position;
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

        PolygonShape polygonShape = new PolygonShape();

        Vector2[] vertices = new Vector2[4];

        vertices[0] = new Vector2(0 / MegamanMainClass.PixelsPerMeters,-43f / MegamanMainClass.PixelsPerMeters);
        vertices[1] = new Vector2(0 / MegamanMainClass.PixelsPerMeters,43f / MegamanMainClass.PixelsPerMeters);
        vertices[2] = new Vector2(-43f / MegamanMainClass.PixelsPerMeters ,43f / MegamanMainClass.PixelsPerMeters);
        vertices[3] = new Vector2(-43f / MegamanMainClass.PixelsPerMeters,-43f / MegamanMainClass.PixelsPerMeters);

        polygonShape.set(vertices);

        vertices = null;

        fixtureDef.shape = polygonShape;

        //Por el momento lo dejamos como enemy bit, luego veremos.
        fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public Sprite getSprite(){
        return sprite;
    }

    public void dispose(){
        //Aca tenemos que poner la gravedad en normal tambien.
        world.setGravity(new Vector2(0,-10f));
        mainGameScreen.setGravityModifyOff();
        world.destroyBody(body);
    }

}
