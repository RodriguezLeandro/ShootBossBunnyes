package Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

import Screen.MainGameScreen;

/**
 * Created by Leandro on 02/01/2017.
 */

public class Megaman{

    public World world;
    public Body body;
    public enum State {STANDING, WALKING, CROUCHING, FALLING, GETTINGHIT, JUMPING, HITTING, DYING, SLASHING, SLIDING};
    public State currentState;
    public State previousState;

    private Sprite sprite;

    private MainGameScreen mainGameScreen;

    private TextureRegion megamanStand;
    private TextureRegion textureRegion;
    private TextureRegion megamanSlashing;
    private TextureRegion megamanSliding;

    private Animation megamanWalking;
    private Animation megamanCrouching;
    private Animation megamanGettingHit;
    private Animation megamanJumping;
    private Animation megamanHitting;
    private Animation megamanDying;

    private float stateTimer;
    private float megamanDamageCuantity;

    private boolean runningRight;
    private boolean shouldBeJumping;
    private boolean megamanIsDead;
    private boolean megamanWasDead;

    public Megaman(MainGameScreen mainGameScreen){

        //Obtenemos el maingamescreen para luego utilizarlo.
        this.mainGameScreen = mainGameScreen;

        //Supuestamente con esto seleccionamos la region de nuestro MegamanAndEnemies SpriteSheet.
        //En realidad, deberiamos agarrar la region solo del ninja?(No estaria funcionando?).
        sprite = new Sprite(mainGameScreen.getTextureAtlasCharac().findRegion("advnt_full"));

        //Obtenemos el mundo en el que el personaje principal vivira.
        world = mainGameScreen.getWorld();

        //Inicializamos los estados de nuestro personaje.
        currentState = State.STANDING;

        //Inicializamos tambien el estado previo.
        previousState = State.STANDING;

        //Inicializamos el timer del estado de las animaciones.
        stateTimer = 0;

        //Decimos que cuando arranca el juego el personaje mira a la derecha.
        runningRight = true;

        //Decimos que megaman esta vivo al comienzo del juego.
        megamanIsDead = false;

        //Decimos que megaman no estaba muerto.
        megamanWasDead = false;

        //Creamos las animaciones de nuestro personaje y las tenemos en memoria? O referenciadas?.
        crearAnimaciones();

        //Definimos y creamos las caracteristicas del body de nuestro personaje.
        defineMegaman();

        //Buscamos la textura inicial, en la que nuestro personaje se encuentra en Standing.
        megamanStand = new TextureRegion(sprite.getTexture(),324,1,32,64);

        //Creamos el textureregion de megamanSlashing.
        megamanSlashing = new TextureRegion(sprite.getTexture(),354,68,32,64);

        //Creamos el textureregion de megamanSliding.
        megamanSliding = new TextureRegion(sprite.getTexture(),354,196,32,64);

        //Definimos la posicion inicial de nuestro personaje.
        //En realidad, en update esto deberia de sobreescribirse siempre, no deberia ser necesario.
        sprite.setBounds(0,0,64 / MegamanMainClass.PixelsPerMeters ,128 / MegamanMainClass.PixelsPerMeters);

        //Con el metodo setRegion se dibujara nuestro personaje.
        sprite.setRegion(megamanStand);

        //El daño default que se le hara a megaman por golpe es :
        megamanDamageCuantity = 10;

    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void crearAnimaciones(){
        //Declaramos el array que contendra los frames para crear animaciones.
        Array<TextureRegion> textureRegionFrames = new Array<TextureRegion>();

        //Al array de textureregions lo llenamos con las imagenes correspondientes para...
        //asi crear las animaciones.
        for(int i = 11; i <17;i++){
            //Creamos objetos textureregions y los agregamos al arraylist.
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32, 1,32,64));
        }
        //Creamos la animacion y queda guardada en memoria.
        megamanWalking = new Animation(0.1f,textureRegionFrames);
        //Liberamos el textureregion para poder utilizarlo nuevamente.
        textureRegionFrames.clear();

        //Realizamos la misma operacion unas 4 veces mas(para cada animacion nueva).

