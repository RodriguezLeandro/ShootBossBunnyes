package Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.megamangame.MegamanMainClass;

import java.util.Random;

import Screen.Level1Screen;
import Screen.MainGameScreen;

/**
 * Created by Leandro on 04/01/2017.
 */

public class Zero{

    public World world;
    public Body body;
    private Level1Screen level1Screen;
    private Megaman megaman;

    private Sprite sprite;

    public enum State {STANDING, WALKING, CROUCHING, FALLING, GETTINGHIT, JUMPING, HITTING, DYING};
    public State currentState;
    public State previousState;


    private TextureRegion zeroStand;
    private TextureRegion textureRegion;


    private Animation zeroWalking;
    private Animation zeroCrouching;
    private Animation zeroGettingHit;
    private Animation zeroJumping;
    private Animation zeroHitting;
    private Animation zeroDying;

    private float stateTimer;
    private float untouchableCount;

    private Integer fightingState;

    private boolean runningRight;
    private boolean shouldBeJumping;
    private boolean zeroIsDead;
    private boolean makeZeroUntouchable;
    private boolean isZeroFighting;
    private boolean zeroShouldJump;


    public Zero(Level1Screen level1Screen){

        //Supuestamente con esto seleccionamos la region de nuestro MegamanAndEnemies SpriteSheet.
        //En realidad, deberiamos agarrar la region solo del ninja?(No estaria funcionando?).
        sprite = new Sprite(level1Screen.getTextureAtlasCharac().findRegion("advnt_full"));

        //Obtenemos el maingamescreen.
        this.level1Screen = level1Screen;

        //Obtenemos el mundo en el que el enemigo principal vivira.
        world = level1Screen.getWorld();

        //Traemos la referencia de megaman.
        megaman = level1Screen.getMegaman();

        //Inicializamos los estados de nuestro personaje.
        currentState = State.STANDING;

        //Inicializamos tambien el estado previo.
        previousState = State.STANDING;

        //Inicializamos el timer del estado de las animaciones.
        stateTimer = 0;

        //Decimos que zero no esta muerto al iniciar el juego.
        zeroIsDead = false;

        //Decimos que cuando arranca el juego el personaje mira a la izquierda.
        //Nota: como es el enemigo, debe estar mirando siempre hacia donde esta nuestro mc(main character).
        runningRight = false;

        //Creamos las animaciones de el malvado personaje contrario.
        crearAnimaciones();

        //Definimos y creamos las caracteristicas del body del enemigo.
        defineZero();

        //Buscamos la textura inicial, en la que nuestro enemigo se encuentra en Standing.
        zeroStand = new TextureRegion(sprite.getTexture(),1,1,32,64);

        //Definimos la posicion inicial de nuestro personaje enemigo.
        //En realidad, en update esto deberia de sobreescribirse siempre, no deberia ser necesario.
        sprite.setBounds(0,0,64 / MegamanMainClass.PixelsPerMeters ,128 / MegamanMainClass.PixelsPerMeters);

        //Con el metodo setRegion se dibujara nuestro personaje.
        sprite.setRegion(zeroStand);

        //Establecemos el valor inicial del contador en 0.
        untouchableCount = 0;

        //Porque el personaje enemigo debe ser tocable.
        makeZeroUntouchable = false;

        //Al comienzo del juego zero no esta peleando.
        isZeroFighting = false;

        //Inicializamos el estado de pelea inicial.
        fightingState = 0;

    }

    public void update(float delta){

        //Zero esta peleando solo si nos encontramos en la seccion final del nivel.
        if (isZeroFighting){

            makeZeroFight();
            //Aqui actualizamos la posicion de nuestro enemigo principal, para que se ...
            //corresponda con la posicion del fixture(y body) de nuestro personaje.

            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2 + 7 / MegamanMainClass.PixelsPerMeters);

            //Tambien seleccionamos la textureregion que veremos en cada ciclo de renderizado.
            sprite.setRegion(getTextureRegion(delta));

            if (makeZeroUntouchable) {
                setZeroUntouchableDot5Seconds();
            }

        }
        else {
            //Aqui actualizamos la posicion de nuestro enemigo principal, para que se ...
            //corresponda con la posicion del fixture(y body) de nuestro personaje.

            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2 + 7 / MegamanMainClass.PixelsPerMeters);

