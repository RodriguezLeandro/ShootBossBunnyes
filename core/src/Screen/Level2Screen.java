package Screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.megamangame.MegamanMainClass;

import java.util.ArrayList;

import Sprites.Fireball;
import Sprites.Megaman;
import Tools.WorldCreator;

/**
 * Created by Leandro on 12/01/2017.
 */

public class Level2Screen implements Screen {

    private MegamanMainClass game;

    public LevelSelect levelSelectScreen;

    private OrthographicCamera mainCamera;
    private FitViewport mainViewport;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private World world;

    private WorldCreator worldCreator;

    private Box2DDebugRenderer box2DDebugRenderer;

    private Megaman megaman;

    private ArrayList<Fireball> arrayListMegamanFireball;


    public Level2Screen(MegamanMainClass game, LevelSelect levelSelect){

        this.game = game;

        levelSelectScreen = levelSelect;

        mainCamera = new OrthographicCamera();

        mainViewport = new FitViewport(game.Virtual_Width / game.PixelsPerMeters, game.Virtual_Height / game.PixelsPerMeters, mainCamera);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("tiledmap/tiled2.tmx");

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / game.PixelsPerMeters);

        mainCamera.position.set((game.Virtual_Width / 2) / game.PixelsPerMeters, (game.Virtual_Height / 2) / game.PixelsPerMeters, 0);

        world = new World(new Vector2(0, -10), true);

        box2DDebugRenderer = new Box2DDebugRenderer();




    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
