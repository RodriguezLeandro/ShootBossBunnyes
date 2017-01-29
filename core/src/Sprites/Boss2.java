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

import Screen.Level3Screen;
import Screen.Level4Screen;

/**
 * Created by Leandro on 29/01/2017.
 */

public class Boss2 {

    public World world;

    public Body body;

    public State currentState;
    public State previousState;

    private Level3Screen level3Screen;
    private Level4Screen level4Screen;

    private TextureRegion boss2TextureStanding;
    private TextureRegion textureRegionBoss2;

    private Animation attackAnimation;

    private Sprite sprite;

    private ArrayList<BlackFireball> arrayListBlackFireball;

    private Integer arrayListBlackFireballSize;

    private float stateTimer;

    private boolean isRunningRight;
    public boolean boss2InFinalBattle;

    public boolean isBoss2Dead;

    private boolean isBoss2InLevel3;
    public enum State {STANDING,ATTACKING};


    public Boss2(Level3Screen level3Screen){

        sprite = new Sprite(new TextureRegion(new Texture("boss2spritesheet.png")));

        this.level3Screen = level3Screen;

        world = level3Screen.getWorld();

        currentState = State.STANDING;

        previousState = State.STANDING;

        crearAnimaciones();

        defineBoss2();

        stateTimer = 0;

        sprite.setSize(62 / MegamanMainClass.PixelsPerMeters, 105 /MegamanMainClass.PixelsPerMeters);

        arrayListBlackFireball = new ArrayList<BlackFireball>();

        arrayListBlackFireballSize = 0;

        isRunningRight = false;

        boss2InFinalBattle = false;

        isBoss2Dead = false;

        isBoss2InLevel3 = true;
    }
    public Boss2(Level4Screen level4Screen){

        sprite = new Sprite(new TextureRegion(new Texture("boss2spritesheet.png")));

        this.level4Screen = level4Screen;

        world = level4Screen.getWorld();

        currentState = State.STANDING;

        previousState = State.STANDING;

        crearAnimaciones();

        defineBoss2();

        stateTimer = 0;

        sprite.setSize(62 / MegamanMainClass.PixelsPerMeters, 105 /MegamanMainClass.PixelsPerMeters);

        arrayListBlackFireball = new ArrayList<BlackFireball>();

        arrayListBlackFireballSize = 0;

        isRunningRight = false;

        boss2InFinalBattle = false;

        isBoss2Dead = false;

        isBoss2InLevel3 = false;
    }

    public void setState(State state){
        currentState = state;
    }

    public void crearAnimaciones(){

        Array<TextureRegion> textureRegionsFrames = new Array<TextureRegion>();

        for(int i = 0; i < 3; i++){

            textureRegionsFrames.add(new TextureRegion(sprite.getTexture(),62 * i , 105 ,62 ,105));
        }

        attackAnimation = new Animation(0.3f,textureRegionsFrames);

        textureRegionsFrames.clear();

        boss2TextureStanding = new TextureRegion(sprite.getTexture(),62,0,62,105);

    }

    public void draw(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);

