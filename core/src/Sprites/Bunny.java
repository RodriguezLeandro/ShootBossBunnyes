package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import Screen.MainGameScreen;
import Tools.Enemy;

/**
 * Created by Leandro on 10/01/2017.
 */

public class Bunny extends Enemy {

    private TextureRegion bunnyTexture;

    private Integer vidaDeBunny;

    public boolean destroyBunny;

    private float stateTimer;

    private Animation animationBunny;

    public Bunny(MainGameScreen mainGameScreen, float positionX, float positionY, float width, float height){

        super(mainGameScreen,new Texture("bunnyspritesheet.png"),positionX,positionY,width,height);

        fixture.setUserData(this);

        fixtureSensor.setUserData(this);

        if (width < 1){
            vidaDeBunny = 3;
        }
        else if(width > 1 && width < 2){
            vidaDeBunny = 5;
        }
        else if (width >= 2) {
            vidaDeBunny = 10;
        }

        crearAnimacion();

        stateTimer = 0;

        destroyBunny = false;
    }

    public void crearAnimacion(){
        Array<TextureRegion> textureRegionsFrames = new Array<TextureRegion>();

        for (int i = 0 ; i < 16; i++){
            for (int j = 0; j < 4; j++){
                if (((i == 15)&(j == 2)) || ((i == 15)&(j == 3))){
                    //No hacemos nada en este caso, porque no hay imagen.
                }
                else {
                    textureRegionsFrames.add(new TextureRegion(sprite.getTexture(),j * 100, i * 122 , 100, 122));
                }
            }
        }
        animationBunny = new Animation(0.05f,textureRegionsFrames);

        textureRegionsFrames.clear();
    }

    @Override
    public void onBodyHit() {
        vidaDeBunny--;
        if (vidaDeBunny == 0) {
            destroyBunny = true;
        }
    }

    public void update(float delta){

        makeBunnyFight();

        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);

        sprite.setRegion(getTextureRegion(delta));
    }

    public TextureRegion getTextureRegion(float delta){
        stateTimer += delta;

        return animationBunny.getKeyFrame(stateTimer,true);
    }


    public void makeBunnyFight(){
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

    public void dispose(){
        world.destroyBody(body);
        mainGameScreen.setAddScore(100);
    }

}
