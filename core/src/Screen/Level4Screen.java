package Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

import java.util.ArrayList;

import Sprites.Asteroid;
import Sprites.Bat;
import Sprites.BlackFireball;
import Sprites.Boss1;
import Sprites.Boss2;
import Sprites.Bunny;
import Sprites.Fireball;
import Sprites.HairAttack;
import Sprites.Megaman;
import Sprites.Zero;
import Tools.WorldCreator;

/**
 * Created by Leandro on 29/01/2017.
 */

public class Level4Screen extends MainGameScreen {

    private TiledMap tiledMap;

    private OrthogonalTiledMapRenderer mapRenderer;

    private WorldCreator worldCreator;

    private float stateTimer;

    private ArrayList<Asteroid> arrayListAsteroid;

    private Integer arrayListAsteroidSize;

    private Boss2 boss2;

    private Boss1 boss1;

    private ArrayList<Bat> arrayListBat;

    private Integer arrayListBatSize;

    private Music music;

    //Zero es el enemigo principal.
    private Zero zero;

    private ArrayList<Bunny> arrayListBunny;

    //Para controlar el numero de bunnys;
    private Integer arrayListBunnySize;

    private boolean jefesHanMuerto;

    public Level4Screen(MegamanMainClass game, LevelSelect levelSelect) {
        super(game, levelSelect);

        tiledMap = tmxMapLoader.load("tiledmap/tiled4.tmx");

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / game.PixelsPerMeters);

        zero = new Zero(this);

        boss1 = new Boss1(this);

        boss2 = new Boss2(this);

        arrayListBatSize = 0;

        arrayListBunnySize = 0;

        arrayListAsteroidSize = 0;

        if(MegamanMainClass.assetManager.isLoaded("audio/topman.mp3")) {
            music = MegamanMainClass.assetManager.get("audio/topman.mp3", Music.class);
            music.play();
            music.setLooping(true);

        }

        worldCreator = new WorldCreator(this);

        arrayListBunny = worldCreator.getBunnys();

        arrayListBat = worldCreator.getBats();

        arrayListAsteroid = worldCreator.getAsteroids();

