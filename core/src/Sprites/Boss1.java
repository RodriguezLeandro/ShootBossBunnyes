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

import java.util.ArrayList;
import java.util.Random;

import Screen.Level2Screen;
import Screen.Level4Screen;

/**
 * Created by Leandro on 12/01/2017.
 */

public class Boss1 {


    public World world;

    public Body body;

    public State currentState;
    public State previousState;

    private Level2Screen level2Screen;
    private Level4Screen level4Screen;

    private TextureRegion boss1TextureStanding;
    private TextureRegion textureRegionBoss1;

    private Animation jumpAnimation;
    private Animation transformAnimation;

    private Sprite sprite;

    private ArrayList<HairAttack> arrayListHair;

    private ArrayList<HairAttack> arrayListSpecialHairAttack;

    private float stateTimer;

    private boolean isRunningRight;
    private boolean boss1InFinalBattle;
    public boolean isBoss1Dead;

    private boolean isBoss1InLevel2;


    public enum State {STANDING};

    public Boss1(Level2Screen level2Screen){
        sprite = new Sprite(new TextureRegion(new Texture("boss1transform.png")));

        this.level2Screen = level2Screen;

        world = level2Screen.getWorld();

        currentState = State.STANDING;

        previousState = State.STANDING;

        crearAnimaciones();

        defineBoss1();

        stateTimer = 0;

        sprite.setSize(92.66f / MegamanMainClass.PixelsPerMeters, 120.33f /MegamanMainClass.PixelsPerMeters);

        arrayListHair = new ArrayList<HairAttack>();

        arrayListSpecialHairAttack = new ArrayList<HairAttack>();

        isRunningRight = false;

        boss1InFinalBattle = false;

        isBoss1Dead = false;

        isBoss1InLevel2 = true;
    }

    public Boss1(Level4Screen level4Screen){
        sprite = new Sprite(new TextureRegion(new Texture("boss1transform.png")));

        this.level4Screen = level4Screen;

        world = level4Screen.getWorld();

        currentState = State.STANDING;

        previousState = State.STANDING;

        crearAnimaciones();

        defineBoss1();

        stateTimer = 0;

        sprite.setSize(92.66f / MegamanMainClass.PixelsPerMeters, 120.33f /MegamanMainClass.PixelsPerMeters);

        arrayListHair = new ArrayList<HairAttack>();

        arrayListSpecialHairAttack = new ArrayList<HairAttack>();

        isRunningRight = false;

        boss1InFinalBattle = false;

        isBoss1Dead = false;

        isBoss1InLevel2 = false;
    }

    public void crearAnimaciones(){
        Array<TextureRegion> textureRegionsFrames = new Array<TextureRegion>();

        //Dejo comentado ya que no utilize la animacion de salto.
        /*
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

        */

        for(int i = 0; i < 16; i++){

            for (int j = 0; j < 4; j++){

                if (((i == 15)&&(j == 2))||((i == 15)&(j == 3))){
                    //No hago nada.
                }
                else {
                    textureRegionsFrames.add(new TextureRegion(sprite.getTexture(),j * 354,i * 561,354,561));
                }
            }
        }

        transformAnimation = new Animation(0.5f,textureRegionsFrames);

        textureRegionsFrames.clear();

    }

    public void createHairAttack(){

        Vector2 positionHairAttack = getPositionHairAttack();

        if (isBoss1InLevel2) {
            arrayListHair.add(new HairAttack(level2Screen, positionHairAttack.x, positionHairAttack.y));
        }else {
            arrayListHair.add(new HairAttack(level4Screen, positionHairAttack.x, positionHairAttack.y));
        }
    }

    public void createHairSpecialAttack(){

        for (int i = 0 ; i < 10 ; i ++){

            Vector2 positionHairSpecialAttack = getPositionHairAttack();

            if (isBoss1InLevel2) {
                arrayListSpecialHairAttack.add(new HairAttack(level2Screen, positionHairSpecialAttack.x + (i * 10) / MegamanMainClass.PixelsPerMeters, positionHairSpecialAttack.y));
            }else {
                arrayListSpecialHairAttack.add(new HairAttack(level4Screen, positionHairSpecialAttack.x + (i * 10) / MegamanMainClass.PixelsPerMeters, positionHairSpecialAttack.y));
            }
        }

    }

    public Vector2 getPositionHairAttack(){

        //Aca va a estar la magia.

        Random random = new Random();

        Integer randomNumber = random.nextInt(150);

        return new Vector2(body.getPosition().x + randomNumber / MegamanMainClass.PixelsPerMeters,body.getPosition().y - 50 / MegamanMainClass.PixelsPerMeters + randomNumber / MegamanMainClass.PixelsPerMeters);
    }

    public boolean isRunningRight(){
        return isRunningRight;
    }

    public void draw(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);

        //Tengo que hacer que cada hair se dibuje tambien.
        for (HairAttack hairAttack : arrayListHair){
            hairAttack.draw(spriteBatch);
        }

