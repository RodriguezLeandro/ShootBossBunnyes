package Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;


import java.util.ArrayList;

import Sprites.Bat;
import Sprites.Boss1;
import Sprites.HairAttack;
import Sprites.Megaman;
import Tools.WorldCreator;

/**
 * Created by Leandro on 12/01/2017.
 */

public class Level2Screen extends MainGameScreen {

    private TiledMap tiledMap;

    private OrthogonalTiledMapRenderer mapRenderer;

    private WorldCreator worldCreator;

    private Boss1 boss1;

    private ArrayList<Bat> arrayListBat;

    private Integer arrayListBatSize;

    private float stateTimer;

    private Music music;

    public Level2Screen(MegamanMainClass game, LevelSelect levelSelect){
        super(game,levelSelect);

        tiledMap = tmxMapLoader.load("tiledmap/tiled2.tmx");

        //OrthogonalTiledMapRenderer se encarga del renderizado del mapa.
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / game.PixelsPerMeters);

        if(MegamanMainClass.assetManager.isLoaded("audio/topman.mp3")) {
            music = MegamanMainClass.assetManager.get("audio/topman.mp3", Music.class);
            music.play();
            music.setLooping(true);

        }

        boss1 = new Boss1(this);

        arrayListBatSize = 0;

        worldCreator = new WorldCreator(this);

        stateTimer = 0;

        arrayListBat = worldCreator.getBats();

    }

    public void dañarBoss1Personaje(){
        personajeEstaMuerto = hud.dañarZeroPersonaje(10);
    }

    public void update(float delta){

        if (stageInFinalBattle && !finalHudActivated) {
            hud.setGameIsInFinalStage();
            finalHudActivated = true;
            boss1.setBoss1InFinalBattle(true);
        }

        boss1.update(delta);

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

        //Si tocamos B, que el enemigo ataque con el hair(haier?);
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)){
               boss1.createHairSpecialAttack();
        }
        //Si tocamos V, el enemigo ataca tambien con el haier.
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)){
            boss1.createHairAttack();
        }

        //Si la pelea no se encuentra en la recta final.
        if (!stageInFinalBattle) {
            //Si el personaje sale de los limites izquierdos, no lo dejamos.
            if (megaman.body.getPosition().x < 10 / MegamanMainClass.PixelsPerMeters) {
                megaman.body.setTransform(new Vector2(10 / MegamanMainClass.PixelsPerMeters, megaman.body.getPosition().y), megaman.body.getAngle());
            }

            //Si el personaje se encuentra dentro de los limites del mundo, la camara lo sigue.
            if ((megaman.body.getPosition().x >= 400 / MegamanMainClass.PixelsPerMeters) && (megaman.body.getPosition().x <= 12525 / MegamanMainClass.PixelsPerMeters)) {

                //Hacemos que la camara tenga en el centro a nuestro personaje principal.
                mainCamera.position.x = megaman.body.getPosition().x;

            } else {
                //Logica: Si el cuerpo del personaje sale de los limites x del mundo, la camara queda fija.
                mainCamera.position.x = megaman.body.getPosition().x < (400 / MegamanMainClass.PixelsPerMeters) ? (399 / MegamanMainClass.PixelsPerMeters) : (12526 / MegamanMainClass.PixelsPerMeters);
               // stageInFinalBattle = true;
            }

            //Si megaman sale de los limites derechos de la pantalla, entramos en la batalla final.
            if (megaman.body.getPosition().x > 12896 / MegamanMainClass.PixelsPerMeters){
                megaman.body.setTransform(new Vector2(13100 / MegamanMainClass.PixelsPerMeters,megaman.body.getPosition().y),megaman.body.getAngle());
                mainCamera.position.x = 13332;
                stageInFinalBattle = true;
            }

            //De la misma manera deberiamos comprobar si el personaje sale del limite inferior del mapa...
            //y de esa manera eliminarlo(setToDead).
            if (megaman.body.getPosition().y <= 0) {
                megaman.setState(Megaman.State.DYING);
            }

        }else {
            //Si estamos en la batalla final.
            mainCamera.position.x = 13332 / MegamanMainClass.PixelsPerMeters;

        }

        //Si el personaje muere arrancamos el statetimer.
        if (personajeEstaMuerto){
            stateTimer += delta;
            boss1.isBoss1Dead = true;
        }

        mainCamera.update();

        mapRenderer.setView(mainCamera);
    }

    @Override
    public void setGravityModifyOn() {

        megaman.body.setGravityScale(0);

        megaman.body.applyForce(new Vector2(0,-10f),megaman.body.getWorldCenter(),true);

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

    }

    @Override
    public void setAddScore(Integer score) {
        hud.addScore(score);
    }

    @Override
    public void show() {

    }

    public void render(float delta) {
        super.update(delta);

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        game.batch.setProjectionMatrix(mainCamera.combined);

        game.batch.begin();

        super.draw(game.batch);

        boss1.draw(game.batch);

        for (Bat bat : arrayListBat){
            bat.draw(game.batch);
        }
        game.batch.end();

        box2DDebugRenderer.render(world, mainCamera.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();

        if (gameOver()) {
            levelSelectScreen.setLastLevelPlayed(2);
            game.setScreen(new GameOverScreen(game, hud.getScore(),levelSelectScreen));
            dispose();
        }

        if (stateTimer > 3){
            music.stop();
            levelSelectScreen.setLastLevelPlayed(2);
            levelSelectScreen.setWonLevel(2);
            game.setScreen(new Level2WinScreen(game,hud.getScore(),levelSelectScreen));
            dispose();
        }

    }

    public World getWorld(){
        return world;
    }


    @Override
    public void dispose() {
        music.dispose();
        tiledMap.dispose();
        mapRenderer.dispose();
        arrayListBat.clear();
        boss1.dispose();
        world.dispose();
    }

    @Override
    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
