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

import Sprites.Bunny;
import Sprites.Megaman;
import Sprites.Zero;
import Tools.WorldCreator;

/**
 * Created by Leandro on 12/01/2017.
 */

public class Level1Screen extends MainGameScreen{

    private TiledMap tiledMap;

    private OrthogonalTiledMapRenderer mapRenderer;

    //Zero es el enemigo principal.
    private Zero zero;

    private ArrayList<Bunny> arrayListBunny;

    //Para controlar el numero de bunnys;
    private Integer arrayListBunnySize;

    private WorldCreator worldCreator;

    public Level1Screen(MegamanMainClass game, LevelSelect levelSelect) {
        super(game, levelSelect);

        tiledMap = tmxMapLoader.load("tiledmap/tiled1.tmx");

        //OrthogonalTiledMapRenderer se encarga del renderizado del mapa.
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / game.PixelsPerMeters);

        //Creamos al enemigo principal.
        zero = new Zero(this);

        //Porque claramente, nuestro personaje aun vive.
        personajeEstaMuerto = false;

        //No lo queremos dañar ni bien arranca.
        dañarPersonajeProgresivamente = false;

        arrayListBunnySize = 0;

        //Cambiamos la manera de poner la musica, al parecer no hay que utilizar el asset manager con los sonidos.
        //Sera una cuestion de duracion de tiempo del sonido? Probablemente.
        if(MegamanMainClass.assetManager.isLoaded("audio/topman.mp3")) {
            Music music = MegamanMainClass.assetManager.get("audio/topman.mp3", Music.class);
            music.play();
            music.setLooping(true);

        }
        //Ponemos musica de background.
      //  MegamanMainClass.assetManager.get("audio/topman.mp3", Sound.class).play();

        worldCreator = new WorldCreator(this);

        //Creamos a los bunnys.
        arrayListBunny = worldCreator.getBunnys();
    }

    public void update(float delta){

        if (stageInFinalBattle && !finalHudActivated) {
            hud.setGameIsInFinalStage();
            finalHudActivated = true;
            zero.setZeroInFinalBattle(true);
        }

        //Updateamos la posicion del sprite del personaje enemigo y luego la animacion.
        zero.update(delta);

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

        //Si estamos en la batalla final los movimientos de la camara son distintos.
        //Las restricciones son distintas tambien.
        if (!stageInFinalBattle) {
            //Si el personaje quiere cruzar el limite izquierdo de la pantalla no lo dejamos.
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
                //Added on 15 / 01 / 2017, tambien cambiamos la posicion del personaje, para que no quede
                //atrapado entre las paredes.
                megaman.body.setTransform(new Vector2(13000 / MegamanMainClass.PixelsPerMeters,megaman.body.getPosition().y),megaman.body.getAngle());
                mainCamera.position.x = 13200 / MegamanMainClass.PixelsPerMeters;
                stageInFinalBattle = true;
            }

            //Todo lo que ocurre arriba es solo si no estamos en la batalla final.
        } else {
            //El personaje no puede cruzar ni el limite izquierdo ni el limite derecho de la pantalla.
            if (megaman.body.getPosition().x < 12800 / MegamanMainClass.PixelsPerMeters) {
                megaman.body.setLinearVelocity(0, megaman.body.getLinearVelocity().y);
                megaman.body.setTransform(new Vector2(12800 / MegamanMainClass.PixelsPerMeters, megaman.body.getPosition().y), megaman.body.getAngle());
            }

            if (megaman.body.getPosition().x > 13600 / MegamanMainClass.PixelsPerMeters) {
                megaman.body.setLinearVelocity(0, megaman.body.getLinearVelocity().y);
                megaman.body.setTransform(new Vector2(13600 / MegamanMainClass.PixelsPerMeters, megaman.body.getPosition().y), megaman.body.getAngle());
            }
            //Zero tampoco puede salir de la pantalla.
            if (zero.body.getPosition().x < 12800 / MegamanMainClass.PixelsPerMeters) {
                zero.body.setLinearVelocity(0, zero.body.getLinearVelocity().y);
                zero.body.setTransform(new Vector2(12800 / MegamanMainClass.PixelsPerMeters, zero.body.getPosition().y), zero.body.getAngle());
            }

            if (zero.body.getPosition().x > 13600 / MegamanMainClass.PixelsPerMeters) {
                zero.body.setLinearVelocity(0, zero.body.getLinearVelocity().y);
                zero.body.setTransform(new Vector2(13600 / MegamanMainClass.PixelsPerMeters, zero.body.getPosition().y), zero.body.getAngle());
            }
        }

        //Por cada renderizado de la pantalla, la camara se actualiza.
        mainCamera.update();

        //Queremos que solo se dibuje por pantalla lo que se puede ver en camara.
        mapRenderer.setView(mainCamera);
    }

    //Es necesario tener esta funcion aca porque conecta las clases megaman y zero.
    public void setZeroFightState(Integer integer) {
        zero.setZeroFightingStateNumber(integer);
    }

    public void dañarZeroPersonaje() {
        //Dañamos al personaje y verificamos si murio.
        personajeEstaMuerto = hud.dañarZeroPersonaje();
        //Si esta muerto, ponemos estado Dying(deberia ser Dead).
        if (personajeEstaMuerto) {
            zero.setState(Zero.State.DYING);
        }
    }

    public Zero getZero(){
        return zero;
    }

    public void render(float delta){

        super.update(delta);

        update(delta);

        //Limpiamos la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Dibujamos el mapa de tiles map(cargado con tmx).
        mapRenderer.render();

        //Escribo todo lo del batch en esta clase para no tener problemas.

        //Establecemos la proyeccion de la matriz de la camara principal.
        game.batch.setProjectionMatrix(mainCamera.combined);

        //Nuestro batch lo iniciamos.
        game.batch.begin();

        //La clase padre, maingamescreen dibujara a megaman.
        super.draw(game.batch);

        //Le decimos al Sprite que se dibuje.
        zero.draw(game.batch);

        //Dibujamos los bunnys.
        for (Bunny bunny : arrayListBunny) {
            bunny.draw(game.batch);
        }
        game.batch.end();

        //Dibujamos el debuger para los objetos que colisionan.
        box2DDebugRenderer.render(world, mainCamera.combined);

        //Establecemos la projeccion de la matriz de la camara hud.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //Le decimos al hud que se dibuje.
        hud.stage.draw();

        //cuando termine de dibujar todo, preguntamos si el juego termino.
        if (gameOver()) {
            levelSelectScreen.setLastLevelPlayed(1);
            game.setScreen(new GameOverScreen(game, hud.getScore(),levelSelectScreen));
            dispose();
        }

        if (zero.isZeroDeadBy3Seconds()){
            MegamanMainClass.assetManager.get("audio/topman.mp3", Music.class).stop();
            levelSelectScreen.setLastLevelPlayed(1);
            game.setScreen(new Level1WinScreen(game,levelSelectScreen));
            dispose();
        }

    }

    @Override
    public World getWorld() {
        return world;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void dispose(){
        world.dispose();
        tiledMap.dispose();
        mapRenderer.dispose();
        textureAtlasCharac.dispose();
        zero.dispose();
        arrayListBunny.clear();
    }



}
