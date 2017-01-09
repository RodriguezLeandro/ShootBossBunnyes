package Screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
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

public class MainGameScreen implements Screen,ControllerListener{

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

    //Para saber que el hud final esta desactivado.
    private boolean finalHudActivated;

    //Para controlar el movimiento de los personajes.
    private boolean moverMegamanDerecha;
    private boolean moverMegamanIzquierda;
    private boolean moverZeroDerecha;
    private boolean moverZeroIzquierda;

    //Para ver el daño que hay que hacerle al personaje.
    private float healthDamage;

    //Para saber el delta time.
    private float deltaTime;

    //Para controlar y ver el tamaño del arraylist de fireballs.
    private Integer arrayListMegamanSize;

    //Para ver la cantidad de objetos del arraylist del personaje enemigo.
    private Integer arrayListZeroSize;

    //Arraylist que contendra los fireballs.
    private ArrayList<Fireball> arrayListMegamanFireball;

    private ArrayList<Fireball> arrayListZeroFireball;

    private Array<Controller> controllers;

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

        arrayListMegamanFireball = new ArrayList<Fireball>();
        arrayListZeroFireball = new ArrayList<Fireball>();

        //El size del array list esta vacio al comienzo.
        arrayListMegamanSize = 0;
        arrayListZeroSize = 0;

        //Porque no se puede pelear la batalla ni bien arranca.
        stageInFinalBattle = false;

        //No esta activado el final hud.
        finalHudActivated = false;

        //Asociamos el arraylist a la cantidad de controles conectados.
        controllers = Controllers.getControllers();

