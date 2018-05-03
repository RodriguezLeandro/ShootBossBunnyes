package Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 11/01/2017.
 */

//Explico de que consta la logica de la clase esta, para su futura utilizacion.
//Esta clase una vez que se instancia no se borra nunca, lo que ocurre es que se manda.
//A todas las otras instancias del juego, para que luego puedan volver a esta clase.
//Esto con el fin de ir guardando los valores del estado actual del juego.
//Notese, que es importante, que existen dos constructores de InitGameScreen.
//Uno de ellos, sirve para cuando iniciamos el juego por primera vez,
//El otro, cuando volvemos a la pantalla principal por medio de GameOverScreen.
//Entonces, cuando finalizemos uno de los niveles, es decir, cuando ganemos, podemos.
//Venir a esta pantalla, y decir que ya hemos ganado el nivel X.
//Entonces, el jugador tendra que jugar los demas niveles para desbloquear el ultimo.
//Si el jugador desbloquea el ultimo nivel, hacemos un setScreen de los creditos del juego.

public class LevelSelect implements Screen {

    private Game game;
    private Viewport viewport;
    private Stage stage;

    private Table table;

    private Label.LabelStyle labelStyle;

    private InputListener inputListener;

    private LevelSelect thisLevelSelect;

    private Integer lastLevelPlayed;

    private Boolean firstLevelWon;
    private Boolean secondLevelWon;
    private Boolean thirdLevelWon;
    private Boolean fourthLevelWon;
    private Boolean lastLevelWon;

    public LevelSelect(MegamanMainClass mainGameScreen){

        game = mainGameScreen;

        viewport = new FitViewport(MegamanMainClass.Virtual_Width, MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,((MegamanMainClass) game).batch);

        table = new Table();

        thisLevelSelect = this;

        Gdx.input.setInputProcessor(stage);

        firstLevelWon = false;
        secondLevelWon= false;
        thirdLevelWon = false;
        fourthLevelWon= false;
        lastLevelWon = false;

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        createHud();

    }

