package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.Level1Screen;

/**
 * Created by Leandro on 12/01/2017.
 */

public class Boss1 {


    public World world;

    public Body body;

    public State currentState;
    public State previousState;

    private Level1Screen level1Screen;

    private TextureRegion boss1TextureStanding;
    private TextureRegion textureRegionBoss1;

    private Animation jumpAnimation;

    private Sprite sprite;

    private float stateTimer;

    public enum State {STANDING,JUMPING};

    public Boss1(Level1Screen level1Screen){
        sprite = new Sprite(new TextureRegion(new Texture("boss1jumpspritesheet.png")));

        this.level1Screen = level1Screen;

        world = level1Screen.getWorld();

        currentState = State.STANDING;

        previousState = State.STANDING;

        crearAnimaciones();

        defineBoss1();

        stateTimer = 0;

        sprite.setSize(92.66f / MegamanMainClass.PixelsPerMeters, 120.33f /MegamanMainClass.PixelsPerMeters);

    }

    public void crearAnimaciones(){
        Array<TextureRegion> textureRegionsFrames = new Array<TextureRegion>();

        for (int i = 0; i < 5; i++){

            for(int j = 0 ; j < 4; j++){

                if (((i == 4)&&(j == 1))||((i == 4)&&(j == 2))||((i == 4)&&(j == 3))){
                    //No hago nada en este caso.
                }
                else{
                    textureRegionsFrames.add(new TextureRegion(sprite.getTexture(),j * 278, i * 361, 278, 361 ));
                }
            }
        }

        jumpAnimation = new Animation(0.1f,textureRegionsFrames);

        textureRegionsFrames.clear();

    }

    public void draw(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);
    }

    public void update(float delta){
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);

        sprite.setRegion(getTextureRegion(delta));
    }

    public TextureRegion getTextureRegion(float delta){
        stateTimer += delta;

        return jumpAnimation.getKeyFrame(stateTimer,true);
    }

    public void defineBoss1(){

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(300 / MegamanMainClass.PixelsPerMeters, 200 / MegamanMainClass.PixelsPerMeters);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape circleShape = new CircleShape();

        circleShape.setRadius(20 / MegamanMainClass.PixelsPerMeters);

        circleShape.setPosition(new Vector2(0, 10 / MegamanMainClass.PixelsPerMeters ));

        fixtureDef.shape = circleShape;

        fixtureDef.filter.categoryBits = MegamanMainClass.BOSS_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT |
                MegamanMainClass.FLYINGGROUND_BIT | MegamanMainClass.FLOOR_BIT |
                MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

        body.createFixture(fixtureDef);

        circleShape.setPosition(new Vector2(0,-32 / MegamanMainClass.PixelsPerMeters));

        body.createFixture(fixtureDef);

        PolygonShape polygonShape = new PolygonShape();

        Vector2[] vertices = new Vector2[4];

        vertices[0] = new Vector2(20f / MegamanMainClass.PixelsPerMeters,-53f / MegamanMainClass.PixelsPerMeters);
        vertices[1] = new Vector2(20f / MegamanMainClass.PixelsPerMeters,30f / MegamanMainClass.PixelsPerMeters);
        vertices[2] = new Vector2(-20f / MegamanMainClass.PixelsPerMeters ,30f / MegamanMainClass.PixelsPerMeters);
        vertices[3] = new Vector2(-20f / MegamanMainClass.PixelsPerMeters,-53f / MegamanMainClass.PixelsPerMeters);

        polygonShape.set(vertices);

        vertices = null;

        fixtureDef.shape = polygonShape;

        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = MegamanMainClass.BOSS_BIT;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void dispose(){
        world.destroyBody(body);
    }

}
