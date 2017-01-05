package Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.megamangame.MegamanMainClass;

import Scenes.Hud;
import Sprites.Megaman;
import Sprites.Zero;
import Tools.WorldContactListener;
import Tools.WorldCreator;

/**
 * Created by Leandro on 01/01/2017.
 */

public class MainGameScreen implements Screen{

    //Objeto MegamanMainClass
    private MegamanMainClass game;

    //Definimos Atlas para las texturas de los personajes.
    private TextureAtlas textureAtlas;

    //Camara para nuestro juego y viewport para ajustar resoluciones.
    private OrthographicCamera mainCamera;
    private FitViewport mainViewport;

    //Para cargar archivo tmx.
    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    //World es el mundo de nuestro juego.
    private World world;

    //Lo utilizamos para ver las lineas que delimitan los objetos pertenecientes a nuestro juego.
    private Box2DDebugRenderer box2DDebugRenderer;

    //Megaman es el personaje principal.
    private Megaman megaman;

    //Zero es el enemigo principal.
    private Zero zero;

    //Creamos el hud de nuestro juego(pantalla principal).
    public Hud hud;

    //Para verificar si el personaje murio.
    private boolean personajeEstaMuerto;

    //Booleano para sacarle vida al personaje progresivamente.
    private boolean dañarPersonajeProgresivamente;

    //Para ver el daño que hay que hacerle al personaje.
    private float healthDamage;

    public MainGameScreen(MegamanMainClass game) {

        //Le asignamos el juego a nuestro MegamanMainClass.
        this.game = game;

        textureAtlas = new TextureAtlas("MegamanAndEnemies.pack");

        //Creamos una camara para que el usuario vea el renderizado.
        mainCamera = new OrthographicCamera();

        //Con el FitViewport nos aseguramos que la app cuente con multiples resoluciones.
        mainViewport = new FitViewport(game.Virtual_Width / game.PixelsPerMeters, game.Virtual_Height / game.PixelsPerMeters, mainCamera);

        //Cargamos el archivo tmx de tiles map.
        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("tiled1.tmx");

        //OrthogonalTiledMapRenderer se encarga del renderizado del mapa.
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / game.PixelsPerMeters);

        //Ponemos como posicion inicial de la camara el medio de la pantalla virtual(inicial).
        mainCamera.position.set((game.Virtual_Width / 2) / game.PixelsPerMeters, (game.Virtual_Height / 2) / game.PixelsPerMeters, 0);

        //Creamos el mundo de nuestro juego.
        world = new World(new Vector2(0, -10), true);

        //Creamos nuestro debugrenderer.
        box2DDebugRenderer = new Box2DDebugRenderer();

        //WorldCreator creara un objeto fisico de box2d por cada objeto de tiled map.
        new WorldCreator(this);

        //Creamos a nuestro personaje principal.
        megaman = new Megaman(this);

        //Creamos al enemigo principal.
        zero = new Zero(this);

        //Creamos el Listener de nuestro mundo.
        world.setContactListener(new WorldContactListener());

      /*  //Creamos la musica, lo dejo comentado como un ejemplo para crear backgroundmusic.
            music = MegamanMainClass.assetManager.get("audio/background.mp3",Music.class);
            music.setLooping(true);
            music.play();
      */

        //Creamos el Hud de nuestro juego.
        hud = new Hud(game.batch);

        //Porque claramente, nuestro personaje aun vive.
        personajeEstaMuerto = false;

