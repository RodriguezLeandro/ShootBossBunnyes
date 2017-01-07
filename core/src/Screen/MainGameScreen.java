package Screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.megamangame.MegamanMainClass;

import java.util.ArrayList;

import Scenes.Hud;
import Sprites.Fireball;
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
    private TextureAtlas textureAtlasCharac;
    private TextureAtlas textureAtlasTools;

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

    //Para saber si estan peleando la batalla final boss.
    private boolean stageInFinalBattle;

    //Para ver el daño que hay que hacerle al personaje.
    private float healthDamage;

    //Para saber el delta time.
    private float deltaTime;

    //Para controlar y ver el tamaño del arraylist de fireballs.
    private Integer arrayListSize;

    private ArrayList<Fireball> arrayListFireball;

    public MainGameScreen(MegamanMainClass game) {

        //Le asignamos el juego a nuestro MegamanMainClass.
        this.game = game;

        textureAtlasCharac = new TextureAtlas("charac1/charac1.pack");

        textureAtlasTools = new TextureAtlas("tools/tools.pack");

        //Creamos una camara para que el usuario vea el renderizado.
        mainCamera = new OrthographicCamera();

        //Con el FitViewport nos aseguramos que la app cuente con multiples resoluciones.
        mainViewport = new FitViewport(game.Virtual_Width / game.PixelsPerMeters, game.Virtual_Height / game.PixelsPerMeters, mainCamera);

        //Cargamos el archivo tmx de tiles map.
        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("tiledmap/tiled1.tmx");

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

        //Si estamos en un dispositivo android:
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            //Creamos el Hud de nuestro juego para android.
            hud = new Hud(game.batch,textureAtlasTools,true);
        }else{
            //Si estamos en un dispositivo que no es android(seguramente desktop):
            hud = new Hud(game.batch,textureAtlasTools,false);
        }
        //Porque claramente, nuestro personaje aun vive.
        personajeEstaMuerto = false;

        //No lo queremos dañar ni bien arranca.
        dañarPersonajeProgresivamente = false;

        arrayListFireball = new ArrayList<Fireball>();

        //El size del array list esta vacio al comienzo.
        arrayListSize = 0;

        //Porque no se puede pelear la batalla ni bien arranca.
        stageInFinalBattle = false;
    }

    public  TextureAtlas getTextureAtlasCharac(){
        return textureAtlasCharac;
    }

    public  TextureAtlas getTextureAtlasTools(){
        return textureAtlasTools;
    }

    @Override
    public void show() {

    }

    //Funcion que se encargara de manejar los inputs que hayan en el juego.
    public void handleMegamanInput(float delta){

        if (!megaman.isDead()) {

            //Esto es para saber si esta siendo tocada la flecha para arriba y que salte solo una vez.
            //Es decir, que no salte muchas veces sino hasta que termine de hacer click.
            if (hud.isUpArrowPressed()){
                if (Gdx.input.justTouched()) {
                    hud.setUpArrowPressed(false);
                    if (!megaman.isMegamanJumping()) {
                        megaman.body.applyLinearImpulse(new Vector2(0, 6f), megaman.body.getWorldCenter(), true);
                    }
                }
            }

            if (hud.isLeftArrowPressed()){
                if (megaman.body.getLinearVelocity().x > -3)
                megaman.body.applyLinearImpulse(new Vector2(-0.2f, 0), megaman.body.getWorldCenter(), true);
            }
            if (hud.isRightArrowPressed()){
                if (megaman.body.getLinearVelocity().x < 3)
                megaman.body.applyLinearImpulse(new Vector2(0.2f, 0), megaman.body.getWorldCenter(), true);
            }
            if (hud.isLeftButtonPressed()){
                megaman.setState(Megaman.State.HITTING);
            }
            if (hud.isDownButtonPressed()){
                //Solo si recien tocamos la pantalla.
                if (Gdx.input.justTouched()){
                    hud.setDownButtonPressed(false);

                    //Si ya se estaba agachando, el personaje se para.
                    if (megaman.getState() == Megaman.State.CROUCHING) {
                        megaman.setState(Megaman.State.STANDING);
                        megaman.redefineMegaman();
                    }
                    //Si no estaba agachado, se para.
                    else {
                        megaman.setState(Megaman.State.CROUCHING);
                        if (megaman.isRunningRight()) {
                            megaman.redefineMegamanCrouching(true);
                        }
                        else{
                            megaman.redefineMegamanCrouching(false);
                        }
                    }
                }
            }
            //Si presionamos W, el personaje salta.
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                //Solo salta si no estaba saltando o volando en el aire.
                if(!megaman.isMegamanJumping())
                megaman.body.applyLinearImpulse(new Vector2(0, 9f), megaman.body.getWorldCenter(), true);
            }
            //Si presionamos D, el personaje se mueve a la derecha.
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                //Solo Si la velocidad actual del personaje es menor a 3 aplicamos el impulso.
                if (megaman.body.getLinearVelocity().x < 3)
                megaman.body.applyLinearImpulse(new Vector2(0.2f, 0), megaman.body.getWorldCenter(), true);
            }
            //Si presionamos A, el personaje se mueve a la izquierda.
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                //Solo Si la velocidad actual del personaje es menor a 3 aplicamos el impulso.
                if (megaman.body.getLinearVelocity().x > -3)
                megaman.body.applyLinearImpulse(new Vector2(-0.2f, 0), megaman.body.getWorldCenter(), true);
            }
            //Si presionamos Flechita UP, el personaje muere.
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                megaman.setState(Megaman.State.DYING);
            }
            //Si presionamos Left, el personaje se agacha.
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                //Si ya se estaba agachando, el personaje se para.
                if (megaman.getState() == Megaman.State.CROUCHING) {
                    megaman.setState(Megaman.State.STANDING);
                    megaman.redefineMegaman();
                }
                //Si no estaba agachado, se para.
                else {
                    megaman.setState(Megaman.State.CROUCHING);
                    if (megaman.isRunningRight()) {
                        megaman.redefineMegamanCrouching(true);
                    }
                    else{
                        megaman.redefineMegamanCrouching(false);
                    }
                }
            }
            //Si presionamos Right, el personaje pega.
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                //Solo creamos una pelota nueva si el size del arraylist actual es menor a 3.
                //Solo cambia el estado de megaman si puede tirar pelotas(estado-->animacion).
                if (arrayListSize < 3) {
                     megaman.setState(Megaman.State.HITTING);

                     //Aca tenemos que crear la bola de fuego(fireball).
                     Vector2 positionFireball = megaman.getPositionFireAttack();

                    //Si el personaje mira a la derecha, dispara hacia alli,
                    if (megaman.isRunningRight()) {
                        arrayListFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, true));
                    } else {
                        //Si mira a la izquierda, dispara hacia el otro lado.
                        arrayListFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, false));
                    }
                }
            }
            //Si presionamos Down, el personaje es lastimado.
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                megaman.setState(Megaman.State.GETTINGHIT);
            }
            //Si presionamos P, el personaje pierde vida.
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                dañarPersonaje(10);
            }
            //Si presionamos O, el personaje pierde mana.
            if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
                hud.gastarMana(30);
            }
            //Si presionamos L, el personaje gana vida.
            if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
                hud.curarPersonaje(30);
            }
            //Si presionamos K, el personaje gana mana.
            if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                hud.recuperarMana(30);
            }
        }
    }

    public void handleZeroInput(float delta){

        if (!zero.isDead()) {
            //Si presionamos 8, el personaje enemigo salta.
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)) {
                if(!zero.isZeroJumping())
                zero.body.applyLinearImpulse(new Vector2(0, 10f), zero.body.getWorldCenter(), true);
            }
            //Si presionamos 4, el personaje enemigo se mueve a la izquierda.
            if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)) {
                if (zero.body.getLinearVelocity().x > -3)
                zero.body.applyLinearImpulse(new Vector2(-0.2f, 0), zero.body.getWorldCenter(), true);
            }
            //Si presionamos 6, el personaje enemigo se mueve a la derecha.
            if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)) {
                if (zero.body.getLinearVelocity().x < 3)
                zero.body.applyLinearImpulse(new Vector2(0.2f, 0), zero.body.getWorldCenter(), true);
            }
            //Si presionamos 7, el personaje enemigo pega.
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7)) {
                zero.setState(Zero.State.HITTING);
            }
            //Si presionamos 9, el personaje enemigo pega.
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9)) {
                //Si ya se estaba agachando, el personaje se para.
                if (zero.getState() == Zero.State.CROUCHING) {
                    zero.setState(Zero.State.STANDING);
                    zero.redefineZero();
                }
                //Si no estaba agachado, se para.
                else {
                    zero.setState(Zero.State.CROUCHING);
                    if (zero.isRunningRight()) {
                        zero.redefineZeroCrouching(true);
                    }
                    else{
                        zero.redefineZeroCrouching(false);
                    }
                }
            }
            //Si presionamos 3, el personaje enemigo pega.
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) {
                zero.setState(Zero.State.GETTINGHIT);
            }
            //Si presionamos 1, el personaje enemigo pega.
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
                zero.setState(Zero.State.DYING);
            }
        }
    }

    //Para ver si el jugador esta muerto desde hace mas de 3 segundos.
    public boolean gameOver(){
        if (megaman.isDead() && megaman.getStateTimer() > 3f){
            return true;
        }
        else{
            return false;
        }
    }

    public float getDeltaTime(){
        return deltaTime;
    }

    public void update(float delta){

        //Guardamos el delta time.
        deltaTime = delta;

        //Manejamos la entrada de datos de los usuarios.
        handleMegamanInput(delta);
        handleZeroInput(delta);

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

        //Tenemos que updatear cada fireball lanzado.
        arrayListSize = arrayListFireball.size();

        //Para cada fireball de la lista, updateamos.
        for (int i = 0; i < arrayListSize; i ++){
            arrayListFireball.get(i).update(delta);
        }

        //Si estamos en la batalla final los movimientos de la camara son distintos.
        //Las restricciones son distintas tambien.
        if (!stageInFinalBattle) {
            //Si el personaje quiere cruzar el limite izquierdo de la pantalla no lo dejamos.
            if (megaman.body.getPosition().x < 10 / MegamanMainClass.PixelsPerMeters) {
                megaman.body.applyLinearImpulse(new Vector2(1, 0), megaman.body.getWorldCenter(), true);
            }

            //Si el personaje se encuentra dentro de los limites del mundo, la camara lo sigue.
            if ((megaman.body.getPosition().x >= 400 / MegamanMainClass.PixelsPerMeters) && (megaman.body.getPosition().x <= 6000 / MegamanMainClass.PixelsPerMeters)) {

                //Hacemos que la camara tenga en el centro a nuestro personaje principal.
                mainCamera.position.x = megaman.body.getPosition().x;

            } else {
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
            if (megaman.body.getPosition().y <= 0) {
                megaman.setState(Megaman.State.DYING);
            }

            //Si el personaje cruza el limite derecho de la pantalla, actualizamos la camara.
            if (megaman.body.getPosition().x > 6400 / MegamanMainClass.PixelsPerMeters) {
                mainCamera.position.x = 68;
                stageInFinalBattle = true;
            }

            //Todo lo que ocurre arriba es solo si no estamos en la batalla final.
        }else{
            //El personaje no puede cruzar ni el limite izquierdo ni el limite derecho de la pantalla.
            if (megaman.body.getPosition().x < 6410 / MegamanMainClass.PixelsPerMeters){
                megaman.body.setLinearVelocity(0,megaman.body.getLinearVelocity().y);
                megaman.body.applyLinearImpulse(new Vector2(1f,0),megaman.body.getWorldCenter(),true);
            }

            if (megaman.body.getPosition().x > 7190 / MegamanMainClass.PixelsPerMeters){
                megaman.body.setLinearVelocity(0,megaman.body.getLinearVelocity().y);
                megaman.body.applyLinearImpulse(new Vector2(-1f,0),megaman.body.getWorldCenter(),true);
            }

        }

            arrayListSize = arrayListFireball.size();

            for(int i = 0;i < arrayListSize; i ++){
                    if ((arrayListFireball.get(i).body.getPosition().x > mainCamera.position.x + 400 / MegamanMainClass.PixelsPerMeters) || (arrayListFireball.get(i).body.getPosition().x < mainCamera.position.x - 400 / MegamanMainClass.PixelsPerMeters)){

                        arrayListFireball.get(i).dispose();
                        arrayListFireball.remove(i);
                        arrayListSize = arrayListFireball.size();
                    }
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
            megaman.setMegamanIsDead();
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

        //Establecemos la proyeccion de la matriz de la camara principal.
        game.batch.setProjectionMatrix(mainCamera.combined);

        //Nuestro batch lo iniciamos.
        game.batch.begin();

        //Le decimos al Sprite que se dibuje segun su correspondiente region.
        megaman.draw(game.batch);

        //Le decimos al Sprite que se dibuje segun su correspondiente region?.
        zero.draw(game.batch);

        //Dibujamos el fireball.
        //Tenemos que dibujar cada fireball lanzado.
        int arrayListSize = arrayListFireball.size();

        //Para cada fireball de la lista, dibujamos.
        for (int i = 0; i < arrayListSize; i ++){
            arrayListFireball.get(i).draw(game.batch);
        }

        //Finalizamos nuestro batch.
        game.batch.end();

        //Establecemos la projeccion de la matriz de la camara hud.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //Le decimos al hud que se dibuje.
        hud.stage.draw();

        //cuando termine de dibujar todo, preguntamos si el juego termino.
        if (gameOver()){
            game.setScreen(new GameOverScreen(game,hud.getScore()));
            dispose();
        }
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