        //Le asignamos a cada control un Listener.
        if (controllers.size > 0)
        controllers.get(0).addListener(this);
        if (controllers.size > 1)
        controllers.get(1).addListener(this);

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
            //Si presionamos Arriba, el personaje muere.
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                megaman.setState(Megaman.State.DYING);
            }
            //Si presionamos Izquierda, el personaje se agacha.
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
            //Si presionamos derecha, el personaje pega.
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                //Solo creamos una pelota nueva si el size del arraylist actual es menor a 3.
                //Solo cambia el estado de megaman si puede tirar pelotas(estado-->animacion).
                if (arrayListMegamanSize < 3) {
                     megaman.setState(Megaman.State.HITTING);

                     //Aca tenemos que crear la bola de fuego(fireball).
                     Vector2 positionFireball = megaman.getPositionFireAttack();

                    //Si el personaje mira a la derecha, dispara hacia alli,
                    if (megaman.isRunningRight()) {
                        arrayListMegamanFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, true, megaman));
                    } else {
                        //Si mira a la izquierda, dispara hacia el otro lado.
                        arrayListMegamanFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, false, megaman));
                    }
                }
            }
            //Si presionamos Q, el personaje es lastimado.
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                megaman.setState(Megaman.State.GETTINGHIT);
            }
            //Si presionamos P, el personaje pierde vida.
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                dañarMegamanPersonaje(10);
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
                    //Solo creamos una pelota nueva si el size del arraylist actual es menor a 3.
                    //Solo cambia el estado del personaje enemigo si puede tirar pelotas(estado-->animacion).
                    if (arrayListZeroSize < 3) {
                        zero.setState(Zero.State.HITTING);

                        //Aca tenemos que crear la bola de fuego(fireball).
                        Vector2 positionFireball = zero.getPositionFireAttack();

                        //Si el personaje mira a la derecha, dispara hacia alli,
                        if (zero.isRunningRight()) {
                            arrayListZeroFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, true, zero));
                        } else {
                            //Si mira a la izquierda, dispara hacia el otro lado.
                            arrayListZeroFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, false, zero));
                        }
                    }
            }
            //Si presionamos 9, el personaje enemigo se agacha.
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

    public void logicaMovimientoPersonajes(){
        if (moverMegamanDerecha){
            if (megaman.body.getLinearVelocity().x < 3)
                megaman.body.applyLinearImpulse(new Vector2(0.5f, 0), megaman.body.getWorldCenter(), true);
        }
        if (moverMegamanIzquierda){
            if (megaman.body.getLinearVelocity().x > -3)
                megaman.body.applyLinearImpulse(new Vector2(-0.5f, 0), megaman.body.getWorldCenter(), true);
        }
        if (moverZeroDerecha){
            if (zero.body.getLinearVelocity().x < 3)
                zero.body.applyLinearImpulse(new Vector2(0.5f, 0), zero.body.getWorldCenter(), true);
        }
        if (moverZeroIzquierda){
            if (zero.body.getLinearVelocity().x > -3)
                zero.body.applyLinearImpulse(new Vector2(-0.5f, 0), zero.body.getWorldCenter(), true);
        }
    }


    public void update(float delta){

        //Guardamos el delta time.
        deltaTime = delta;

        if (stageInFinalBattle && !finalHudActivated){
            hud.setGameIsInFinalStage();
            finalHudActivated = true;
        }

        //Manejamos la entrada de datos de los usuarios.
        handleMegamanInput(delta);
        handleZeroInput(delta);

        //Tambien manejamos la logica del movimiento de los personajes.
        logicaMovimientoPersonajes();

        //Preguntamos si hay que dañarlo progresivamente al personaje.
        if (dañarPersonajeProgresivamente){
            dañarMegamanPersonaje(getHealthDamage());
        }

        //Le decimos al mundo que avanze, que efectue cambios en las fisicas.
        world.step(1/60f,6,2);

        //Updateamos la posicion del sprite de nuestro personaje y luego la animacion.
        megaman.update(delta);

        //Updateamos la posicion del sprite del personaje enemigo y luego la animacion.
        zero.update(delta);

        //Tenemos que updatear cada fireball lanzado.
        arrayListMegamanSize = arrayListMegamanFireball.size();

        //Para cada fireball de la lista, updateamos.
        for (int i = 0; i < arrayListMegamanSize; i ++){
            arrayListMegamanFireball.get(i).update(delta);
        }

        //Tenemos que updatear cada fireball lanzado.
        arrayListZeroSize = arrayListZeroFireball.size();

        //Para cada fireball de la lista, updateamos.
        for (int i = 0; i < arrayListZeroSize; i ++){
            arrayListZeroFireball.get(i).update(delta);
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
                megaman.body.setTransform(new Vector2(6410 / MegamanMainClass.PixelsPerMeters,megaman.body.getPosition().y),megaman.body.getAngle());
            }

            if (megaman.body.getPosition().x > 7190 / MegamanMainClass.PixelsPerMeters){
                megaman.body.setLinearVelocity(0,megaman.body.getLinearVelocity().y);
                megaman.body.setTransform(new Vector2(7190 / MegamanMainClass.PixelsPerMeters,megaman.body.getPosition().y),megaman.body.getAngle());
            }
            //Zero tampoco puede salir de la pantalla.
            if (zero.body.getPosition().x < 6410 / MegamanMainClass.PixelsPerMeters){
                zero.body.setLinearVelocity(0,zero.body.getLinearVelocity().y);
                zero.body.setTransform(new Vector2(6410 / MegamanMainClass.PixelsPerMeters,zero.body.getPosition().y),zero.body.getAngle());
            }

            if (zero.body.getPosition().x > 7190 / MegamanMainClass.PixelsPerMeters){
                zero.body.setLinearVelocity(0,zero.body.getLinearVelocity().y);
                zero.body.setTransform(new Vector2(7190 / MegamanMainClass.PixelsPerMeters,zero.body.getPosition().y),zero.body.getAngle());
            }

        }

        //Vemos cuantos objetos hay en el arraylist.
            arrayListMegamanSize = arrayListMegamanFireball.size();

        //Decimos que para cada fireball.
            for(int i = 0; i < arrayListMegamanSize; i ++){
                //Si sale de la camara lo eliminamos.
                    if ((arrayListMegamanFireball.get(i).body.getPosition().x > mainCamera.position.x + 400 / MegamanMainClass.PixelsPerMeters) || (arrayListMegamanFireball.get(i).body.getPosition().x < mainCamera.position.x - 400 / MegamanMainClass.PixelsPerMeters)){

                        arrayListMegamanFireball.get(i).dispose();
                        arrayListMegamanFireball.remove(i);
                        arrayListMegamanSize = arrayListMegamanFireball.size();
                    }
            }

        arrayListZeroSize = arrayListZeroFireball.size();

        for(int i = 0; i < arrayListZeroSize; i ++){
            if ((arrayListZeroFireball.get(i).body.getPosition().x > mainCamera.position.x + 400 / MegamanMainClass.PixelsPerMeters) || (arrayListZeroFireball.get(i).body.getPosition().x < mainCamera.position.x - 400 / MegamanMainClass.PixelsPerMeters)){

                arrayListZeroFireball.get(i).dispose();
                arrayListZeroFireball.remove(i);
                arrayListZeroSize = arrayListZeroFireball.size();
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

    public void dañarMegamanPersonaje(float health){

        //Dañamos al personaje y verificamos si murio.
        personajeEstaMuerto = hud.dañarMegamanPersonaje(health);
        //Si esta muerto, ponemos estado Dying(deberia ser Dead).
        if(personajeEstaMuerto){
            megaman.setState(Megaman.State.DYING);
            megaman.setMegamanIsDead();
        }
    }

    public void dañarZeroPersonaje(){
        //Dañamos al personaje y verificamos si murio.
        personajeEstaMuerto = hud.dañarZeroPersonaje();
        //Si esta muerto, ponemos estado Dying(deberia ser Dead).
        if(personajeEstaMuerto){
            zero.setState(Zero.State.DYING);
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
        arrayListMegamanSize = arrayListMegamanFireball.size();

        //Para cada fireball de la lista, dibujamos.
        for (int i = 0; i < arrayListMegamanSize; i ++){
            arrayListMegamanFireball.get(i).draw(game.batch);
        }

        //Dibujamos el otro fireball.
        //Tenemos que dibujar cada fireball lanzado.
        int arrayListZeroSize = arrayListZeroFireball.size();

        //Para cada fireball de la lista, dibujamos.
        for (int i = 0; i < arrayListZeroSize; i ++){
            arrayListZeroFireball.get(i).draw(game.batch);
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
        //No lo elimino al mundo porque me trae problemas con el Controller de gdx libgdx.
        //Que utilizo para conectar los controles gamepads.
        //world.dispose();
        box2DDebugRenderer.dispose();
        arrayListZeroFireball.clear();
    }

    //Lo de abajo son metodos para controles.
    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    //Aca manejamos los inputs de los botones. x cuad triang red l1 l2 start l3 r3 31 32 select, etc
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        //Controller.getbutton 0 es triangulo.
        if (controller.getButton(0)){
            //Hacemos un flash.
            if (controller.equals(controllers.get(0))){
                if (megaman.isRunningRight()){
                    megaman.body.setTransform(new Vector2(megaman.body.getPosition().x + 100 / MegamanMainClass.PixelsPerMeters,megaman.body.getPosition().y),megaman.body.getAngle());
                }else {
                    megaman.body.setTransform(new Vector2(megaman.body.getPosition().x - 100 / MegamanMainClass.PixelsPerMeters,megaman.body.getPosition().y),megaman.body.getAngle());
                }
            }
            else {
                if (zero.isRunningRight()){
                    zero.body.setTransform(new Vector2(zero.body.getPosition().x + 100 / MegamanMainClass.PixelsPerMeters,zero.body.getPosition().y),zero.body.getAngle());
                }
                else {
                    zero.body.setTransform(new Vector2(zero.body.getPosition().x - 100 / MegamanMainClass.PixelsPerMeters,zero.body.getPosition().y),megaman.body.getAngle());
                }
            }
        }


        //Controller.getbutton(1) es redondo.
        if(controller.getButton(1)){
            if (controller.equals(controllers.get(0))){
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
            else{
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
        }


        //controller.getbutton(2) es equis.
        if(controller.getButton(2)){
            if (controller.equals(controllers.get(0))){
                //Si presionamos cuadrado, el personaje salta.
                    //Solo salta si no estaba saltando o volando en el aire.
                    if(!megaman.isMegamanJumping())
                        megaman.body.applyLinearImpulse(new Vector2(0, 9f), megaman.body.getWorldCenter(), true);
            }
            else{
                if(!zero.isZeroJumping())
                    zero.body.applyLinearImpulse(new Vector2(0, 10f), zero.body.getWorldCenter(), true);
            }
        }

        //controller.getButton(3) es cuadrado
        if(controller.getButton(3)){
            if (controller.equals(controllers.get(0))){
                if (arrayListMegamanSize < 3) {
                    megaman.setState(Megaman.State.HITTING);

                    //Aca tenemos que crear la bola de fuego(fireball).
                    Vector2 positionFireball = megaman.getPositionFireAttack();

                    //Si el personaje mira a la derecha, dispara hacia alli,
                    if (megaman.isRunningRight()) {
                        arrayListMegamanFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, true, megaman));
                    } else {
                        //Si mira a la izquierda, dispara hacia el otro lado.
                        arrayListMegamanFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, false, megaman));
                    }
                }
            }
            else{
                if (arrayListZeroSize < 3) {
                    zero.setState(Zero.State.HITTING);

                    //Aca tenemos que crear la bola de fuego(fireball).
                    Vector2 positionFireball = zero.getPositionFireAttack();

                    //Si el personaje mira a la derecha, dispara hacia alli,
                    if (zero.isRunningRight()) {
                        arrayListZeroFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, true, zero));
                    } else {
                        //Si mira a la izquierda, dispara hacia el otro lado.
                        arrayListZeroFireball.add(new Fireball(this, positionFireball.x, positionFireball.y, false, zero));
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {

        //Solo si nos estamos moviendo en el eje x.
        //Luego con controller.getAxis(axisCode), comprobamos que haya terminado completamente
        //el movimiento, por ej, JustTouched. Porque el analog tiene 2 sensores?
        if (axisCode == 1 && (controller.getAxis(axisCode) > 0.99 || controller.getAxis(axisCode) < -0.99)) {
                //Si estamos con el control 1.
                if (controller.equals(controllers.get(0))) {
                    //Si el axiscode es 1, mover a la derecha.
                    if (controller.getAxis(axisCode) > 0.99){
                        //La logica es la siguiente: Si el personaje se estaba moviendo a la derecha, y ahora quiere ir a la izquierda,
                        //lo frenamos, en cambio si estaba frenado, lo dejamos correr al otro lado.
                        if (moverMegamanIzquierda){
                            //Por ultimo, comprobamos que el personaje no este saltando, ya que sino se convierte
                            //en un movimiento feo.
                            if (megaman.isMegamanJumping()){
                                moverMegamanIzquierda = false;
                                moverMegamanDerecha = true;
                            }
                            else {
                                moverMegamanIzquierda = false;
                            }
                        }
                        else {
                            moverMegamanDerecha = true;
                        }
                        //Dejo comentada la logica anterior por si la llego a necesitar(no creo).
                        //Solo Si la velocidad actual del personaje es menor a 3 aplicamos el impulso.
                     //   if (megaman.body.getLinearVelocity().x < 3)
                    //        megaman.body.applyLinearImpulse(new Vector2(2f, 0), megaman.body.getWorldCenter(), true);
                    }
                    //Sino, mover a la izquierda en el otro caso.
                    else if (controller.getAxis(axisCode) < -0.99){
                        if (moverMegamanDerecha){
                            if (megaman.isMegamanJumping()){
                                moverMegamanDerecha = false;
                                moverMegamanIzquierda = true;
                            }
                            else {
                                moverMegamanDerecha = false;
                            }
                        }
                        else {
                            moverMegamanIzquierda = true;
                        }
                        //Solo Si la velocidad actual del personaje es menor a 3 aplicamos el impulso.
                    //    if (megaman.body.getLinearVelocity().x > -3)
                   //         megaman.body.applyLinearImpulse(new Vector2(-2f, 0), megaman.body.getWorldCenter(), true);
                    }
                }
                //Si es el control 2, entonces zero se tiene que mover.
                else {
                    //Si el axiscode es 1, mover a la derecha.
                    if (controller.getAxis(axisCode) > 0.99){
                        if (moverZeroIzquierda){
                            if (zero.isZeroJumping()){
                                moverZeroIzquierda = false;
                                moverZeroDerecha = true;
                            }
                            else {
                                moverZeroIzquierda = false;
                            }
                        }
                        else {
                            moverZeroDerecha = true;
                        }
                        //Solo Si la velocidad actual del personaje es menor a 3 aplicamos el impulso.
                  //      if (zero.body.getLinearVelocity().x < 3)
                 //           zero.body.applyLinearImpulse(new Vector2(2f, 0), zero.body.getWorldCenter(), true);
                    }
                    //Sino, mover a la izquierda en el otro caso.
                    else if (controller.getAxis(axisCode) < -0.99){
                        if (moverZeroDerecha){
                            if (zero.isZeroJumping()){
                                moverZeroDerecha = false;
                                moverZeroIzquierda = true;
                            }
                            else {
                                moverZeroDerecha = false;
                            }
                        }
                        else {
                            moverZeroIzquierda = true;
                        }
                        //Solo Si la velocidad actual del personaje es menor a 3 aplicamos el impulso.
                    //    if (zero.body.getLinearVelocity().x > -3)
                    //        zero.body.applyLinearImpulse(new Vector2(-2f, 0), zero.body.getWorldCenter(), true);
                    }
                }
            }

        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