       //Tengo que hacer que cada blackfireball se dibuje tambien.
        for (BlackFireball blackFireball : arrayListBlackFireball){
            blackFireball.draw(spriteBatch);
        }

    }

    public void setBlackFireballAttack(){
        if (isRunningRight) {
            if (isBoss2InLevel3) {
                arrayListBlackFireball.add(new BlackFireball(level3Screen, body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y, isRunningRight));
            }else {
                arrayListBlackFireball.add(new BlackFireball(level4Screen, body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y, isRunningRight));
            }
        }else {
            if (isBoss2InLevel3) {
                arrayListBlackFireball.add(new BlackFireball(level3Screen, body.getPosition().x + sprite.getWidth() / 2, body.getPosition().y, isRunningRight));
            }else {
                arrayListBlackFireball.add(new BlackFireball(level4Screen, body.getPosition().x + sprite.getWidth() / 2, body.getPosition().y, isRunningRight));
            }
        }
    }

    public void update(float delta){

        if (boss2InFinalBattle){
            //Tengo que updatear cada blackFireball tambien creo.

            arrayListBlackFireballSize = arrayListBlackFireball.size();

            if (isBoss2InLevel3) {
                for (int i = 0; i < arrayListBlackFireballSize; i++) {
                    //Si el ataque sale de la camara
                    if ((arrayListBlackFireball.get(i).body.getPosition().x < level3Screen.getMainCamera().position.x - 500 / MegamanMainClass.PixelsPerMeters) || (arrayListBlackFireball.get(i).body.getPosition().x > level3Screen.getMainCamera().position.x + 500 / MegamanMainClass.PixelsPerMeters)) {
                        arrayListBlackFireball.get(i).dispose();
                        arrayListBlackFireball.remove(i);
                        arrayListBlackFireballSize = arrayListBlackFireball.size();
                    } else {
                        arrayListBlackFireball.get(i).update(delta);
                    }
                }
            }else {
                for (int i = 0; i < arrayListBlackFireballSize; i++) {
                    //Si el ataque sale de la camara
                    if ((arrayListBlackFireball.get(i).body.getPosition().x < level4Screen.getMainCamera().position.x - 500 / MegamanMainClass.PixelsPerMeters) || (arrayListBlackFireball.get(i).body.getPosition().x > level4Screen.getMainCamera().position.x + 500 / MegamanMainClass.PixelsPerMeters)) {
                        arrayListBlackFireball.get(i).dispose();
                        arrayListBlackFireball.remove(i);
                        arrayListBlackFireballSize = arrayListBlackFireball.size();
                    } else {
                        arrayListBlackFireball.get(i).update(delta);
                    }
                }
            }

            if (!isBoss2Dead){
                makeBoss2Fight(delta);
            }

            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);

            sprite.setRegion(getTextureRegion(delta));

        }
        else {
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
            sprite.setRegion(getTextureRegion(delta));
        }
    }

    public void makeBoss2Fight(float delta){
        Random random = new Random();
        Integer randomNumber = random.nextInt(50);

        if (randomNumber == 1){
            setBlackFireballAttack();
            currentState = State.ATTACKING;
        }

        moverBoss2HaciaMegaman();

        changeTextureRegionOrientation(delta);

    }

    public void moverBoss2HaciaMegaman(){

        if (isBoss2InLevel3) {
            //Si megaman se encuentra a la izquierda del boss 2.
            if (level3Screen.getMegaman().body.getPosition().x < body.getPosition().x) {
                if (isRunningRight) {
                    //Si esta mirando para el lado contrario.
                    isRunningRight = false;
                    body.applyLinearImpulse(new Vector2(-0.05f, 0), body.getWorldCenter(), true);
                }
                //Si esta mirando para el lado correcto.
                else {
                    body.applyLinearImpulse(new Vector2(-0.05f, 0), body.getWorldCenter(), true);
                }
            }
            //Si megaman se encuentra a la derecha del boss2
            else {
                if (!isRunningRight) {
                    isRunningRight = true;
                    body.applyLinearImpulse(new Vector2(0.05f, 0), body.getWorldCenter(), true);
                } else {
                    body.applyLinearImpulse(new Vector2(0.05f, 0), body.getWorldCenter(), true);
                }
            }
        }else {
            //Si megaman se encuentra a la izquierda del boss 2.
            if (level4Screen.getMegaman().body.getPosition().x < body.getPosition().x) {
                if (isRunningRight) {
                    //Si esta mirando para el lado contrario.
                    isRunningRight = false;
                    body.applyLinearImpulse(new Vector2(-0.05f, 0), body.getWorldCenter(), true);
                }
                //Si esta mirando para el lado correcto.
                else {
                    body.applyLinearImpulse(new Vector2(-0.05f, 0), body.getWorldCenter(), true);
                }
            }
            //Si megaman se encuentra a la derecha del boss2
            else {
                if (!isRunningRight) {
                    isRunningRight = true;
                    body.applyLinearImpulse(new Vector2(0.05f, 0), body.getWorldCenter(), true);
                } else {
                    body.applyLinearImpulse(new Vector2(0.05f, 0), body.getWorldCenter(), true);
                }
            }
        }
    }

    public ArrayList<BlackFireball> getArrayListBlackFireball(){
        return arrayListBlackFireball;
    }

    public void changeTextureRegionOrientation(float delta){

        if (!isRunningRight && getTextureRegion(delta).isFlipX()){
                getTextureRegion(delta).flip(true,false);
        }
        else if(isRunningRight && !getTextureRegion(delta).isFlipX()){
                getTextureRegion(delta).flip(true,false);
        }

    }

    public TextureRegion getTextureRegion(float delta){

        currentState = getState();

        switch (currentState){

            case STANDING:
                if (previousState == State.ATTACKING){
                    stateTimer = 0;
                    previousState = State.STANDING;
                    textureRegionBoss2 = boss2TextureStanding;
                }
                else {
                    textureRegionBoss2 = boss2TextureStanding;
                    stateTimer +=delta;
                }
                break;

            case ATTACKING:
                if (previousState == State.STANDING){
                    stateTimer = 0;
                    previousState = State.ATTACKING;
                    textureRegionBoss2 = attackAnimation.getKeyFrame(stateTimer);
                }
                else {
                    stateTimer +=delta;
                    textureRegionBoss2 = attackAnimation.getKeyFrame(stateTimer);
                }
                if (attackAnimation.isAnimationFinished(stateTimer)){
                    currentState = State.STANDING;
                }
                break;
        }

        return textureRegionBoss2;

    }

    public State getState(){
        if (currentState == State.STANDING){
            return State.STANDING;
        }
        else if (currentState == State.ATTACKING){
            return  State.ATTACKING;
        }
        else{
            return  State.STANDING;
        }
    }

    public void defineBoss2(){

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

    public void onBodyHit(){
        if (isBoss2InLevel3) {
            level3Screen.dañarBoss2Personaje();
        }
        else{
            level4Screen.dañarJefes();
        }
    }

    public void dispose(){
        world.destroyBody(body);
    }

}
