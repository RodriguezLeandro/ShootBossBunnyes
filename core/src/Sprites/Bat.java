package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import Screen.MainGameScreen;
import Tools.Enemy;

/**
 * Created by Leandro on 26/01/2017.
 */

public class Bat extends Enemy {

    private Integer vidaDeBat;

    public boolean destroyBat;

    private float stateTimer;

    private Animation animationBat;

    public Bat(MainGameScreen mainGameScreen, float positionX, float positionY, float width, float height) {
        super(mainGameScreen, new Texture("batspritesheet.png"), positionX, positionY, width, height);

        fixture.setUserData(this);

        fixtureSensor.setUserData(this);

        if (width < 1){
            vidaDeBat = 3;
        }
        else if(width > 1 && width < 2){
            vidaDeBat = 5;
        }
        else if (width >= 2) {
            vidaDeBat = 10;
        }

        crearAnimacion();

        stateTimer = 0;

        destroyBat = false;

    }

    public void update(float delta){

        makeBatFight();

        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);

        sprite.setRegion(getTextureRegion(delta));
    }

    public void makeBatFight(){
        if (mainGameScreen.getMegaman().body.getPosition().x < body.getPosition().x){
            //Limito la velocidad porque va demasiado rapido sino.
            if (body.getLinearVelocity().x > -1) {
                body.applyLinearImpulse(new Vector2(-0.2f, 0), body.getWorldCenter(), true);
            }
        }else{
            body.applyLinearImpulse(new Vector2(1f, 0), body.getWorldCenter(), true);
        }

        if(mainGameScreen.getMegaman().body.getPosition().y < body.getPosition().y){
            if (body.getLinearVelocity().y > -0.5f)
                body.applyLinearImpulse(new Vector2(0,-0.05f),body.getWorldCenter(),true);
        }else {
            if (body.getLinearVelocity().y < 0.5f)
                body.applyLinearImpulse(new Vector2(0,0.05f),body.getWorldCenter(),true);
        }
    }

    public TextureRegion getTextureRegion(float delta){
        stateTimer += delta;

        return animationBat.getKeyFrame(stateTimer,true);
    }

    public void crearAnimacion(){

        Array<TextureRegion> textureRegionsFrames = new Array<TextureRegion>();

        for (int i = 0 ; i < 5; i++){
            for (int j = 0; j < 8; j++){
                textureRegionsFrames.add(new TextureRegion(sprite.getTexture(),j * 111, i * 96 , 111, 96));
            }
        }
        animationBat = new Animation(0.05f,textureRegionsFrames);

        textureRegionsFrames.clear();

    }

    public void dispose(){
        world.destroyBody(body);
        mainGameScreen.setAddScore(100);
    }

    @Override
    public void onBodyHit() {
        vidaDeBat --;
        if (vidaDeBat == 0){
            destroyBat = true;
        }
    }
}