        jefesHanMuerto = false;
    }

    public void update(float delta){

        if (stageInFinalBattle && !finalHudActivated) {
            hud.setGameIsInFinalStage();
            finalHudActivated = true;
            zero.setZeroInFinalBattle(true);
            //Notese que tanto para boss1 como para boss2 lo que hacemos es activar el booleano.
            //currentBossInFinalBattle, pero lo hacemos de 2 maneras distintas.(siendo current el correspondiente boss seleccionado.)
            boss1.setBoss1InFinalBattle(true);
            boss2.boss2InFinalBattle = true;
        }


        zero.update(delta);
        boss1.update(delta);
        boss2.update(delta);

        //Si la pelea no se encuentra en la recta final.
        if (!stageInFinalBattle) {
            //Si el personaje sale de los limites izquierdos, no lo dejamos.
            if (megaman.body.getPosition().x < 10 / MegamanMainClass.PixelsPerMeters) {
                megaman.body.setTransform(new Vector2(10 / MegamanMainClass.PixelsPerMeters, megaman.body.getPosition().y), megaman.body.getAngle());
            }

            //Si el personaje se encuentra dentro de los limites del mundo, la camara lo sigue.
            if ((megaman.body.getPosition().x >= 400 / MegamanMainClass.PixelsPerMeters) && (megaman.body.getPosition().x <= 12768 / MegamanMainClass.PixelsPerMeters)) {

                //Hacemos que la camara tenga en el centro a nuestro personaje principal.
                mainCamera.position.x = megaman.body.getPosition().x;

            } else {
                //Logica: Si el cuerpo del personaje sale de los limites x del mundo, la camara queda fija.
                mainCamera.position.x = megaman.body.getPosition().x < (400 / MegamanMainClass.PixelsPerMeters) ? (399 / MegamanMainClass.PixelsPerMeters) : (12768 / MegamanMainClass.PixelsPerMeters);
                // stageInFinalBattle = true;
            }

            //Si megaman sale de los limites derechos de la pantalla, entramos en la batalla final.
            if (megaman.body.getPosition().x > 13158 / MegamanMainClass.PixelsPerMeters){
                megaman.body.setTransform(new Vector2(13250 / MegamanMainClass.PixelsPerMeters,megaman.body.getPosition().y),megaman.body.getAngle());
                mainCamera.position.x = 13568;
                stageInFinalBattle = true;
            }

            //De la misma manera deberiamos comprobar si el personaje sale del limite inferior del mapa...
            //y de esa manera eliminarlo(setToDead).
            if (megaman.body.getPosition().y <= 0) {
                megaman.setState(Megaman.State.DYING);
            }

        }else {
            //Si estamos en la batalla final.
            mainCamera.position.x = 13568 / MegamanMainClass.PixelsPerMeters;

        }

        //Si megaman queda por debajo del suelo, debe morir.
        if (megaman.body.getPosition().y <= 0) {
            megaman.setState(Megaman.State.DYING);
        }

        //Vemos cuantos objetos tiene el arraylist de bunny.
        arrayListBunnySize = arrayListBunny.size();

        //Updateamos los bunnys.
        for (int i = 0; i < arrayListBunnySize; i++) {
            //Si hay que destruirlo, lo destruimos, sino lo updateamos.
            if (arrayListBunny.get(i).destroyBunny) {
                arrayListBunny.get(i).dispose();
                arrayListBunny.remove(i);
                arrayListBunnySize = arrayListBunny.size();
            } else {
                arrayListBunny.get(i).update(delta);
                //Si el enemigo esta cerca, lo despertamos, sino no.
                if (arrayListBunny.get(i).getPositionX() < megaman.body.getPosition().x + 500 / MegamanMainClass.PixelsPerMeters) {
                    arrayListBunny.get(i).body.setActive(true);
                }
            }
        }

        arrayListBatSize = arrayListBat.size();

        for (int i = 0; i < arrayListBatSize; i ++){
            if (arrayListBat.get(i).destroyBat){
                arrayListBat.get(i).dispose();
                arrayListBat.remove(i);
                arrayListBatSize = arrayListBat.size();
            }
            else {
                arrayListBat.get(i).update(delta);
                if (arrayListBat.get(i).getPositionX() < megaman.body.getPosition().x + 500 / MegamanMainClass.PixelsPerMeters) {
                    arrayListBat.get(i).body.setActive(true);
                }
            }
        }

        arrayListAsteroidSize = arrayListAsteroid.size();

        for (int i = 0; i < arrayListAsteroidSize; i ++){

            if (arrayListAsteroid.get(i).body.getPosition().y < 0){
                arrayListAsteroid.get(i).setDestroyAsteroid();
            }

            if (arrayListAsteroid.get(i).destroyAsteroid()) {
                arrayListAsteroid.get(i).dispose();
                arrayListAsteroid.remove(i);
                arrayListAsteroidSize = arrayListAsteroid.size();
            } else {
                arrayListAsteroid.get(i).update(delta);
                //Si el enemigo esta cerca, lo despertamos, sino no.
                if (arrayListAsteroid.get(i).getPositionX() < megaman.body.getPosition().x + 600 / MegamanMainClass.PixelsPerMeters) {
                    arrayListAsteroid.get(i).body.setActive(true);
                }
            }
        }

        //Si logramos vencer a los enemigos...
        if (jefesHanMuerto){
            stateTimer += delta;
            zero.setState(Zero.State.DYING);
            boss1.isBoss1Dead = true;
            boss2.isBoss2Dead = true;
        }

        if (!hud.isZeroBarNull()) {
            //Si logramos vencer a un enemigo, causando daño.
            if (hud.getHpBar() < 180) {
                zero.setZeroInFinalBattle(false);
                zero.body.setTransform(new Vector2(2,8),zero.body.getAngle());
                zero.getArrayListZeroFireball().clear();
            }
            if (hud.getHpBar() < 100){
                boss2.boss2InFinalBattle = false;
                boss2.body.setTransform(new Vector2(2,8),boss2.body.getAngle());
                boss2.getArrayListBlackFireball().clear();
            }

        }

        mainCamera.update();

        //Queremos que solo se dibuje por pantalla lo que se puede ver en camara.
        mapRenderer.setView(mainCamera);

    }

    public void dañarJefes(){
        jefesHanMuerto = hud.dañarZeroPersonaje(10);
    }

    @Override
    public void setGravityModifyOn() {
        megaman.body.setGravityScale(0);

        megaman.body.applyForce(new Vector2(0,-10f),megaman.body.getWorldCenter(),true);

        for(Asteroid asteroid : arrayListAsteroid){
            asteroid.body.setGravityScale(1);
        }

        for(BlackFireball blackFireball : boss2.getArrayListBlackFireball()){
            blackFireball.body.setGravityScale(1);
        }

        for(Bunny bunny : arrayListBunny){
            bunny.body.setGravityScale(1);
        }

        //Hacemos que a los fireballs de zero les afecte la gravedad.
        //A zero no hace falta modificarlo, porque la gravedad ya le afecta.
        for(Fireball fireball : zero.getArrayListZeroFireball()){
            fireball.body.setGravityScale(1);
        }

        for(Bat bat : arrayListBat){
            bat.body.setGravityScale(1);
        }

        for(HairAttack hairAttack : boss1.getArrayListHairAttack()){
            hairAttack.body.setGravityScale(1);
        }

        for(HairAttack hairAttack : boss1.getArrayListSpecialHairAttack()){
            hairAttack.body.setGravityScale(1);
        }
    }

    @Override
    public void setGravityModifyOff() {

        megaman.body.setGravityScale(1);

        //Hacemos que a los conejos no los afecte la gravedad.
        for(Bunny bunny : arrayListBunny){
            bunny.body.setGravityScale(0);
        }

        //Hacemos que a los fireballs de zero no les afecte la gravedad.
        //A zero no hace falta modificarlo, porque la gravedad ya le afecta.
        for(Fireball fireball : zero.getArrayListZeroFireball()){
            fireball.body.setGravityScale(0);
        }

        for(Bat bat : arrayListBat){
            bat.body.setGravityScale(0);
        }

        //Hacemos que a los fireballs de zero no les afecte la gravedad.
        //A zero no hace falta modificarlo, porque la gravedad ya le afecta.
        for(HairAttack hairAttack : boss1.getArrayListHairAttack()){
            hairAttack.body.setGravityScale(0);
        }

        for(HairAttack hairAttack : boss1.getArrayListSpecialHairAttack()){
            hairAttack.body.setGravityScale(0);
        }

        for(Asteroid asteroid : arrayListAsteroid){
            asteroid.body.setGravityScale(0);
        }

        for(BlackFireball blackFireball : boss2.getArrayListBlackFireball()){
            blackFireball.body.setGravityScale(0);
        }

    }

    @Override
    public void setAddScore(Integer score) {
        hud.addScore(score);
    }

    @Override
    public void render(float delta) {
        super.update(delta);

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Dibujamos el mapa de tiles map(cargado con tmx).
        mapRenderer.render();

        game.batch.setProjectionMatrix(mainCamera.combined);

        game.batch.begin();

        super.draw(game.batch);

        zero.draw(game.batch);

        boss1.draw(game.batch);

        boss2.draw(game.batch);

        for (Bunny bunny : arrayListBunny) {
            bunny.draw(game.batch);
        }

        for (Bat bat : arrayListBat){
            bat.draw(game.batch);
        }

        for (Asteroid asteroid : arrayListAsteroid){
            asteroid.draw(game.batch);
        }

        game.batch.end();

      //  box2DDebugRenderer.render(world, mainCamera.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();

        if (gameOver()) {
            levelSelectScreen.setLastLevelPlayed(4);
            game.setScreen(new GameOverScreen(game, hud.getScore(),levelSelectScreen));
            dispose();
        }

        if (stateTimer > 3){
            music.stop();
            levelSelectScreen.setLastLevelPlayed(4);
            levelSelectScreen.setWonLevel(4);
            game.setScreen(new GameWinScreen(game,hud.getScore(),levelSelectScreen));
            dispose();
        }

    }


    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
        boss1.dispose();
        zero.dispose();
        boss2.dispose();
        arrayListAsteroid.clear();
        arrayListBat.clear();
        arrayListBunny.clear();
        arrayListBat.clear();
        world.dispose();
    }
    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
