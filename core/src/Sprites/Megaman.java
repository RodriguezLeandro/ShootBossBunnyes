package Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 02/01/2017.
 */

public class Megaman extends Sprite {

    public World world;
    public Body body;
    public enum State {STANDING, WALKING, CROUCHING, FALLING, GETTINGHIT, JUMPING, HITTING, DYING};
    public State currentState;
    public State previousState;

    private TextureRegion megamanStand;
    private TextureRegion textureRegion;

    private Animation megamanWalking;
    private Animation megamanCrouching;
    private Animation megamanGettingHit;
    private Animation megamanJumping;
    private Animation megamanHitting;
    private Animation megamanDying;

    private float stateTimer;
    private boolean runningRight;
    public boolean isCrouching;
    public boolean isGettingHit;
    public boolean isHitting;
    public boolean isDying;


    public Megaman(World world, MainGameScreen mainGameScreen){

        //Supuestamente con esto seleccionamos la region de nuestro MegamanAndEnemies SpriteSheet.
        //En realidad, deberiamos agarrar la region solo del ninja?(No estaria funcionando?).
        super(mainGameScreen.getTextureAtlas().findRegion("ninja_full"));
        //Obtenemos el mundo en el que el personaje principal vivira.
        this.world = world;

        //Inicializamos los estados de nuestro personaje.
        currentState = State.STANDING;

        //Inicializamos tambien el estado previo.
        previousState = State.STANDING;

        //Inicializamos el timer del estado de las animaciones.
        stateTimer = 0;

        //Decimos que cuando arranca el juego el personaje mira a la derecha.
        runningRight = true;

        //Cuando arranca el juego el personaje no esta sentado.
        isCrouching = false;

        //Cuando arranca el juego el personaje no esta siendo lastimado.
        isGettingHit = false;

        //Cuando arranca el juego el personaje no esta pegando.
        isHitting = false;

        //Cuando arranca el juego el personaje no esta muriendo.
        isDying = false;

        //Creamos las animaciones de nuestro personaje y las tenemos en memoria.
        crearAnimaciones();

        //Definimos y creamos las caracteristicas del body de nuestro personaje.
        defineMegaman();

        //Buscamos la textura inicial, en la que nuestro personaje se encuentra en Standing.
        megamanStand = new TextureRegion(getTexture(),324,1,32,64);

        //Definimos la posicion inicial de nuestro personaje.
        //En realidad, en update esto deberia de sobreescribirse siempre, no deberia ser necesario.
        setBounds(0,0,64 / MegamanMainClass.PixelsPerMeters ,128 / MegamanMainClass.PixelsPerMeters);

        //Con el metodo setRegion se dibujara nuestro personaje.
        setRegion(megamanStand);
    }

