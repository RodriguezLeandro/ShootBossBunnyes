package Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 11/01/2017.
 */

public abstract class Enemy {

    public World world;

    protected Sprite sprite;
    public Body body;

    protected Fixture fixture;
    protected Fixture fixtureSensor;
    protected FixtureDef fixtureDef;

    protected MainGameScreen mainGameScreen;

    protected float positionX;
    protected float positionY;


    public Enemy(MainGameScreen mainGameScreen,Texture texture, float positionX, float positionY, float width, float height){

        this.mainGameScreen = mainGameScreen;

        sprite = new Sprite(texture);

        world = mainGameScreen.getWorld();

        this.positionX = positionX;

        this.positionY = positionY;

        sprite.setBounds(positionX,positionY,width,height);

        defineEnemy();

        body.setActive(false);
    }

    //Importante acordarse que cada bunny tiene dos fixtures, uno para el cuerpo y el otro es
    //Un sensor.

    public void defineEnemy(){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(positionX,positionY);

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        body.setGravityScale(0);

        fixtureDef = new FixtureDef();

        PolygonShape polygonShape = new PolygonShape();

        polygonShape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);

        fixtureDef.shape = polygonShape;

        fixtureDef.filter.categoryBits = MegamanMainClass.DEFAULT_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.LAVA_BIT |
                MegamanMainClass.FLOOR_BIT | MegamanMainClass.MEGAMAN_SENSOR_BIT |
                MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT | MegamanMainClass.ENEMY_BIT;

        fixture =  body.createFixture(fixtureDef);

        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = MegamanMainClass.ENEMY_BIT;
        //Que solo el sensor pueda ser golpeado por los disparos de megaman.
        fixtureSensor = body.createFixture(fixtureDef);

    }

    public void update(float delta){
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
    }

    public void draw(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);
    }

    public float getPositionX(){
        return body.getPosition().x;
    }

    public void setCategoryBits(short categoryBits){
        Filter filter = new Filter();

        filter.categoryBits = categoryBits;

        filter.maskBits = fixtureDef.filter.maskBits;

        fixture.setFilterData(filter);
    }

    public abstract void onBodyHit();
}