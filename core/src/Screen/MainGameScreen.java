package Screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.megamangame.MegamanMainClass;

import java.util.ArrayList;

import Scenes.Hud;
import Sprites.Fireball;
import Sprites.Megaman;
import Sprites.RayCast;
import Tools.WorldContactListener;

/**
 * Created by Leandro on 01/01/2017.
 */

//Voy a hacer esta clase abstracta, y voy a intentar separar lo mejor posible lo reutilizable.
public abstract class MainGameScreen implements Screen {

    //Objeto MegamanMainClass
    protected MegamanMainClass game;

    //Camara para nuestro juego y viewport para ajustar resoluciones.
    protected OrthographicCamera mainCamera;
    protected FitViewport mainViewport;

    //Para cargar archivo tmx.
    protected TmxMapLoader tmxMapLoader;

    //World es el mundo de nuestro juego.
    protected World world;

    //Lo utilizamos para ver las lineas que delimitan los objetos pertenecientes a nuestro juego.
    protected Box2DDebugRenderer box2DDebugRenderer;

    //Megaman es el personaje principal.
    protected Megaman megaman;

    //Creamos el hud de nuestro juego(pantalla principal).
    protected Hud hud;

    //Para guardar el levelSelectScreen.
    protected LevelSelect levelSelectScreen;

    //Esto lo dejo ya que las tools las voy a utilizar en el hud.
    protected TextureAtlas textureAtlasTools;

    //Definimos Atlas para las texturas de los personajes.
    protected TextureAtlas textureAtlasCharac;

    //Hasta aca va a ser de esta clase, de aca para abajo me parece que saco todo.

    //Para saber si estan peleando la batalla final boss.
    protected boolean stageInFinalBattle;

    //Para saber que el hud final esta desactivado.
    protected boolean finalHudActivated;

    //Para controlar el movimiento de los personajes.
    protected boolean moverMegamanDerecha;
    protected boolean moverMegamanIzquierda;

    protected boolean realizarRayCast;
    protected boolean vaciarRayCast;
    protected boolean vaciarSpritesRayCast;
    protected boolean fireToRight;

    //Para ver el daño que hay que hacerle al personaje.
    protected float healthDamage;

    //Para saber el delta time.
    protected float deltaTime;

    //Booleano para sacarle vida al personaje progresivamente.
    protected boolean dañarPersonajeProgresivamente;

    //Para controlar y ver el tamaño del arraylist de fireballs.
    protected Integer arrayListMegamanSize;

    protected Integer multiplicadorRaycast;

    //Arraylist que contendra los fireballs.
    protected ArrayList<Fireball> arrayListMegamanFireball;

    protected ArrayList<RayCast> arrayListMegamanRaycast;

    //Para verificar si el personaje murio.
    protected boolean personajeEstaMuerto;

    protected Vector2 positionRayCast;

    protected Vector2 positionInitialRaycast;

    public MainGameScreen(MegamanMainClass game, LevelSelect levelSelect) {

        //Le asignamos el juego a nuestro MegamanMainClass.
        this.game = game;

        levelSelectScreen = levelSelect;

        textureAtlasCharac = new TextureAtlas("charac1/charac1.pack");

        textureAtlasTools = new TextureAtlas("tools/tools.pack");

        //Creamos una camara para que el usuario vea el renderizado.
        mainCamera = new OrthographicCamera();

        //Con el FitViewport nos aseguramos que la app cuente con multiples resoluciones.
        mainViewport = new FitViewport(game.Virtual_Width / game.PixelsPerMeters, game.Virtual_Height / game.PixelsPerMeters, mainCamera);

        //Cargamos el archivo tmx de tiles map.
        tmxMapLoader = new TmxMapLoader();

        //Ponemos como posicion inicial de la camara el medio de la pantalla virtual(inicial).
        mainCamera.position.set((game.Virtual_Width / 2) / game.PixelsPerMeters, (game.Virtual_Height / 2) / game.PixelsPerMeters, 0);

        //Creamos el mundo de nuestro juego.
        world = new World(new Vector2(0, -10), true);

        //Creamos nuestro debugrenderer.
        box2DDebugRenderer = new Box2DDebugRenderer();

        //Creamos a nuestro personaje principal.
        megaman = new Megaman(this);

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
            hud = new Hud(game.batch, textureAtlasTools, true);
        } else {
            //Si estamos en un dispositivo que no es android(seguramente desktop):
            hud = new Hud(game.batch, textureAtlasTools, false);
        }