    public void crearAnimaciones(){
        //Declaramos el array que contendra los frames para crear animaciones.
        Array<TextureRegion> textureRegionFrames = new Array<TextureRegion>();

        //Al array de textureregions lo llenamos con las imagenes correspondientes para...
        //asi crear las animaciones.
        for(int i = 11; i <17;i++){
            //Creamos objetos textureregions y los agregamos al arraylist.
            textureRegionFrames.add(new TextureRegion(getTexture(),i * 32, 1,32,64));
        }
        //Creamos la animacion y queda guardada en memoria.
        megamanWalking = new Animation(0.1f,textureRegionFrames);
        //Liberamos el textureregion para poder utilizarlo nuevamente.
        textureRegionFrames.clear();

        //Realizamos la misma operacion unas 4 veces mas(para cada animacion nueva).

        for (int i = 17; i < 20; i++){
            textureRegionFrames.add(new TextureRegion(getTexture(),i * 32, 1, 32, 64));
        }
        megamanCrouching = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 10; i < 13; i++){
            textureRegionFrames.add(new TextureRegion(getTexture(),i * 32, 65, 32, 64));
        }
        megamanGettingHit = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 16; i < 20; i++){
            textureRegionFrames.add(new TextureRegion(getTexture(),i * 32, 65, 32, 64));
        }
        megamanJumping = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 17; i < 20; i++){
            textureRegionFrames.add(new TextureRegion(getTexture(),i * 32 + 4, 129, 32, 64));
        }
        megamanHitting = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 11; i < 13; i++){
            textureRegionFrames.add(new TextureRegion(getTexture(),i * 32, 321, 32, 64));
        }
        //Aqui agregamos manualmente una textureRegion separada de las demas.
        textureRegionFrames.add(new TextureRegion(getTexture(),14 * 32 , 321, 32,64 ));
        megamanDying = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();
    }

    public void update(float delta){

        //Aqui actualizamos la posicion de nuestro personaje principal, para que se ...
        //corresponda con la posicion del fixture(y body) de nuestro personaje.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 + 7 / MegamanMainClass.PixelsPerMeters);

        //Tambien seleccionamos la textureregion que veremos en cada ciclo de renderizado.
        setRegion(getTextureRegion(delta));
    }

    public TextureRegion getTextureRegion(float delta){

        //Obtenemos el estado actual de nuestro personaje.
        currentState = getState();

        //Vemos en que estado estamos, y que haremos a partir de esta informacion.
        switch (currentState){
            case JUMPING:
                //Si esta saltando, prendemos la animacion de salto.
                textureRegion = megamanJumping.getKeyFrame(stateTimer);
                break;
            case WALKING:
                //Si esta caminando, prendemos la animacion de caminar.
                //Notese que en el segundo parametro del metodo getkeyframe, decimos...
                //que cuando finalize la animacion, comienze de nuevo en un bucle.
                textureRegion = megamanWalking.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
                //Si esta callendo, mostramos la animacion de Standing.
                textureRegion = megamanStand;
                break;
            //Si esta en standing, mostramos la animacion de Standing.
            case STANDING:
                textureRegion = megamanStand;
                break;
            case GETTINGHIT:
                //Si esta siendo lastimado, mostramos la animacion de IsGettingHit.
                textureRegion = megamanGettingHit.getKeyFrame(stateTimer);
                //Si la animacion de ser golpeado finaliza...
                if (megamanGettingHit.isAnimationFinished(stateTimer)){
                    //Decimos que ya no esta siendo golpeado nuestro personaje.
                    isGettingHit = false;
                    //Establecemos el estado anterior como siendo golpeado.
                    previousState = State.GETTINGHIT;
                    //Y volvemos a parar a nuestro personaje con State.Standing.
                    currentState = State.STANDING;
                }
                break;
            //En el caso de Dying, luego vemos que hacemos(porque debe finalizar el juego);
            case DYING:
                textureRegion = megamanDying.getKeyFrame(stateTimer);
                if (megamanDying.isAnimationFinished(stateTimer)){
                    //End of the game.
                }
                break;
            //Realizamos lo mismo con cada uno de los estados.
            case HITTING:
                textureRegion = megamanHitting.getKeyFrame(stateTimer);
                if (megamanHitting.isAnimationFinished(stateTimer)){
                    isHitting = false;
                    previousState = State.HITTING;
                    currentState = State.STANDING;
                }
                break;
            case CROUCHING:
                textureRegion = megamanCrouching.getKeyFrame(stateTimer);
                if (megamanCrouching.isAnimationFinished(stateTimer)){
                    isCrouching = false;
                    previousState = State.CROUCHING;
                    currentState = State.STANDING;
                }
                break;
            default:
                textureRegion = megamanStand;
                break;

        }

        //Verificamos para que lado esta mirando el personaje y si nuestra imagen ve para el mismo lado...
        //De no ser asi, damos vuelta las imagenes de nuestro animacion para que se vea en el lado correcto.
        if ((body.getLinearVelocity().x < 0 || !runningRight) && (!textureRegion.isFlipX())){
            textureRegion.flip(true,false);
            //Acordarse que si cambiamos la orientacion, hay que cambiar el booleano tambien.
            runningRight = false;
        }
        //Lo mismo que arriba.
        else if((body.getLinearVelocity().x > 0 || runningRight) &&(textureRegion.isFlipX())){
            textureRegion.flip(true,false);
            runningRight = true;
        }

        //Si no hemos cambiado de estado, entonces le agregamos tiempo a la animacion...
        //De lo contrario, establecemos el timer en 0.
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;

        //Devolvemos el textureRegion que corresponde.
        return textureRegion;
    }

    //Funcion para modificar el estado de nuestro personaje desde pantalla principal.
    public void setState(State state){
        previousState = currentState;
        currentState = state;
    }

    //Funcion que devuelve el estado de nuestro personaje.
    public State getState(){

        if (currentState == State.CROUCHING){
            return  State.CROUCHING;
        }
        else if(currentState == State.GETTINGHIT){
            return State.GETTINGHIT;
        }
        else if (currentState == State.HITTING){
            return State.HITTING;
        }
        else if (currentState == State.DYING){
            return State.DYING;
        }

        //Si el jugador estaba saltando, aunque luego caiga devolvemos el State.Jumping.
        if ((body.getLinearVelocity().y > 0) || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        //Si el jugador cae, devolvemos State.Falling.
        else if (body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        //Si el jugador camina, devolvemos State.Walking
        else if (body.getLinearVelocity().x != 0) {
            return State.WALKING;
        }
        //Si el jugador esta parado, devolvemos State.Standing.
        else{
            return State.STANDING;
        }

    }

    public void defineMegaman(){

        //Creamos el bodydef de nuestro personaje.
        BodyDef bodyDef = new BodyDef();

        //Establecemos la posicion que tendra nuestro personaje.
        bodyDef.position.set(32 / MegamanMainClass.PixelsPerMeters ,200 / MegamanMainClass.PixelsPerMeters);
        //Decidimos si es StaticBody, DynamicBody o KinematicBody.
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //Creamos el cuerpo de nuestro personaje en nuestro mundo principal.
        body = world.createBody(bodyDef);

        //Creamos el fixtureDef de nuestro fixture.
        FixtureDef fixtureDef = new FixtureDef();

        //Creamos el Shape que utilizara nuestro fixturedef.(Polygonshape, Circleshape).
        CircleShape circleShape = new CircleShape();

        //Establecemos el radio de nuestro circulo.
        circleShape.setRadius(20 / MegamanMainClass.PixelsPerMeters);

        //Establecemos la posicion inicial de nuestro circulo.
        circleShape.setPosition(new Vector2(0, 10 / MegamanMainClass.PixelsPerMeters ));

        //Agregamos el shape al fixturedef.
        fixtureDef.shape = circleShape;

        //Creamos el fixture de nuestro body(con el fixturedef).
        body.createFixture(fixtureDef);

        //Cambiamos la posicion de nuestro circleshape(Esto es porque crearemos otro ...
        //fixture con el mismo fixtureshape).
        circleShape.setPosition(new Vector2(0,-32 / MegamanMainClass.PixelsPerMeters));

        //Creamos otro fixture asi nuestro body contiene dos circulos y es mas grande la colision.
        body.createFixture(fixtureDef);

    }

}