        for (int i = 17; i < 20; i++){
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32, 1, 32, 64));
        }
        megamanCrouching = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 10; i < 13; i++){
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32, 68, 32, 64));
        }
        megamanGettingHit = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 17; i < 20; i++){
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32, 68, 32, 64));
        }
        megamanJumping = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 17; i < 20; i++){
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(),i * 32 + 4, 132, 32, 64));
        }
        megamanHitting = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

        for (int i = 11; i < 13; i++) {
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(), i * 32, 321, 38, 64));
            textureRegionFrames.add(new TextureRegion(sprite.getTexture(), i * 32, 321, 38, 64));
        }
        megamanDying = new Animation(0.1f,textureRegionFrames);
        textureRegionFrames.clear();

    }

    public void update(float delta){

        if (!megamanIsDead) {

            //Aqui actualizamos la posicion de nuestro personaje principal, para que se ...
            //corresponda con la posicion del fixture(y body) de nuestro personaje.
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2 + 7 / MegamanMainClass.PixelsPerMeters);

            //Tambien seleccionamos la textureregion que veremos en cada ciclo de renderizado.
            sprite.setRegion(getTextureRegion(delta));

            //Verificamos que el personaje no este muerto, y si lo esta, llamamos a la funcion.
            //Tambien verificamos que no estuviera muerto anteriormente, con la finalidad.
            //de que el audio suene solo una vez.
            if (megamanIsDead && !megamanWasDead) {
                MegamanMainClass.assetManager.get("audio/fall_death.wav", Sound.class).play();
                megamanWasDead = true;
                stateTimer = 0;
            }
        }else {
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2 + 7 / MegamanMainClass.PixelsPerMeters);
            sprite.setRegion(getTextureRegion(delta));
            stateTimer += delta;
        }

    }

    public void setLinearDamping(float linearDamping){
        body.setLinearDamping(linearDamping);
    }
    public void redefineMegamanCrouching(boolean rightOrientation) {

        if (rightOrientation) {

            //Esto es para guardar la posicion que tenia el cuerpo.
            Vector2 position = body.getPosition();

            //Esto es para guardar la velocidad que tenia el cuerpo.
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

            fixtureDef.filter.categoryBits = MegamanMainClass.MEGAMAN_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                    | MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT;

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

            fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                    | MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT |
                    MegamanMainClass.ZERO_SENSOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT
                    | MegamanMainClass.ENEMY_BIT;

            fixtureDef.filter.categoryBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

            body.createFixture(fixtureDef).setUserData(this);
        }
        else{
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

            circleShape.setPosition(new Vector2(-14 / MegamanMainClass.PixelsPerMeters, 2 / MegamanMainClass.PixelsPerMeters));

            fixtureDef.shape = circleShape;

            fixtureDef.filter.categoryBits = MegamanMainClass.MEGAMAN_BIT;

            fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                    | MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT |
                    MegamanMainClass.ZERO_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.ENEMY_BIT;

            body.createFixture(fixtureDef);

            circleShape.setRadius(20 / MegamanMainClass.PixelsPerMeters);

            circleShape.setPosition(new Vector2(-10 / MegamanMainClass.PixelsPerMeters, -32 / MegamanMainClass.PixelsPerMeters));

            body.createFixture(fixtureDef);

            PolygonShape polygonShape = new PolygonShape();

            Vector2[] vertices = new Vector2[4];

            vertices[0] = new Vector2(-30f / MegamanMainClass.PixelsPerMeters, -53f / MegamanMainClass.PixelsPerMeters);
            vertices[1] = new Vector2(-30f / MegamanMainClass.PixelsPerMeters, 12f / MegamanMainClass.PixelsPerMeters);
            vertices[2] = new Vector2(10f / MegamanMainClass.PixelsPerMeters, 12f / MegamanMainClass.PixelsPerMeters);
            vertices[3] = new Vector2(10f / MegamanMainClass.PixelsPerMeters, -53f / MegamanMainClass.PixelsPerMeters);

            polygonShape.set(vertices);

            vertices = null;

            fixtureDef.shape = polygonShape;

            fixtureDef.isSensor = true;

            fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                    | MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT |
                    MegamanMainClass.ZERO_SENSOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT
                    | MegamanMainClass.ENEMY_BIT;

            fixtureDef.filter.categoryBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

            body.createFixture(fixtureDef).setUserData(this);
        }
    }

    public void redefineMegaman(){

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

        fixtureDef.filter.categoryBits = MegamanMainClass.MEGAMAN_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                | MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT;

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

        fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                | MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT |
                MegamanMainClass.ZERO_SENSOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT
                | MegamanMainClass.ENEMY_BIT;

        fixtureDef.filter.categoryBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public TextureRegion getTextureRegion(float delta){

        //Obtenemos el estado actual de nuestro personaje.
        currentState = getState();

        //Vemos en que estado estamos, y que haremos a partir de esta informacion.
        switch (currentState){
            case JUMPING:
                //Si no estaba saltando, reiniciamos el stateTimer.
                if (previousState != State.JUMPING) {
                    stateTimer = 0;
                }
                //Si esta saltando, prendemos la animacion de salto.
                textureRegion = megamanJumping.getKeyFrame(stateTimer);
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
                textureRegion = megamanWalking.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
                //Si no estaba cayendo, reiniciamos el stateTimer.
                if (previousState != State.FALLING) {
                    stateTimer = 0;
                }
                //Si esta callendo, mostramos la animacion de Standing.
                textureRegion = megamanStand;
                break;
            case SLIDING:
                //Si estaba agachado, hay que cambiar la forma del cuerpo.
                if (previousState == State.CROUCHING){
                    redefineMegaman();
                }
                if (previousState != State.SLIDING){
                    stateTimer = 0;
                    //El problema con cargar la textura asi, es que siempre se carga mirando
                    //para la derecha, ahora mismo lo solucionamos.
                    //Ocurre exactamente lo mismo cuando utilizamos la textura de megamanStand.
                    //Solo que no nos damos cuenta, ya que siempre esta en la misma posicion.
                    //Con slashing pasa lo mismo?
                    //Si, pasa lo mismo, solo que controlo la posicion en el input.
                    textureRegion = megamanSliding;

                    if ((runningRight && textureRegion.isFlipX())||(!runningRight && !textureRegion.isFlipX())){
                        textureRegion.flip(true,false);
                    }
                }

                break;
            //Nota, siempre y cuando no modifique manualmente el sentido del personaje(megaman),
            //entonces no me tengo que preocupar de si esta viendo hacia la derecha o hacia la izquierda.
            case SLASHING:
                //Si estaba agachado, hay que cambiar la forma del cuerpo.
                if (previousState == State.CROUCHING){
                    redefineMegaman();
                }
                if (previousState != State.SLASHING){
                    stateTimer = 0;
                }
                if (stateTimer < 1.5f) {
                    textureRegion = megamanSlashing;
                }
                else {
                    setState(State.STANDING);
                    textureRegion = megamanStand;
                    stateTimer = 0;
                }
                break;
            case STANDING:
                //Si esta en standing, mostramos la animacion de Standing.
                textureRegion = megamanStand;

                break;
            case GETTINGHIT:
                if (previousState == State.CROUCHING){
                    redefineMegaman();
                }
                //Si no estaba siendo golpeado, reiniciamos el stateTimer.
                if (previousState != State.GETTINGHIT) {
                    stateTimer = 0;
                    mainGameScreen.dañarMegamanPersonaje(megamanDamageCuantity);
                    //Volvemos a poner el daño en default.
                    megamanDamageCuantity = 10;
                }
                //Si esta siendo lastimado, mostramos la animacion de IsGettingHit.
                textureRegion = megamanGettingHit.getKeyFrame(stateTimer);
                //Si la animacion de ser golpeado finaliza...
                if (megamanGettingHit.isAnimationFinished(stateTimer)){
                    shouldBeJumping = false;
                    //Y volvemos a parar a nuestro personaje con State.Standing.
                    currentState = State.STANDING;
                }
                break;
            //En el caso de Dying, luego vemos que hacemos(porque debe finalizar el juego);
            case DYING:
                //Si no estaba muriendo, reiniciamos el stateTimer.
                if (currentState == State.DYING && megamanIsDead == false) {
                    stateTimer = 0;
                    megamanIsDead = true;
                    MegamanMainClass.assetManager.get("audio/topman.mp3", Music.class).stop();
                    mainGameScreen.setZeroFightState(1);
                }
                textureRegion = megamanDying.getKeyFrame(stateTimer);
                break;
            //Realizamos lo mismo con cada uno de los estados.
            case HITTING:
                //Si estaba agachado, hay que cambiar la forma del cuerpo.
                if (previousState == State.CROUCHING){
                    redefineMegaman();
                }
                //Si no estaba pegando, reiniciamos el stateTimer.
                if (previousState != State.HITTING) {
                    stateTimer = 0;
                }
                textureRegion = megamanHitting.getKeyFrame(stateTimer);
                if (megamanHitting.isAnimationFinished(stateTimer)){
                    shouldBeJumping = false;
                    currentState = State.STANDING;
                }
                break;
            case CROUCHING:
                //Si no estaba agachandose, reiniciamos el stateTimer.
                if (previousState != State.CROUCHING) {
                    stateTimer = 0;
                }
                textureRegion = megamanCrouching.getKeyFrame(stateTimer);
                if (megamanCrouching.isAnimationFinished(stateTimer)){
                    shouldBeJumping = false;
                }
                break;
            default:
                textureRegion = megamanStand;
                break;
        }


        //Nota: podriamos combinar esta seccion de codigo con la de abajo, la de cambiar
        //la orientacion del spritesheet, pero para no complicar visualmente el codigo, no lo hacemos.
        //Si cambia la orientacion del cuerpo.
        if ((body.getLinearVelocity().x < 0 && runningRight)||(body.getLinearVelocity().x > 0 && !runningRight)) {
            // Y si el personaje esta agachado, cambiamos la forma de la orientacion del cuerpo.
            if (currentState == State.CROUCHING) {
                changeBodyOrientation();
            }
        }

        //Verificamos para que lado esta mirando el personaje y si nuestra imagen ve para el mismo lado...
        //De no ser asi, damos vuelta las imagenes de nuestro animacion para que se vea en el lado correcto.
        if ((body.getLinearVelocity().x < 0 || !runningRight) && (!textureRegion.isFlipX())){
            //Tenemos que agregar este condicional debido a que no queremos que cuando megaman este deslizandose,
            //pueda cambiar de orientacion.
            //Cuando realizo manualmente la posicion del sprite es mas dificil.
            if (currentState != State.SLIDING) {
                textureRegion.flip(true, false);
                //Acordarse que si cambiamos la orientacion, hay que cambiar el booleano tambien.
                runningRight = false;
            }
        }
        //Lo mismo que arriba.
        else if((body.getLinearVelocity().x > 0 || runningRight) &&(textureRegion.isFlipX())){
            if (currentState != State.SLIDING) {
                textureRegion.flip(true, false);
                runningRight = true;
            }
        }

        //Si no hemos cambiado de estado, entonces le agregamos tiempo a la animacion...
        //De lo contrario, establecemos el timer en 0.
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;

        //Devolvemos el textureRegion que corresponde.
        return textureRegion;
    }

    public void draw(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);
    }

    public void setRunningRight(boolean bool){
        //Tengo que preguntar si bool es verdadero o falso, y si tengo que cambiar la posicion
        //del sprite o no.
        if (bool) {
            if (runningRight) {
                //Si megaman tiene que mirar a la derecha y esta mirando a la derecha, no hacemos nada.
                //No hacemos nada X2.
            } else {
                //Si megaman tiene que mirar a la derecha y esta mirando a la izquierda, cumplimos nuestro deber.
                runningRight = true;
                textureRegion.flip(true, false);
            }
        }
        else{
            if (runningRight){
                //De lo contrario, si el personaje tiene que mirar a la izquierda y esta mirando a la derecha...
                runningRight = false;
                textureRegion.flip(true, false);
            }
            else{
                //No hacemos nada, recordar luego borrar estos 2 elses innecesarios.
            }
        }

    }

    public boolean isRunningRight(){
        if (runningRight) {
            return true;
        }else {
            return false;
        }
    }

    public void setMegamanDamageCuantity(float damage){
        megamanDamageCuantity = damage;
    }

    public void setMegamanIsDead(){
        currentState = State.DYING;
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

    //Funcion para modificar el estado de nuestro personaje desde pantalla principal.
    public void setState(State state){
        previousState = currentState;
        currentState = state;
    }

    public Boolean isDead(){
        if (megamanIsDead){
            return true;
        }
        else {
            return false;
        }
    }

    public Vector2 getPositionFireAttack(){
        return new Vector2(body.getPosition().x + sprite.getWidth()/ 2,body.getPosition().y);
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
        else if (currentState == State.SLASHING){
            return State.SLASHING;
        }
        else if (currentState == State.SLIDING){
            return State.SLIDING;
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

    public void defineMegaman(){

        //Creamos el bodydef de nuestro personaje.
        BodyDef bodyDef = new BodyDef();

        //Establecemos la posicion que tendra nuestro personaje.
        bodyDef.position.set(200 / MegamanMainClass.PixelsPerMeters ,200 / MegamanMainClass.PixelsPerMeters);
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

        //Agregamos el filtro de categoria(quien es nuestro personaje) de box2d.
        fixtureDef.filter.categoryBits = MegamanMainClass.MEGAMAN_BIT;

        //Agregamos el filtro de mascara(a quien puede colisionar nuestro personaje).
        fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                | MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT;

        //Creamos el fixture de nuestro body(con el fixturedef).
        body.createFixture(fixtureDef);

        //Cambiamos la posicion de nuestro circleshape(Esto es porque crearemos otro ...
        //fixture con el mismo fixtureshape).
        circleShape.setPosition(new Vector2(0,-32 / MegamanMainClass.PixelsPerMeters));

        //Creamos otro fixture asi nuestro body contiene dos circulos y es mas grande la colision.
        body.createFixture(fixtureDef);

        //Creamos una caja para utilizarla como sensor al chocar enemigos.
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

        //Creamos una nueva categoria para nuestro sensor.
        fixtureDef.filter.categoryBits = MegamanMainClass.MEGAMAN_SENSOR_BIT;

        fixtureDef.filter.maskBits = MegamanMainClass.DEFAULT_BIT | MegamanMainClass.COIN_BIT
                | MegamanMainClass.WALL_BIT | MegamanMainClass.FLOOR_BIT |
                MegamanMainClass.ZERO_SENSOR_BIT | MegamanMainClass.LAVA_BIT | MegamanMainClass.FIREBALL_ZERO_SENSOR_BIT
                | MegamanMainClass.ENEMY_BIT;

        //Creamos nuestro sensor, y decimos que el fixture se llamara mainNinja(personajePrincipal).
        body.createFixture(fixtureDef).setUserData(this);

    }

    public void onBodyHit(){
        //Si no esta muerto megaman.
        if (currentState != State.DYING) {
            //O sea, que el enemigo pega fuerte.
            megamanDamageCuantity = 30;
            currentState = State.GETTINGHIT;
        }
    }

    public void onBodyHitLower(boolean bool){
        if (bool) {
            //Si no esta muerto megaman.
            if (currentState != State.DYING) {
                //O sea, que el enemigo no pega tan fuerte.
                megamanDamageCuantity = 15;
                currentState = State.GETTINGHIT;
                body.applyLinearImpulse(new Vector2(-4f,0),body.getWorldCenter(),true);
            }
        }
        else {
            //Si no esta muerto megaman.
            if (currentState != State.DYING) {
                //O sea, que el enemigo no pega tan fuerte.
                megamanDamageCuantity = 15;
                currentState = State.GETTINGHIT;
                body.setLinearVelocity(0,0);
                body.setLinearDamping(0);
                body.applyLinearImpulse(new Vector2(4f,0),body.getWorldCenter(),true);
            }
        }
    }

    public boolean isMegamanJumping(){
        if (body.getLinearVelocity().y == 0){
            return false;
        }
        else {
            return true;
        }
    }
}