        arrayListMegamanFireball = new ArrayList<Fireball>();

        arrayListMegamanRaycast = new ArrayList<RayCast>();


        //El size del array list esta vacio al comienzo.
        arrayListMegamanSize = 0;

        //Porque no se puede pelear la batalla ni bien arranca.
        stageInFinalBattle = false;

        //No esta activado el final hud.
        finalHudActivated = false;

        realizarRayCast = false;

        vaciarRayCast = false;

        vaciarSpritesRayCast = false;

    }

    public TextureAtlas getTextureAtlasTools() {
        return textureAtlasTools;
    }

    @Override
    public void show() {

    }

    public OrthographicCamera getMainCamera(){
        return mainCamera;
    }

    public void handleMegamanInputAndroid(){
        //Si toca flecha izquierda impulso a izquierda.
        if (hud.isLeftArrowPressed()) {
            if (megaman.body.getLinearVelocity().x > -3)
                megaman.body.applyLinearImpulse(new Vector2(-0.2f, 0), megaman.body.getWorldCenter(), true);
        }
        //Si toca flecha derecha impulso a derecha.
        if (hud.isRightArrowPressed()) {
            if (megaman.body.getLinearVelocity().x < 3)
                megaman.body.applyLinearImpulse(new Vector2(0.2f, 0), megaman.body.getWorldCenter(), true);
        }
        //Si toca cuadrado pega.
        if (hud.isLeftButtonPressed()) {
            hud.setLeftButtonPressed(false);
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
        //Si toca redondo slashea.
        if ( hud.isRightButtonPressed()) {
            hud.setRightButtonPressed(false);
            //Solo puede slashear si no estaba saltando.
            if (!megaman.isMegamanJumping()) {
                if (megaman.isRunningRight()) {
                    if (megaman.body.getLinearVelocity().x < 5)
                        megaman.body.applyLinearImpulse(new Vector2(4f, 0), megaman.body.getWorldCenter(), true);
                    megaman.setState(Megaman.State.SLASHING);
                } else {
                    if (megaman.body.getLinearVelocity().x > -5)
                        megaman.body.applyLinearImpulse(new Vector2(-5, 0), megaman.body.getWorldCenter(), true);
                    megaman.setState(Megaman.State.SLASHING);
                }
            }
        }
        //Si toca x salta
        if (hud.isDownButtonPressed()) {
            //Solo si recien tocamos la pantalla.
            hud.setDownButtonPressed(false);
            //Si el personaje estaba deslizandose(SLIDING) en una pared, puede saltar hacia afuera.
            if (megaman.getState() == Megaman.State.SLIDING){
                if (megaman.isRunningRight()) {
                    //Que divertido, el impulso es hacia el eje x contrario.
                    //Si desliza a la derecha, el impulso es a la izquierda, y viceversa.
                    megaman.body.applyLinearImpulse(new Vector2(-3f,6f),megaman.body.getWorldCenter(),true);
                }else {
                    megaman.body.applyLinearImpulse(new Vector2(3f,6f),megaman.body.getWorldCenter(),true);
                }
            }
            //Si estaba haciendo slash el personaje, puede saltar mas alto.
            if (megaman.getState() == Megaman.State.SLASHING) {
                if (!megaman.isMegamanJumping())
                    megaman.body.applyLinearImpulse(new Vector2(0, 8f), megaman.body.getWorldCenter(), true);
            } else {
                //Solo salta si no estaba saltando o volando en el aire.
                if (!megaman.isMegamanJumping())
                    megaman.body.applyLinearImpulse(new Vector2(0, 6f), megaman.body.getWorldCenter(), true);
            }
        }
        //Si toca triangulo hace raycast.
        if (hud.isUpButtonPressed()){
            hud.setUpButtonPressed(false);

            //Solo puedo realizar el raycast si tengo el mana suficiente.
            if (hud.getMana() > 50) {
                //Solo puedo realizar un raycast si ya termino el anterior.
                if (arrayListMegamanRaycast.isEmpty()) {
                    //Si estamos realizando el ataque, perdemos mucho mana, porque es muy fuerte este ataque.
                    hud.gastarMana(100);
                    //El problema es que no es un for, se trata de un ataque sin limites definidos.
                    //Ya no se trata de un problema jeje.
                    realizarRayCast = true;
                    multiplicadorRaycast = 0;
                    positionRayCast = megaman.getPositionFireAttack();
                    positionInitialRaycast = new Vector2(megaman.body.getPosition().x, megaman.body.getPosition().y);
                    if (megaman.isRunningRight()) {
                        fireToRight = true;
                    } else {
                        fireToRight = false;
                    }
                }
            }

        }
    }

    public void handleMegamanInputDesktop(){
        //Si presionamos W, el personaje salta.
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            //Si el personaje estaba deslizandose(SLIDING) en una pared, puede saltar hacia afuera.
            if (megaman.getState() == Megaman.State.SLIDING){
                if (megaman.isRunningRight()) {
                    //Que divertido, el impulso es hacia el eje x contrario.
                    //Si desliza a la derecha, el impulso es a la izquierda, y viceversa.
                    megaman.body.applyLinearImpulse(new Vector2(-3f,6f),megaman.body.getWorldCenter(),true);
                }else {
                    megaman.body.applyLinearImpulse(new Vector2(3f,6f),megaman.body.getWorldCenter(),true);
                }
            }
            //Si estaba haciendo slash el personaje, puede saltar mas alto.
            if (megaman.getState() == Megaman.State.SLASHING) {
                if (!megaman.isMegamanJumping())
                    megaman.body.applyLinearImpulse(new Vector2(0, 8f), megaman.body.getWorldCenter(), true);
            } else {
                //Solo salta si no estaba saltando o volando en el aire.
                if (!megaman.isMegamanJumping())
                    megaman.body.applyLinearImpulse(new Vector2(0, 6f), megaman.body.getWorldCenter(), true);
            }
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
        //Si el jugador toca flecha arriba, el personaje tira RAY!!!
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){

            //Solo puedo realizar el raycast si tengo el mana suficiente.
            if (hud.getMana() > 50) {
                //Solo puedo realizar un raycast si ya termino el anterior.
                if (arrayListMegamanRaycast.isEmpty()) {
                    //Si estamos realizando el ataque, perdemos mucho mana, porque es muy fuerte este ataque.
                    hud.gastarMana(100);
                    //El problema es que no es un for, se trata de un ataque sin limites definidos.
                    //Ya no se trata de un problema jeje.
                    realizarRayCast = true;
                    multiplicadorRaycast = 0;
                    positionRayCast = megaman.getPositionFireAttack();
                    positionInitialRaycast = new Vector2(megaman.body.getPosition().x, megaman.body.getPosition().y);
                    if (megaman.isRunningRight()) {
                        fireToRight = true;
                    } else {
                        fireToRight = false;
                    }
                }
            }
        }
        //Si el jugador toca flecha abajo, el personaje slashea.
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            //Solo puede slashear si no estaba saltando.
            if (!megaman.isMegamanJumping()) {
                if (megaman.isRunningRight()) {
                    if (megaman.body.getLinearVelocity().x < 5)
                        megaman.body.applyLinearImpulse(new Vector2(4f, 0), megaman.body.getWorldCenter(), true);
                    megaman.setState(Megaman.State.SLASHING);
                } else {
                    if (megaman.body.getLinearVelocity().x > -5)
                        megaman.body.applyLinearImpulse(new Vector2(-5, 0), megaman.body.getWorldCenter(), true);
                    megaman.setState(Megaman.State.SLASHING);
                }
            }
        }

        //Esto lo dejamos comentado, ya que es un atajo, y no queremos que se pueda realizar.
        /*
        //Si presionamos M, el personaje se traslada a una posicion cercana al final.
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            megaman.body.setTransform(12400 / MegamanMainClass.PixelsPerMeters, 200 / MegamanMainClass.PixelsPerMeters, megaman.body.getAngle());
        }
        */
        //Antes si presionabamos arriba, el personaje moria.
        //Si presionamos Arriba, el personaje ya no muere.
          /*  if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                megaman.setState(Megaman.State.DYING);
            }*/
        //Si presionamos Izquierda, el personaje se agacha.
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            //Segundo bug que me encuentro, si me agacho mientras me deslizo, se bugea.
            //Lo voy a solucionar sencillamente, no permitiendo agacharse si estoy deslizando.
            if (megaman.getState() == Megaman.State.SLIDING){
                //No hago nada, ya que se bugea.
            }
            else if(megaman.getState() == Megaman.State.HITTING){
                //Al parecer se bugea otra vez, si el jugador se agacha cuando esta deslizando y disparando.
                //Entonces, decimos que no se pueda agachar mientras esta disparando.
                //El bug tendra que ver con los bodys en box2d? Probablemente.
            }else {
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
                    } else {
                        megaman.redefineMegamanCrouching(false);
                    }
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

        //Lo dejamos comentado tambien ,y lo de abajo tambien, son cheats.
        /*
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

        */

    }

    //Funcion que se encargara de manejar los inputs que hayan en el juego.
    public void handleMegamanInput(float delta) {

        if (!megaman.isDead()) {

            handleMegamanInputAndroid();
            handleMegamanInputDesktop();

        }
    }

    public void restoreMegamanHp(float health) {
        hud.curarPersonaje(health);
    }
    public void restoreMegamanMana(float mana) {
        hud.recuperarMana(mana);
    }

    //Para ver si el jugador esta muerto desde hace mas de 3 segundos.
    public boolean gameOver() {
        if (megaman.isDead() && megaman.getStateTimer() > 3f) {
            return true;
        } else {
            return false;
        }
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void logicaMovimientoPersonajes() {
        if (moverMegamanDerecha) {
            if (megaman.body.getLinearVelocity().x < 3)
                megaman.body.applyLinearImpulse(new Vector2(0.5f, 0), megaman.body.getWorldCenter(), true);
        }
        if (moverMegamanIzquierda) {
            if (megaman.body.getLinearVelocity().x > -3)
                megaman.body.applyLinearImpulse(new Vector2(-0.5f, 0), megaman.body.getWorldCenter(), true);
        }

    }


    //El update de aca entonces se encarga de la logica de los ataques de megaman y un poco
    //Mas de cosas, todo lo que sea render, tiene que dibujarse en la clase del respectivo nivel.
    public void update(float delta) {

        //Guardamos el delta time.
        deltaTime = delta;

        //Manejamos la entrada de datos de los usuarios.
        handleMegamanInput(delta);

        //Tambien manejamos la logica del movimiento de los personajes.
        logicaMovimientoPersonajes();

        //Preguntamos si hay que dañarlo progresivamente al personaje.
        if (dañarPersonajeProgresivamente) {
            dañarMegamanPersonaje(getHealthDamage());
        }

        //Le decimos al mundo que avanze, que efectue cambios en las fisicas.
        world.step(1 / 60f, 6, 2);

        //Updateamos la posicion del sprite de nuestro personaje y luego la animacion.
        megaman.update(delta);

        //Tenemos que updatear cada fireball lanzado.
        arrayListMegamanSize = arrayListMegamanFireball.size();

        //Para cada fireball de la lista, updateamos.
        //Recordar que el arraylistmegamanfireball.get(i).destroyfireball era un booleano para
        //destruir fireball en casos de colisiones con otros objetos.
        for (int i = 0; i < arrayListMegamanSize; i++) {
            //Si hay que destruirlo, lo destruimos, sino, lo updateamos.
            if (arrayListMegamanFireball.get(i).destroyFireball) {
                arrayListMegamanFireball.get(i).dispose();
                arrayListMegamanFireball.remove(i);
                arrayListMegamanSize = arrayListMegamanFireball.size();
            } else {
                arrayListMegamanFireball.get(i).update(delta);
            }
        }

        //Vemos cuantos objetos hay en el arraylist.
        arrayListMegamanSize = arrayListMegamanFireball.size();

        //Decimos que para cada fireball.
        for (int i = 0; i < arrayListMegamanSize; i++) {
            //Si sale de la camara lo eliminamos.
            if ((arrayListMegamanFireball.get(i).body.getPosition().x > mainCamera.position.x + 400 / MegamanMainClass.PixelsPerMeters) || (arrayListMegamanFireball.get(i).body.getPosition().x < mainCamera.position.x - 400 / MegamanMainClass.PixelsPerMeters)) {

                arrayListMegamanFireball.get(i).dispose();
                arrayListMegamanFireball.remove(i);
                arrayListMegamanSize = arrayListMegamanFireball.size();
            }
        }

        if (realizarRayCast){

            //Notese que es muy importante que solo creo cuerpo para el primer body, de esa manera.
            //Puedo utilizar infinidad de sprites y conseguir un rendimiento excepcional sin crear tantos cuerpos inservibles.
            if (fireToRight) {
                //Si es el primer sprite que creo, le pongo body, de lo contrario no.
                if (arrayListMegamanRaycast.isEmpty()){
                    //Nota, por ahora dejo la distancia entre los sprites en 6, queda muy lindo visualmente.
                    //Pero tengo que ver si funciona bien en celulares, de ser asi, lo dejo en 6, de lo contrario, lo vuelvo a subir a 12.
                    //Finalmente lo subo a 12.
                    arrayListMegamanRaycast.add(new RayCast(this, positionRayCast.x + 40 / MegamanMainClass.PixelsPerMeters + multiplicadorRaycast * 16 / MegamanMainClass.PixelsPerMeters, positionRayCast.y,true));
                    multiplicadorRaycast++;
                    Integer lastraycast = arrayListMegamanRaycast.size();
                    arrayListMegamanRaycast.get(lastraycast - 1).getSprite().setSize(arrayListMegamanRaycast.get(lastraycast - 1).getSprite().getWidth(), arrayListMegamanRaycast.get(lastraycast - 1).getSprite().getHeight() + multiplicadorRaycast * 5 / MegamanMainClass.PixelsPerMeters);
                }
                else {
                    //Cada vez que añadimos un sprite, tenemos que agrandar el cuerpo de nuestro voidAttack.
                    //Tenemos que hacerlo, pero lo dejo pendiente porque tengo que estudiar quimica.
                    arrayListMegamanRaycast.add(new RayCast(this, positionRayCast.x + 40 / MegamanMainClass.PixelsPerMeters + multiplicadorRaycast * 16 / MegamanMainClass.PixelsPerMeters, positionRayCast.y,false));
                    multiplicadorRaycast++;
                    Integer lastraycast = arrayListMegamanRaycast.size();
                    arrayListMegamanRaycast.get(lastraycast - 1).getSprite().setSize(arrayListMegamanRaycast.get(lastraycast - 1).getSprite().getWidth(), arrayListMegamanRaycast.get(lastraycast - 1).getSprite().getHeight() + multiplicadorRaycast * 5 / MegamanMainClass.PixelsPerMeters);
                }
            }else {
                if (arrayListMegamanRaycast.isEmpty()){
                    arrayListMegamanRaycast.add(new RayCast(this, positionRayCast.x - 100 / MegamanMainClass.PixelsPerMeters - multiplicadorRaycast * 16 / MegamanMainClass.PixelsPerMeters, positionRayCast.y, true));
                    multiplicadorRaycast++;
                    Integer lastraycast = arrayListMegamanRaycast.size();
                    arrayListMegamanRaycast.get(lastraycast - 1).getSprite().setSize(arrayListMegamanRaycast.get(lastraycast - 1).getSprite().getWidth(), arrayListMegamanRaycast.get(lastraycast - 1).getSprite().getHeight() + multiplicadorRaycast * 5 / MegamanMainClass.PixelsPerMeters);
                }else {
                    //Dejo anotado masomenos que hace o que hago con cada valor.
                    //Multiplico primero la posicion de cada sprite, siendo que si achico mas la posicion entre cada sprite, se ve mucho mejor pero pierdo rendimiento.
                    arrayListMegamanRaycast.add(new RayCast(this, positionRayCast.x - 100 / MegamanMainClass.PixelsPerMeters - multiplicadorRaycast * 16 / MegamanMainClass.PixelsPerMeters, positionRayCast.y, false));
                    multiplicadorRaycast++;
                    Integer lastraycast = arrayListMegamanRaycast.size();
                    arrayListMegamanRaycast.get(lastraycast - 1).getSprite().setSize(arrayListMegamanRaycast.get(lastraycast - 1).getSprite().getWidth(), arrayListMegamanRaycast.get(lastraycast - 1).getSprite().getHeight() + multiplicadorRaycast * 5 / MegamanMainClass.PixelsPerMeters);
                }
            }

            Integer lastraycast = arrayListMegamanRaycast.size();

            //Mejor no, le damos un limite al raycast, decimos que cuando supera por cierta cantidad de distancia la posicion inicial de lanzamiento,
            //se empiece a borrar.
            //El algoritmo funciona asi: si el ultimo sprite que agregamos, super por 500/600 en el eje x al primer sprite agregado,
            //entonces comenzamos a eliminar los sprites uno por uno.
            if ((arrayListMegamanRaycast.get(lastraycast - 1).getPosition().x > positionInitialRaycast.x + 800 / MegamanMainClass.PixelsPerMeters)||(arrayListMegamanRaycast.get(lastraycast - 1).getPosition().x < positionInitialRaycast.x - 800 / MegamanMainClass.PixelsPerMeters)){
                realizarRayCast = false;
                vaciarRayCast = true;
            }
        }

        if (vaciarRayCast){

            //Si el arraylist de raycast quedo vacio, salimos de aca, de lo contrario,
            //Frame a frame, iremos borrando los raycasts.
            if (multiplicadorRaycast == 0){
                //Comento la logica ya que la tengo fresca.
                //con el booleano que utilizo lineas mas abajo, entro una sola vez a disposear al unico body de la tanda de sprites.
                //Luego, activo el booleano para ya solo ir borrando los sprites.
                //Multiplicadorraycast es como si fuera un contador i en un for, ej for(i = 0; i < ...).
                //Cuando finalizo de hacer todo, reinicio los valores por defecto y funciona!.
                vaciarRayCast = false;
                vaciarSpritesRayCast = false;
            }else {
                //Utilizo un booleano para solo entrar una vez a eliminar el unico cuerpo que hay.
                if (vaciarSpritesRayCast){
                    arrayListMegamanRaycast.remove(0);
                    multiplicadorRaycast--;
                }else{
                    arrayListMegamanRaycast.get(0).dispose();
                    arrayListMegamanRaycast.remove(0);
                    multiplicadorRaycast --;
                    vaciarSpritesRayCast = true;
                }
                //Como solo existe un body, solo haremos el dispose 1 sola vez, y luego limpiamos los sprites(prefiero limpiar los sprites de a 1, queda mas lindo).

            }
        }

        //Updateo los raycast.
        for (RayCast rayCast : arrayListMegamanRaycast){
            rayCast.update(delta);
        }

    }

    //En esta funcion, tenemos que modificar los cuerpos del juego para que sean afectados
    //por la gravedad, dependiendo de si se utilizo el ataque especial de gravedad o no.
    public abstract void setGravityModifyOn();

    //En esta funcion, tenemos que volver todo lo modificado anteriormente a la normalidad.
    public abstract void setGravityModifyOff();

    //Esta funcion es para añadir puntaje cuando eliminamos un enemigo.
    public abstract void setAddScore(Integer score);

    public void draw(SpriteBatch spriteBatch){
        //Le decimos al Sprite que se dibuje segun su correspondiente region.
        megaman.draw(game.batch);

        //Dibujamos el fireball.

        //Para cada fireball de la lista, dibujamos.
        for(Fireball fireball : arrayListMegamanFireball){
            fireball.draw(game.batch);
        }

        for (RayCast rayCast : arrayListMegamanRaycast){
            rayCast.draw(game.batch);
        }

    }

    //Esta tambien estaba hecha para el hijo nomas.
    public Megaman getMegaman() {
        return megaman;
    }
    public void setZeroFightState(Integer integer) {
        //Esta clase esta hecha para que su hijo la utilice.
    }

    public float getHealthDamage() {
        return healthDamage;
    }

    public void setHealthDamage(float healthDamage) {
        this.healthDamage = healthDamage;
    }

    public void dañarMegamanPersonaje(float health) {

        //Dañamos al personaje y verificamos si murio.
        personajeEstaMuerto = hud.dañarMegamanPersonaje(health);
        //Si esta muerto, ponemos estado Dying(deberia ser Dead).
        if (personajeEstaMuerto) {
            megaman.setState(Megaman.State.DYING);
            megaman.setMegamanIsDead();
        }
    }

    public void dañarPersonajeProgresivamente(float health, boolean bool) {
        //Si hay que dañarlo al personaje, o sea si bool = true.
        if (bool) {
            //Lo dañamos, ingresando el daño solicitado.
            setHealthDamage(health);
            dañarPersonajeProgresivamente = true;
        }
        //De lo contrario, si hay que dejar de dañarlo, lo dejamos de dañar.
        else {
            dañarPersonajeProgresivamente = false;
        }
    }

    @Override
    public abstract void render(float delta);

    public TextureAtlas getTextureAtlasCharac() {
        return textureAtlasCharac;
    }

    public abstract World getWorld();

    @Override
    public void resize(int width, int height) {
        mainViewport.update(width, height);
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
        world.dispose();
        box2DDebugRenderer.dispose();
        arrayListMegamanFireball.clear();
        arrayListMegamanRaycast.clear();
        textureAtlasTools.dispose();
        textureAtlasCharac.dispose();
    }

    //Este no es lo mismo que arriba, este molesta.
      public abstract TiledMap getTiledMap();

    //Dejo comentadas las funciones de abajo, ya que sirven si queremos utilizar controles en nuestro juego.
    //Controles de ps1, sin embargo, esto hace que ande mal la app.
/*
    //Aca manejamos los inputs de los botones. x cuad triang red l1 l2 start l3 r3 31 32 select, etc
    public boolean buttonDown(Controller controller, int buttonCode) {

        //Controller.getbutton 0 es triangulo.
        if (controller.getButton(0)) {
            //Hacemos un flash.
            if (controller.equals(controllers.get(0))) {
                if (megaman.isRunningRight()) {
                    megaman.body.setTransform(new Vector2(megaman.body.getPosition().x + 100 / MegamanMainClass.PixelsPerMeters, megaman.body.getPosition().y), megaman.body.getAngle());
                } else {
                    megaman.body.setTransform(new Vector2(megaman.body.getPosition().x - 100 / MegamanMainClass.PixelsPerMeters, megaman.body.getPosition().y), megaman.body.getAngle());
                }
            } else {
                if (zero.isRunningRight()) {
                    zero.body.setTransform(new Vector2(zero.body.getPosition().x + 100 / MegamanMainClass.PixelsPerMeters, zero.body.getPosition().y), zero.body.getAngle());
                } else {
                    zero.body.setTransform(new Vector2(zero.body.getPosition().x - 100 / MegamanMainClass.PixelsPerMeters, zero.body.getPosition().y), megaman.body.getAngle());
                }
            }
        }


        //Controller.getbutton(1) es redondo.
        if (controller.getButton(1)) {
            if (controller.equals(controllers.get(0))) {
                if (megaman.getState() == Megaman.State.CROUCHING) {
                    megaman.setState(Megaman.State.STANDING);
                    megaman.redefineMegaman();
                }
                //Si no estaba agachado, se para.
                else {
                    megaman.setState(Megaman.State.CROUCHING);
                    if (megaman.isRunningRight()) {
                        megaman.redefineMegamanCrouching(true);
                    } else {
                        megaman.redefineMegamanCrouching(false);
                    }
                }
            } else {
                if (zero.getState() == Zero.State.CROUCHING) {
                    zero.setState(Zero.State.STANDING);
                    zero.redefineZero();
                }
                //Si no estaba agachado, se para.
                else {
                    zero.setState(Zero.State.CROUCHING);
                    if (zero.isRunningRight()) {
                        zero.redefineZeroCrouching(true);
                    } else {
                        zero.redefineZeroCrouching(false);
                    }
                }
            }
        }


        //controller.getbutton(2) es equis.
        if (controller.getButton(2)) {
            if (controller.equals(controllers.get(0))) {
                //Si presionamos cuadrado, el personaje salta.
                //Solo salta si no estaba saltando o volando en el aire.
                if (!megaman.isMegamanJumping())
                    megaman.body.applyLinearImpulse(new Vector2(0, 9f), megaman.body.getWorldCenter(), true);
            } else {
                if (!zero.isZeroJumping())
                    zero.body.applyLinearImpulse(new Vector2(0, 10f), zero.body.getWorldCenter(), true);
            }
        }

        //controller.getButton(3) es cuadrado
        if (controller.getButton(3)) {
            if (controller.equals(controllers.get(0))) {
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
            } else {
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

    public boolean axisMoved(Controller controller, int axisCode, float value) {

        //Solo si nos estamos moviendo en el eje x.
        //Luego con controller.getAxis(axisCode), comprobamos que haya terminado completamente
        //el movimiento, por ej, JustTouched. Porque el analog tiene 2 sensores?
        if (axisCode == 1 && (controller.getAxis(axisCode) > 0.99 || controller.getAxis(axisCode) < -0.99)) {
            //Si estamos con el control 1.
            if (controller.equals(controllers.get(0))) {
                //Si el axiscode es 1, mover a la derecha.
                if (controller.getAxis(axisCode) > 0.99) {
                    //La logica es la siguiente: Si el personaje se estaba moviendo a la derecha, y ahora quiere ir a la izquierda,
                    //lo frenamos, en cambio si estaba frenado, lo dejamos correr al otro lado.
                    if (moverMegamanIzquierda) {
                        //Por ultimo, comprobamos que el personaje no este saltando, ya que sino se convierte
                        //en un movimiento feo.
                        if (megaman.isMegamanJumping()) {
                            moverMegamanIzquierda = false;
                            moverMegamanDerecha = true;
                        } else {
                            moverMegamanIzquierda = false;
                        }
                    } else {
                        moverMegamanDerecha = true;
                    }
                    //Dejo comentada la logica anterior por si la llego a necesitar(no creo).
                    //Solo Si la velocidad actual del personaje es menor a 3 aplicamos el impulso.
                    //   if (megaman.body.getLinearVelocity().x < 3)
                    //        megaman.body.applyLinearImpulse(new Vector2(2f, 0), megaman.body.getWorldCenter(), true);
                }
                //Sino, mover a la izquierda en el otro caso.
                else if (controller.getAxis(axisCode) < -0.99) {
                    if (moverMegamanDerecha) {
                        if (megaman.isMegamanJumping()) {
                            moverMegamanDerecha = false;
                            moverMegamanIzquierda = true;
                        } else {
                            moverMegamanDerecha = false;
                        }
                    } else {
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
                if (controller.getAxis(axisCode) > 0.99) {
                    if (moverZeroIzquierda) {
                        if (zero.isZeroJumping()) {
                            moverZeroIzquierda = false;
                            moverZeroDerecha = true;
                        } else {
                            moverZeroIzquierda = false;
                        }
                    } else {
                        moverZeroDerecha = true;
                    }
                    //Solo Si la velocidad actual del personaje es menor a 3 aplicamos el impulso.
                    //      if (zero.body.getLinearVelocity().x < 3)
                    //           zero.body.applyLinearImpulse(new Vector2(2f, 0), zero.body.getWorldCenter(), true);
                }
                //Sino, mover a la izquierda en el otro caso.
                else if (controller.getAxis(axisCode) < -0.99) {
                    if (moverZeroDerecha) {
                        if (zero.isZeroJumping()) {
                            moverZeroDerecha = false;
                            moverZeroIzquierda = true;
                        } else {
                            moverZeroDerecha = false;
                        }
                    } else {
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
*/
}