    public void createHud(){

        table.top().left();
        table.setFillParent(true);

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Label instructionToPlay = new Label("Touch the level you want to play",labelStyle);
        Label level1 = new Label("Level 1",labelStyle);
        Label level2 = new Label("Level 2",labelStyle);
        Label level3 = new Label("Level 3",labelStyle);
        Label level4 = new Label("Level 4(final level, very hard)",labelStyle);

        Image imageZero = new Image(new TextureRegion(new Texture("LevelSelectImages/Zero.png")));
        Image imageBoss1 = new Image(new TextureRegion(new Texture("LevelSelectImages/Boss1.png")));
        Image imageBoss2 = new Image(new TextureRegion(new Texture("LevelSelectImages/Boss2.png")));
        Image imageWho = new Image(new TextureRegion(new Texture("LevelSelectImages/Who.png")));

        level1.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Level1Screen((MegamanMainClass) game , thisLevelSelect));
                return true;
            }
        });

        level2.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Level2Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        level3.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Level3Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        level4.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Level4Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        imageZero.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Level1Screen((MegamanMainClass) game , thisLevelSelect));
                return true;
            }
        });

        imageBoss1.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Level2Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        imageBoss2.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Level3Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        imageWho.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Level4Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        table.top().padTop(100);

        table.setFillParent(true);

        instructionToPlay.setFontScale(3);
        level1.setFontScale(1.5f);
        level2.setFontScale(1.5f);
        level3.setFontScale(1.5f);
        level4.setFontScale(1.5f);

        table.add(instructionToPlay).expandX();
        table.row();
        table.padTop(40);

        table.add(level1);
        table.add(imageZero);
        table.row();

        table.add(level2);
        table.add(imageBoss1);
        table.row();

        table.add(level3);
        table.add(imageBoss2);
        table.row();

        table.add(level4);
        table.add(imageWho);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void updateScore(){

    }

    public void setLastLevelPlayed(Integer integer){
        lastLevelPlayed = integer;
    }

    public Integer getLastLevelPlayed(){
        return lastLevelPlayed;
    }

    public void setWonLevel(Integer levelWon){

        if (levelWon == 1){
            firstLevelWon = true;
        }
        else if(levelWon == 2){
            secondLevelWon = true;
        }
        else if(levelWon == 3){
            thirdLevelWon = true;
        }
        else if(levelWon == 4){
            fourthLevelWon = true;
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.51f, 0.51f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
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
        stage.dispose();
    }
}



    /* Esta funcion servía para la version del juego que tenía puntaje.
    public void setScore(Integer lastLevelPlayed,Integer score){

        preferences = Gdx.app.getPreferences("Login");

        for (int i = 1; i < preferences.getInteger("CantidadJugadores")+1;i++){
            //Si el jugador es el que se encuentra logeado.
            if ((preferences.getString("LastPlayerThatPlayed")).equals(preferences.getString("Jugador"+i))){
                //Si el ultimo nivel jugado es el 1.
                if (lastLevelPlayed == 1) {
                    //Si no existe score anterior, ponemos el nuevo score.
                    if (preferences.getInteger("ScoreLevel1Jugador"+i) == 0){
                        preferences.putInteger("ScoreLevel1Jugador"+i,score);
                        preferences.flush();
                    }
                    //Si ya hay un score anterior, entonces vemos si es menor que el nuevo
                    else{
                        if (preferences.getInteger("ScoreLevel1Jugador"+i) < score){
                            //Si lo es, entonces sobreescribimos el nuevo score.
                            preferences.putInteger("ScoreLevel1Jugador"+i,score);
                            preferences.flush();
                        }
                        else {
                            //Si no lo es, entonces dejamos el score mas alto.
                        }
                    }

                }
                else if(lastLevelPlayed == 2){
                    //Si no existe score anterior, ponemos el nuevo score.
                    if (preferences.getInteger("ScoreLevel2Jugador"+i) == 0){
                        preferences.putInteger("ScoreLevel2Jugador"+i,score);
                        preferences.flush();
                    }
                    //Si ya hay un score anterior, entonces vemos si es menor que el nuevo
                    else{
                        if (preferences.getInteger("ScoreLevel2Jugador"+i) < score){
                            //Si lo es, entonces sobreescribimos el nuevo score.
                            preferences.putInteger("ScoreLevel2Jugador"+i,score);
                            preferences.flush();
                        }
                        else {
                            //Si no lo es, entonces dejamos el score mas alto.
                        }
                    }

                }
                else if(lastLevelPlayed == 3){
                    //Si no existe score anterior, ponemos el nuevo score.
                    if (preferences.getInteger("ScoreLevel3Jugador"+i) == 0){
                        preferences.putInteger("ScoreLevel3Jugador"+i,score);
                        preferences.flush();
                    }
                    //Si ya hay un score anterior, entonces vemos si es menor que el nuevo
                    else{
                        if (preferences.getInteger("ScoreLevel3Jugador"+i) < score){
                            //Si lo es, entonces sobreescribimos el nuevo score.
                            preferences.putInteger("ScoreLevel3Jugador"+i,score);
                            preferences.flush();
                        }
                        else {
                            //Si no lo es, entonces dejamos el score mas alto.
                        }
                    }

                }
                else if(lastLevelPlayed == 4){
                    //Si no existe score anterior, ponemos el nuevo score.
                    if (preferences.getInteger("ScoreLevel4Jugador"+i) == 0){
                        preferences.putInteger("ScoreLevel4Jugador"+i,score);
                        preferences.flush();
                    }
                    //Si ya hay un score anterior, entonces vemos si es menor que el nuevo
                    else{
                        if (preferences.getInteger("ScoreLevel4Jugador"+i) < score){
                            //Si lo es, entonces sobreescribimos el nuevo score.
                            preferences.putInteger("ScoreLevel4Jugador"+i,score);
                            preferences.flush();
                        }
                        else {
                            //Si no lo es, entonces dejamos el score mas alto.
                        }
                    }


            }
        }

        }
    }

*/