        //No lo queremos dañar ni bien arranca.
        dañarPersonajeProgresivamente = false;
    }

    public  TextureAtlas getTextureAtlas(){
        return textureAtlas;
    }
    @Override
    public void show() {

    }

    public void update(float delta){

        //Si presionamos W, el personaje salta.
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            megaman.body.applyLinearImpulse(new Vector2(0,6f),megaman.body.getWorldCenter(),true);
        }
        //Si presionamos D, el personaje se mueve a la derecha.
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            megaman.body.applyLinearImpulse(new Vector2(0.2f,0),megaman.body.getWorldCenter(),true);
        }
        //Si presionamos A, el personaje se mueve a la izquierda.
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            megaman.body.applyLinearImpulse(new Vector2(-0.2f,0),megaman.body.getWorldCenter(),true);
        }
        //Si presionamos 8, el personaje enemigo salta.
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)){
            zero.body.applyLinearImpulse(new Vector2(0,6f),zero.body.getWorldCenter(),true);
        }
        //Si presionamos 4, el personaje enemigo se mueve a la izquierda.
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)){
            zero.body.applyLinearImpulse(new Vector2(-0.2f,0),zero.body.getWorldCenter(),true);
        }
        //Si presionamos 6, el personaje enemigo se mueve a la derecha.
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)){
            zero.body.applyLinearImpulse(new Vector2(0.2f,0),zero.body.getWorldCenter(),true);
        }
        //Si presionamos 7, el personaje enemigo pega.
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7)){
            zero.setState(Zero.State.HITTING);
        }
        //Si presionamos 9, el personaje enemigo pega.
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9)){
            zero.setState(Zero.State.CROUCHING);
        }
        //Si presionamos 3, el personaje enemigo pega.
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)){
            zero.setState(Zero.State.GETTINGHIT);
        }
        //Si presionamos 1, el personaje enemigo pega.
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)){
            zero.setState(Zero.State.DYING);
        }
        //Si presionamos Flechita UP, el personaje muere.
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            megaman.setState(Megaman.State.DYING);
        }
        //Si presionamos Left, el personaje se agacha.
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            megaman.setState(Megaman.State.CROUCHING);
        }
        //Si presionamos Right, el personaje pega.
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            megaman.setState(Megaman.State.HITTING);
        }
        //Si presionamos Down, el personaje es lastimado.
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            megaman.setState(Megaman.State.GETTINGHIT);
        }
        //Si presionamos P, el personaje pierde vida.
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            dañarPersonaje(10);
        }
        //Si presionamos O, el personaje pierde mana.
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)){
            hud.gastarMana(30);
        }
        //Si presionamos L, el personaje gana vida.
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
            hud.curarPersonaje(30);
        }
        //Si presionamos K, el personaje gana mana.
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)){
            hud.recuperarMana(30);
        }

        //Preguntamos si hay que dañarlo progresivamente al personaje.
        if (dañarPersonajeProgresivamente){
            dañarPersonaje(getHealthDamage());
        }

        //Le decimos al mundo que avanze, que efectue cambios en las fisicas.
        world.step(1/60f,6,2);

        //Updateamos la posicion del sprite de nuestro personaje y luego la animacion.
        megaman.update(delta);

        //Updateamos la posicion del sprite del personaje enemigo y luego la animacion.
        zero.update(delta);

        //Si el personaje se encuentra dentro de los limites del mundo, la camara lo sigue.
        if ((megaman.body.getPosition().x >= 400 / MegamanMainClass.PixelsPerMeters )&&(megaman.body.getPosition().x <= 6000 / MegamanMainClass.PixelsPerMeters)) {
            //Hacemos que la camara tenga en el centro a nuestro personaje principal.
            mainCamera.position.x = megaman.body.getPosition().x;
        }
        else {
            //Dejo comentada la primera parte de la manera "estructurada de hacerlo".
          /*  if (megaman.body.getPosition().x < (400 / MegamanMainClass.PixelsPerMeters)){
                mainCamera.position.x = (399 / MegamanMainClass.PixelsPerMeters);
            }
          */
            //Logica: Si el cuerpo del personaje sale de los limites x del mundo, la camara queda fija.
            mainCamera.position.x = megaman.body.getPosition().x < (400 / MegamanMainClass.PixelsPerMeters) ? (399 / MegamanMainClass.PixelsPerMeters) : (6001 / MegamanMainClass.PixelsPerMeters);
        }

        //De la misma manera deberiamos comprobar si el personaje sale del limite inferior del mapa...
        //y de esa manera eliminarlo(setToDead).
        if (megaman.body.getPosition().y <= 0){
            megaman.setState(Megaman.State.DYING);
        }

        //Por cada renderizado de la pantalla, la camara se actualiza.
        mainCamera.update();

        //Queremos que solo se dibuje por pantalla lo que se puede ver en camara.
        mapRenderer.setView(mainCamera);
    }

    public float getHealthDamage(){
        return healthDamage;
    }

    public void setHealthDamage(float healthDamage){
        this.healthDamage = healthDamage;
    }

    public void dañarPersonaje(float health){

        //Dañamos al personaje y verificamos si murio.
        personajeEstaMuerto = hud.dañarPersonaje(health);
        //Si esta muerto, ponemos estado Dying(deberia ser Dead).
        if(personajeEstaMuerto){
            megaman.setState(Megaman.State.DYING);
        }
    }

    public void dañarPersonajeProgresivamente(float health){
        setHealthDamage(health);
        dañarPersonajeProgresivamente = true;
    }

    public void dejarDañoPersonajeProgresivo(boolean bool){
        dañarPersonajeProgresivamente = false;
    }

    @Override
    public void render(float delta) {

        //Realizamos los cambios que se requieran en la logica del juego.
        update(delta);

        //Limpiamos la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Dibujamos el mapa de tiles map(cargado con tmx).
        mapRenderer.render();

        //Dibujamos el debuger para los objetos que colisionan.
        box2DDebugRenderer.render(world,mainCamera.combined);

        //Le decimos al hud que se dibuje.
        hud.stage.draw();

        //Establecemos la proyeccion de la matriz de la camara principal.
        game.batch.setProjectionMatrix(mainCamera.combined);

        //Nuestro batch lo iniciamos.
        game.batch.begin();

        //Le decimos al Sprite que se dibuje segun su correspondiente region.
        megaman.draw(game.batch);

        //Le decimos al Sprite que se dibuje segun su correspondiente region?.
        zero.draw(game.batch);

        //Finalizamos nuestro batch.
        game.batch.end();
    }

    public World getWorld(){
        return world;
    }
    public TiledMap getTiledMap(){
        return tiledMap;
    }

    @Override
    public void resize(int width, int height) {
        mainViewport.update(width,height);
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
        //Eliminamos todo lo que podemos, luego de utilizarlo.
        tiledMap.dispose();
        mapRenderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
    }
}
