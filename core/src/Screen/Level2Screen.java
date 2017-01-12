package Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;


import Sprites.Boss1;
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


    public Level2Screen(MegamanMainClass game, LevelSelect levelSelect){
        super(game,levelSelect);

        tiledMap = tmxMapLoader.load("tiledmap/tiled2.tmx");

        //OrthogonalTiledMapRenderer se encarga del renderizado del mapa.
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / game.PixelsPerMeters);

        //Ponemos musica de background.
        MegamanMainClass.assetManager.get("audio/topman.mp3", Sound.class).play(0.2f);

        boss1 = new Boss1(this);

        worldCreator = new WorldCreator(this);

    }

    public void update(float delta){

        boss1.update(delta);

        if (megaman.body.getPosition().x < 10 / MegamanMainClass.PixelsPerMeters) {
            megaman.body.setTransform(new Vector2(10 / MegamanMainClass.PixelsPerMeters, megaman.body.getPosition().y), megaman.body.getAngle());
        }

        //Si el personaje se encuentra dentro de los limites del mundo, la camara lo sigue.
        if ((megaman.body.getPosition().x >= 400 / MegamanMainClass.PixelsPerMeters) && (megaman.body.getPosition().x <= 12400 / MegamanMainClass.PixelsPerMeters)) {

            //Hacemos que la camara tenga en el centro a nuestro personaje principal.
            mainCamera.position.x = megaman.body.getPosition().x;

        } else {
            //Dejo comentada la primera parte de la manera "estructurada de hacerlo".
          /*  if (megaman.body.getPosition().x < (400 / MegamanMainClass.PixelsPerMeters)){
                mainCamera.position.x = (399 / MegamanMainClass.PixelsPerMeters);
            }
          */
            //Logica: Si el cuerpo del personaje sale de los limites x del mundo, la camara queda fija.
            mainCamera.position.x = megaman.body.getPosition().x < (400 / MegamanMainClass.PixelsPerMeters) ? (399 / MegamanMainClass.PixelsPerMeters) : (12401 / MegamanMainClass.PixelsPerMeters);
        }

        //De la misma manera deberiamos comprobar si el personaje sale del limite inferior del mapa...
        //y de esa manera eliminarlo(setToDead).
        if (megaman.body.getPosition().y <= 0) {
            megaman.setState(Megaman.State.DYING);
        }

        //Si el personaje cruza el limite derecho de la pantalla antes de la batalla final, actualizamos la camara.
        if (megaman.body.getPosition().x > 12800 / MegamanMainClass.PixelsPerMeters) {
            mainCamera.position.x = 13200 / MegamanMainClass.PixelsPerMeters;
            stageInFinalBattle = true;
        }

        mainCamera.update();

        mapRenderer.setView(mainCamera);
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

        megaman.draw(game.batch);

        boss1.draw(game.batch);

        arrayListMegamanSize = arrayListMegamanFireball.size();

        for (int i = 0; i < arrayListMegamanSize; i++) {
            arrayListMegamanFireball.get(i).draw(game.batch);
        }

        game.batch.end();

        box2DDebugRenderer.render(world, mainCamera.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game, hud.getScore(),levelSelectScreen));
            dispose();
        }

    }

    public World getWorld(){
        return world;
    }


    @Override
    public void dispose() {

    }

    @Override
    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