            //Tambien seleccionamos la textureregion que veremos en cada ciclo de renderizado.
            sprite.setRegion(getTextureRegion(delta));

            if (makeZeroUntouchable) {
                setZeroUntouchableDot5Seconds();
            }

        }

    }

    public void makeZeroFight(){

        switch(fightingState){
            case 0:
                Random random = new Random();
                Integer randomNumer = random.nextInt(10);

                if (randomNumer == 0){
                    level1Screen.isZeroHitting();
                }
                else if (randomNumer == 1){
                    level1Screen.isZeroRunningToMegaman();
                }

                if (zeroShouldJump){
                    level1Screen.isZeroJumping();
                    zeroShouldJump = false;
                }
                break;

            case 1:
                Random random2 = new Random();
                Integer randomNumer2 = random2.nextInt(10);

                if (randomNumer2 == 0){
                    level1Screen.isZeroHitting();
                }
                else if(randomNumer2 == 1){
                    level1Screen.isZeroRunningAwayFromMegaman();
                }
                else if(randomNumer2 == 2){
                    level1Screen.isZeroJumping();
                }

                break;

            case 2:

                break;

        }

    }

    public void setZeroFightingStateNumber(Integer integer){
        fightingState = integer;
    }

    public void crearAnimaciones(){

        //Declaramos el array que contendra los frames para crear animaciones.
        Array<TextureRegion> textureRegionFrames = new Array<TextureRegion>();

        //Al array de textureregions lo llenamos con las imagenes correspondientes para...
        //asi crear las animaciones.
        for(int i = 1; i <7;i++){
            //Creamos objetos textureregions y los agregamos al arraylist.
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32, 1,32,64));
        }
        //Creamos la animacion y queda guardada en memoria.
        zeroWalking = new Animation(0.1f,textureRegionFrames);
        //Liberamos el textureregion para poder utilizarlo nuevamente.
        textureRegionFrames.clear();

        //Realizamos la misma operacion unas 4 veces mas(para cada animacion nueva).

        for (int i = 7; i < 10; i++){
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32, 1, 32, 64));
        }
        zeroCrouching = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 1; i < 3; i++){
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32, 65, 32, 64));
        }
        zeroGettingHit = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 7; i < 10; i++){
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32, 65, 32, 64));
        }
        zeroJumping = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 7; i < 10; i++){
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32 + 2, 129, 32, 64));
        }
        zeroHitting = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 1; i < 3; i++) {
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(), i * 32 - 4, 321, 38, 64));
        }
        zeroDying = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();
    }

    public void defineZero(){

        //Creamos el bodydef de nuestro personaje.
        BodyDef bodyDef = new BodyDef();

        //Establecemos la posicion que tendra nuestro personaje.
        bodyDef.position.set(13500 / MegamanMainClass.PixelsPerMeters ,200 / MegamanMainClass.PixelsPerMeters);
        //Decidimos si es StaticBody, DynamicBody o KinematicBody.
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //Creamos el cuerpo de nuestro personaje enemigo en nuestro mundo principal.
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

        //Agregamos el filtro de categoria(quien es nuestro personaje) de box2d.
        fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_BIT;

        //Agregamos el filtro de mascara(a quien puede colisionar nuestro personaje).
        fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT |
                MegamanMainClass.FLYINGGROUND_BIT | MegamanMainClass.FLOOR_BIT |
                MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

        //Creamos el fixture de nuestro body(con el fixturedef).
        body.createFixture(fixtureDef);

        //Cambiamos la posicion de nuestro circleshape(Esto es porque crearemos otro ...
        //fixture con el mismo fixtureshape).
        circleShape.setPosition(new Vector2(0,-32 / MegamanMainClass.PixelsPerMeters));

        //Creamos otro fixture asi nuestro body contiene dos circulos y es mas grande la colision.
        body.createFixture(fixtureDef);

        //Creamos un polygonShape para utilizarla como sensor al chocar aliados???.
        PolygonShape polygonShape = new PolygonShape();

        //Creamos una caja del tamaño de los dos circulos(body del personaje).
        //Al final no lo hacemos, preferimos crear un rectangulo customizado.
        //Igualmente dejamos comentado la manera de hacer la caja.
        //polygonShape.setAsBox(20 / MegamanMainClass.PixelsPerMeters,40 / MegamanMainClass.PixelsPerMeters);

        //Creamos un array de vector2 para crear un poligono con forma rectangular.

        Vector2[] vertices = new Vector2[4];

        //Creamos cada vector del array y les asignamos las correspondientes posiciones.
        vertices[0] = new Vector2(20f / MegamanMainClass.PixelsPerMeters,-53f / MegamanMainClass.PixelsPerMeters);
        vertices[1] = new Vector2(20f / MegamanMainClass.PixelsPerMeters,30f / MegamanMainClass.PixelsPerMeters);
        vertices[2] = new Vector2(-20f / MegamanMainClass.PixelsPerMeters ,30f / MegamanMainClass.PixelsPerMeters);
        vertices[3] = new Vector2(-20f / MegamanMainClass.PixelsPerMeters,-53f / MegamanMainClass.PixelsPerMeters);

        //Ponemos los vertices del array que conforman la forma poligonal.
        polygonShape.set(vertices);

        //Liberamos la memoria utilizada por el array.
        vertices = null;

        //Ponemos el shape en el fixturedef.
        fixtureDef.shape = polygonShape;

        //Decimos que nuestro shape es un sensor.
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_SENSOR_BIT;

        //Creamos nuestro sensor, y decimos que el fixture se llamara mainNinja(personajePrincipal).???Me parece que no. Lo lei despues esto.
        body.createFixture(fixtureDef).setUserData(this);

        vertices = new Vector2[4];

        //Creamos cada vector del array y les asignamos las correspondientes posiciones.
        vertices[0] = new Vector2(250f / MegamanMainClass.PixelsPerMeters,-53f / MegamanMainClass.PixelsPerMeters);
        vertices[1] = new Vector2(250f / MegamanMainClass.PixelsPerMeters,30f / MegamanMainClass.PixelsPerMeters);
        vertices[2] = new Vector2(-250f / MegamanMainClass.PixelsPerMeters ,30f / MegamanMainClass.PixelsPerMeters);
        vertices[3] = new Vector2(-250f / MegamanMainClass.PixelsPerMeters,-53f / MegamanMainClass.PixelsPerMeters);

        //Ponemos los vertices del array que conforman la forma poligonal.
        polygonShape.set(vertices);

        fixtureDef.shape = polygonShape;

        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_SENSOR_BIT_2;

        fixtureDef.filter.maskBits = MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

        body.createFixture(fixtureDef).setUserData(this);

    }

    //Funcion para modificar el estado de nuestro personaje desde pantalla principal.
    public void setState(State state){
        previousState = currentState;
        currentState = state;
    }

    public void setZeroShouldJump(){
        zeroShouldJump = true;
    }

    public TextureRegion getTextureRegion(float delta){

        //Obtenemos el estado actual de nuestro enemigo.
        currentState = getState();

        //Vemos en que estado estamos, y que haremos a partir de esta informacion.
        switch (currentState){
            case JUMPING:
                //Si no estaba saltando, reiniciamos el stateTimer.
                if (previousState != State.JUMPING) {
                    stateTimer = 0;
                }
                //Si esta saltando, prendemos la animacion de salto.
                textureRegion = zeroJumping.getKeyFrame(stateTimer);
                //Si termina la animacion de salto
                break;
            case WALKING:
                //Si no estaba caminando, reiniciamos el stateTimer.
                if (previousState != State.WALKING) {
                    stateTimer = 0;
                }
                //Si esta caminando, prendemos la animacion de caminar.
                //Notese que en el segundo parametro del metodo getkeyframe, decimos...
                //que cuando finalize la animacion, comienze de nuevo en un bucle.
                textureRegion = zeroWalking.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
                //Si no estaba cayendo, reiniciamos el stateTimer.
                if (previousState != State.FALLING) {
                    stateTimer = 0;
                }
                //Si esta callendo, mostramos la animacion de Standing.
                textureRegion = zeroStand;
                break;
            case STANDING:
                //Si esta en standing, mostramos la animacion de Standing.
                textureRegion = zeroStand;
                break;
            case GETTINGHIT:

                if (previousState == State.CROUCHING){
                    redefineZero();
                }
                //Si no estaba siendo golpeado, reiniciamos el stateTimer.
                if (previousState != State.GETTINGHIT) {
                    stateTimer = 0;
                    level1Screen.dañarZeroPersonaje();
                }
                //Si esta siendo lastimado, mostramos la animacion de IsGettingHit.
                textureRegion = zeroGettingHit.getKeyFrame(stateTimer);
                //Si la animacion de ser golpeado finaliza...
                if (zeroGettingHit.isAnimationFinished(stateTimer)){
                    shouldBeJumping = false;
                    //Y volvemos a parar a nuestro personaje con State.Standing.
                    currentState = State.STANDING;
                }
            break;
            //En el caso de Dying, luego vemos que hacemos(porque debe finalizar el juego);
            case DYING:

                //Si no estaba muriendo, reiniciamos el stateTimer.
                if (previousState != State.DYING) {
                    stateTimer = 0;
                }
                textureRegion = zeroDying.getKeyFrame(stateTimer);
                if (zeroDying.isAnimationFinished(stateTimer)){
                    //End of the game.
                }
                break;
            //Realizamos lo mismo con cada uno de los estados.
            case HITTING:
                if (previousState == State.CROUCHING){
                    redefineZero();
                }
                //Si no estaba pegando, reiniciamos el stateTimer.
                if (previousState != State.HITTING) {
                    stateTimer = 0;
                }
                textureRegion = zeroHitting.getKeyFrame(stateTimer);
                if (zeroHitting.isAnimationFinished(stateTimer)){
                    shouldBeJumping = false;
                    currentState = State.STANDING;
                }
                break;
            case CROUCHING:
                //Si no estaba agachandose, reiniciamos el stateTimer.
                if (previousState != State.CROUCHING) {
                    stateTimer = 0;
                }
                textureRegion = zeroCrouching.getKeyFrame(stateTimer);
                if (zeroCrouching.isAnimationFinished(stateTimer)){
                    shouldBeJumping = false;
                }
                break;
            default:
                textureRegion = zeroStand;
                break;
        }

        if ((body.getLinearVelocity().x < 0 && runningRight)||(body.getLinearVelocity().x > 0 && !runningRight)) {
            // Y si el personaje esta agachado, cambiamos la forma de la orientacion del cuerpo.
            if (currentState == State.CROUCHING) {
                changeBodyOrientation();
            }
        }

        //Verificamos para que lado esta mirando el personaje y si nuestra imagen ve para el mismo lado...
        //De no ser asi, damos vuelta las imagenes de nuestro animacion para que se vea en el lado correcto.
        if ((body.getLinearVelocity().x < 0 || !runningRight) && (!textureRegion.isFlipX())){
                textureRegion.flip(true, false);
                //Acordarse que si cambiamos la orientacion, hay que cambiar el booleano tambien.
                runningRight = false;
        }
        //Lo mismo que arriba.
        else if((body.getLinearVelocity().x > 0 || runningRight) &&(textureRegion.isFlipX())){
                textureRegion.flip(true, false);
                runningRight = true;
        }

        //Si no hemos cambiado de estado, entonces le agregamos tiempo a la animacion...
        //De lo contrario, establecemos el timer en 0.
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;

        //Devolvemos el textureRegion que corresponde.
        return textureRegion;
    }

    public boolean isRunningRight(){
        if (runningRight) {
            return true;
        }else {
            return false;
        }
    }

    public Vector2 getPositionFireAttack(){
        return new Vector2(body.getPosition().x + sprite.getWidth()/ 2,body.getPosition().y);
    }

    public void draw(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);
    }

    public void setZeroFighting(boolean bool){
        isZeroFighting = bool;
    }

    private void changeBodyOrientation(){

        //Recorremos todos los fixtures que conforman el cuerpo del personaje.
        //La idea es que a cada uno le cambiaremos la orientacion.

        for(Fixture fixture : body.getFixtureList()){

            if (fixture.getShape().getType() == Shape.Type.Polygon){

                //Si bien es un poligono, solo trabajaremos con cuadrados o rectangulos.
                //Y asi podremos cambiar la orientacion en los ejes importantes.

                Vector2[] vector2array = new Vector2[4];

                for (int i = 0; i < 4; i ++){
                    vector2array[i] = new Vector2();
                }

                for (int i = 0; i < 4; i ++){
                    ((PolygonShape) fixture.getShape()).getVertex(i,vector2array[i]);
                }

                //El problema es que al crearse el poligono cambia las posiciones.
                //Por el momento cambio el signo del eje x.

                vector2array[0].set(-vector2array[0].x,vector2array[0].y);

                vector2array[1].set(-vector2array[1].x,vector2array[1].y);

                vector2array[2].set(-vector2array[2].x,vector2array[2].y);

                vector2array[3].set(-vector2array[3].x,vector2array[3].y);

                ((PolygonShape) fixture.getShape()).set(vector2array);

                //Liberamos la memoria, importante.

                vector2array = null;

            }
            else if(fixture.getShape().getType() == Shape.Type.Circle){

                //Si es un circulo, le cambiamos la orientacion al fixture.
                //Poniendo el vector posicion del cuerpo pero de forma negativa.
                //Importante : solo cambiamos la orientacion en el eje x.

                ((CircleShape) fixture.getShape()).setPosition(new Vector2(- ((CircleShape) fixture.getShape()).getPosition().x,((CircleShape) fixture.getShape()).getPosition().y));
            }
            //Dejo el else por futuras actualizaciones, por si no es ni polygon ni circle.
            else {
            }
        }
    }
    public void redefineZeroCrouching(boolean orientation) {

        if (orientation) {
            Vector2 position = body.getPosition();

            Vector2 velocidad = body.getLinearVelocity();

            world.destroyBody(body);

            BodyDef bodyDef = new BodyDef();

            bodyDef.position.set(position);

            bodyDef.linearVelocity.set(velocidad);

            bodyDef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();

            CircleShape circleShape = new CircleShape();

            circleShape.setRadius(8 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(14 / MegamanMainClass.PixelsPerMeters, 2 / MegamanMainClass.PixelsPerMeters));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                    | MegamanMainClass.FLYINGGROUND_BIT | MegamanMainClass.FLOOR_BIT |
                    MegamanMainClass.MEGAMAN_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

            body.createFixture(fixtureDef);

            circleShape.setRadius(20 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(10 / MegamanMainClass.PixelsPerMeters, -32 / MegamanMainClass.PixelsPerMeters));

            body.createFixture(fixtureDef);

            PolygonShape polygonShape = new PolygonShape();

            Vector2[] vertices = new Vector2[4];

            vertices[0] = new Vector2(30f / MegamanMainClass.PixelsPerMeters, -53f / MegamanMainClass.PixelsPerMeters);
            vertices[1] = new Vector2(30f / MegamanMainClass.PixelsPerMeters, 12f / MegamanMainClass.PixelsPerMeters);
            vertices[2] = new Vector2(-10f / MegamanMainClass.PixelsPerMeters, 12f / MegamanMainClass.PixelsPerMeters);
            vertices[3] = new Vector2(-10f / MegamanMainClass.PixelsPerMeters, -53f / MegamanMainClass.PixelsPerMeters);

            polygonShape.set(vertices);

            vertices = null;

            fixtureDef.shape = polygonShape;

            fixtureDef.isSensor = true;

            fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_SENSOR_BIT;

            body.createFixture(fixtureDef).setUserData(this);
        }
        else {
            Vector2 position = body.getPosition();

            Vector2 velocidad = body.getLinearVelocity();

            world.destroyBody(body);

            BodyDef bodyDef = new BodyDef();

            bodyDef.position.set(position);

            bodyDef.linearVelocity.set(velocidad);

            bodyDef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();

            CircleShape circleShape = new CircleShape();

            circleShape.setRadius(8 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(- 14 / MegamanMainClass.PixelsPerMeters, 2 / MegamanMainClass.PixelsPerMeters));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                    | MegamanMainClass.FLYINGGROUND_BIT | MegamanMainClass.FLOOR_BIT |
                    MegamanMainClass.MEGAMAN_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

            body.createFixture(fixtureDef);

            circleShape.setRadius(20 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(- 10 / MegamanMainClass.PixelsPerMeters, -32 / MegamanMainClass.PixelsPerMeters));

            body.createFixture(fixtureDef);

            PolygonShape polygonShape = new PolygonShape();

            Vector2[] vertices = new Vector2[4];

            vertices[0] = new Vector2(- 30f / MegamanMainClass.PixelsPerMeters, -53f / MegamanMainClass.PixelsPerMeters);
            vertices[1] = new Vector2(- 30f / MegamanMainClass.PixelsPerMeters, 12f / MegamanMainClass.PixelsPerMeters);
            vertices[2] = new Vector2(10f / MegamanMainClass.PixelsPerMeters, 12f / MegamanMainClass.PixelsPerMeters);
            vertices[3] = new Vector2(10f / MegamanMainClass.PixelsPerMeters, -53f / MegamanMainClass.PixelsPerMeters);

            polygonShape.set(vertices);

            vertices = null;

            fixtureDef.shape = polygonShape;

            fixtureDef.isSensor = true;

            fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_SENSOR_BIT;

            body.createFixture(fixtureDef).setUserData(this);
        }
    }

    public void setZeroUntouchableDot5Seconds(){

        if(untouchableCount < 1.5f){
            untouchableCount += level1Screen.getDeltaTime();
            makeZeroUntouchable = true;
        }
        else {
            untouchableCount = 0;
            makeZeroUntouchable = false;
        }
    }

    public void onBodyHit() {
        //Si no hay que pegarle no le pegamos, de lo contrario lo hacemos.
        if (makeZeroUntouchable) {

        }else {
            setState(State.GETTINGHIT);
        }
    }

    public void redefineZero(){

        Vector2 vector2 = body.getPosition();

        Vector2 velocidad = body.getLinearVelocity();

        world.destroyBody(body);

        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(vector2);

        bodyDef.linearVelocity.set(velocidad);

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape circleShape = new CircleShape();

        circleShape.setRadius(20 / MegamanMainClass.PixelsPerMeters);

        circleShape.setPosition(new Vector2(0, 10 / MegamanMainClass.PixelsPerMeters ));

        fixtureDef.shape = circleShape;

        fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                | MegamanMainClass.FLYINGGROUND_BIT | MegamanMainClass.FLOOR_BIT |
                MegamanMainClass.MEGAMAN_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_MEGAMAN_SENSOR_BIT;

        body.createFixture(fixtureDef);

        circleShape.setPosition(new Vector2(0,-32 / MegamanMainClass.PixelsPerMeters));

        body.createFixture(fixtureDef);

        PolygonShape polygonShape = new PolygonShape();

        Vector2[] vertices = new Vector2[4];

        vertices[0] = new Vector2(20f / MegamanMainClass.PixelsPerMeters,-53f / MegamanMainClass.PixelsPerMeters);
        vertices[1] = new Vector2(20f / MegamanMainClass.PixelsPerMeters,30f / MegamanMainClass.PixelsPerMeters);
        vertices[2] = new Vector2(-20f / MegamanMainClass.PixelsPerMeters ,30f / MegamanMainClass.PixelsPerMeters);
        vertices[3] = new Vector2(-20f / MegamanMainClass.PixelsPerMeters,-53f / MegamanMainClass.PixelsPerMeters);

        polygonShape.set(vertices);

        vertices = null;

        fixtureDef.shape = polygonShape;

        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = MegamanMainClass.ZERO_SENSOR_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }
    public Boolean isDead(){
        if (zeroIsDead){
            return true;
        }
        else {
            return false;
        }
    }

    //Funcion que devuelve el estado de nuestro personaje.
    public State getState(){

        if (currentState == State.CROUCHING){
            return State.CROUCHING;
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
        else if ((body.getLinearVelocity().y > 0 && shouldBeJumping) || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        //Si el jugador cae, devolvemos State.Falling.
        else if (body.getLinearVelocity().y < 0){
            shouldBeJumping = true;
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

    public boolean isZeroJumping(){
        if (body.getLinearVelocity().y != 0){
            return true;
        }else{
            return false;
        }
    }
}