        for (HairAttack hairAttack : arrayListSpecialHairAttack){
            hairAttack.draw(spriteBatch);
        }
    }

    public void update(float delta){

        if (boss1InFinalBattle){
            //Tengo que updatear cada hair tambien creo.

            for (HairAttack hairAttack : arrayListHair) {
                hairAttack.update(delta);
            }

            for (HairAttack hairAttack : arrayListSpecialHairAttack) {
                hairAttack.update(delta);
            }

            if (!isBoss1Dead){
                makeBoss1Fight();
            }

            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);

            sprite.setRegion(getTextureRegion(delta));

        }
        else {
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
            sprite.setRegion(getTextureRegion(delta));
        }
    }

    public void makeBoss1Fight(){
        Random random = new Random();

        Integer randomNumber = random.nextInt(50);

        Integer randomNumber2 = random.nextInt(500);

        if (randomNumber == 0){
            createHairAttack();
        }
        else if(randomNumber == 1){
            setTeleTransport();
        }
        if (randomNumber2 == 0){
            createHairSpecialAttack();
        }

    }

    public void setTeleTransport(){

        Random random = new Random();
        float distance = random.nextInt(200) / MegamanMainClass.PixelsPerMeters;

        Random random2 = new Random();
        boolean increase = random2.nextBoolean();

        //Si increase es falso, realizamos la teletransportacion a la izquierda.
        if (!increase){
            distance = -distance;
        }

        //Para ver si puede teletransportarse, debemos ver en que nivel se encuentra el boss1.
        if (isBoss1InLevel2) {
            //Si luego de teletransportarse, se encuentra dentro del mapa, lo realizamos.
            if ((body.getPosition().x + distance < 12996 / MegamanMainClass.PixelsPerMeters) || (body.getPosition().x + distance > 13650 / MegamanMainClass.PixelsPerMeters)) {
                //We do nothing.
            } else {
                body.setTransform(new Vector2(body.getPosition().x + distance, body.getPosition().y), body.getAngle());
            }

            //Si luego de teletransportarse, se encuentra dentro del mapa, lo realizamos.
            if ((body.getPosition().y + distance < 180 / MegamanMainClass.PixelsPerMeters) || (body.getPosition().y + distance > 600 / MegamanMainClass.PixelsPerMeters)) {
                //No hacemos nada,
            } else {
                //Si se encuentra dentro del mapa, teletransportamos.
                body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y + distance), body.getAngle());
            }
        }else {
            //En el eje x tenemos que modificar el rango de teletransportacion para el nivel 4.
            if ((body.getPosition().x + distance < 13232 / MegamanMainClass.PixelsPerMeters) || (body.getPosition().x + distance > 13886 / MegamanMainClass.PixelsPerMeters)) {
                //We do nothing.
            } else {
                body.setTransform(new Vector2(body.getPosition().x + distance, body.getPosition().y), body.getAngle());
            }

            //En el eje y no deberiamos tener que modificar nada.
            if ((body.getPosition().y + distance < 180 / MegamanMainClass.PixelsPerMeters) || (body.getPosition().y + distance > 600 / MegamanMainClass.PixelsPerMeters)) {
                //No hacemos nada,
            } else {
                //Si se encuentra dentro del mapa, teletransportamos.
                body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y + distance), body.getAngle());
            }
        }
    }

    public TextureRegion getTextureRegion(float delta){
        stateTimer += delta;

        return transformAnimation.getKeyFrame(stateTimer);
    }

    public void setBoss1InFinalBattle(boolean bool){
        if (bool) {
            boss1InFinalBattle = true;
        }
        else {
            boss1InFinalBattle = false;
        }
    }

    public ArrayList<HairAttack> getArrayListHairAttack(){

        return  arrayListHair;
    }

    public ArrayList<HairAttack> getArrayListSpecialHairAttack(){

        return  arrayListSpecialHairAttack;
    }


    public void onBodyHit(){
        if(isBoss1InLevel2) {
            level2Screen.dañarBoss1Personaje();
        }else{
            level4Screen.dañarJefes();
        }
    }

    public void defineBoss1(){

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(13600 / MegamanMainClass.PixelsPerMeters, 150 / MegamanMainClass.PixelsPerMeters);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape circleShape = new CircleShape();

        circleShape.setRadius(20 / MegamanMainClass.PixelsPerMeters);

        circleShape.setPosition(new Vector2(0, 10 / MegamanMainClass.PixelsPerMeters ));

        fixtureDef.shape = circleShape;

        fixtureDef.filter.categoryBits = MegamanMainClass.BOSS_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT |
                MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT |
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

        fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_SENSOR_BIT;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void dispose(){
        if (isBoss1InLevel2){
            level2Screen.setAddScore(300);
        }
        else {
            level4Screen.setAddScore(300);
        }
        arrayListHair.clear();
        arrayListSpecialHairAttack.clear();
        world.destroyBody(body);
    }

}
