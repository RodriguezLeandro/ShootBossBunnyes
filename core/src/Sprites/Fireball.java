package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 06/01/2017.
 */

public class Fireball extends MisileObject{

    public boolean fireToRight;

    public boolean destroyFireball;

    public Fireball(MainGameScreen mainGameScreen, float x, float y, boolean fireToRight, Object player) {

        super(mainGameScreen,x,y);

        this.fireToRight = fireToRight;

        //Si la fireball proviene del personaje principal, creamos el fireball con ciertas propiedades.
        if (player.getClass() == Megaman.class) {
            sprite = new Sprite(mainGameScreen.getTextureAtlasTools().findRegion("fireball"));
            sprite.setBounds(x,y,12 / MegamanMainClass.PixelsPerMeters, 12 / MegamanMainClass.PixelsPerMeters);
            defineMegamanFireball();
            //Si viene del personaje enemigo, creamos otras propiedades.
        }else if(player.getClass() == Zero.class){
            sprite = new Sprite(mainGameScreen.getTextureAtlasTools().findRegion("coldball"));
            sprite.setBounds(x,y,12 / MegamanMainClass.PixelsPerMeters, 12 / MegamanMainClass.PixelsPerMeters);
            defineZeroFireball();
        }else {
            System.out.println(player.getClass());
            //Dejo abierto por si necesitamos utilizar fireball como propiedad de otro enemigo.
            //Hasta podriamos crear un particle con el sprite. Rayo de fuego?.
        }

    }

    public void destroy(){
        destroyFireball = true;
    }

    public void defineMegamanFireball(){

        if (fireToRight) {
            BodyDef bodyDef = new BodyDef();

            //Aqui esta la magia del posicionamiento.
            bodyDef.position.set(vector2Position);

            bodyDef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bodyDef);

            body.setGravityScale(0);

            body.applyLinearImpulse(new Vector2(5f, 0), vector2Position, true);

            FixtureDef fixtureDef = new FixtureDef();

            CircleShape circleShape = new CircleShape();

            circleShape.setRadius(6 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(0, 0));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.ZERO_SENSOR_BIT | MegamanMainClass.ZERO_SENSOR_BIT_2 | MegamanMainClass.ENEMY_BIT;

            body.createFixture(fixtureDef).setUserData(this);

        }else {
            BodyDef bodyDef = new BodyDef();

            //La idea es que el tamaño de sprite.getWidth * 2 sea igual o casi igual a .
            //Megaman.Sprite.getWidht * 2, pero como casi es lo mismo, lo dejamos asi.
            bodyDef.position.set(vector2Position.x - sprite.getWidth() * 2,vector2Position.y);

            bodyDef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bodyDef);

            body.setGravityScale(0);

            body.applyLinearImpulse(new Vector2(-5f, 0), vector2Position, true);

            FixtureDef fixtureDef = new FixtureDef();

            CircleShape circleShape = new CircleShape();

            circleShape.setRadius(6 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(0, 0));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.ZERO_SENSOR_BIT | MegamanMainClass.ZERO_SENSOR_BIT_2 | MegamanMainClass.ENEMY_BIT;

            body.createFixture(fixtureDef).setUserData(this);
        }
    }

    public void defineZeroFireball(){

        if (fireToRight) {
            BodyDef bodyDef = new BodyDef();

            //Aqui esta la magia del posicionamiento.
            bodyDef.position.set(vector2Position);

            bodyDef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bodyDef);

            body.setGravityScale(0);

            body.applyLinearImpulse(new Vector2(5f, 0), vector2Position, true);

            FixtureDef fixtureDef = new FixtureDef();

            CircleShape circleShape = new CircleShape();

            circleShape.setRadius(6 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(0, 0));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

            body.createFixture(fixtureDef).setUserData(this);

        }else {
            BodyDef bodyDef = new BodyDef();

            //La idea es que el tamaño de sprite.getWidth * 2 sea igual o casi igual a .
            //Megaman.Sprite.getWidht * 2, pero como casi es lo mismo, lo dejamos asi.
            bodyDef.position.set(vector2Position.x - sprite.getWidth() * 2,vector2Position.y);

            bodyDef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bodyDef);

            body.setGravityScale(0);

            body.applyLinearImpulse(new Vector2(-5f, 0), vector2Position, true);

            FixtureDef fixtureDef = new FixtureDef();

            CircleShape circleShape = new CircleShape();

            circleShape.setRadius(6 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(0, 0));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

            body.createFixture(fixtureDef).setUserData(this);
        }
    }


}
