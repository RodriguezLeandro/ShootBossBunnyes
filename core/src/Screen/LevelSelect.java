package Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    private Label level1Label;
    private Label level2Label;
    private Label level3Label;
    private Label level4Label;
    private Label finalLabel;
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

        createHud();

        firstLevelWon = false;
        secondLevelWon= false;
        thirdLevelWon = false;
        fourthLevelWon= false;
        lastLevelWon = false;
    }

    public void createHud(){

        table.top().left();
        table.setFillParent(true);

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        level1Label = new Label("LEVEL 1",labelStyle);
        level2Label = new Label("LEVEL 2",labelStyle);
        level3Label = new Label("LEVEL 3",labelStyle);
        level4Label = new Label("LEVEL 4",labelStyle);
        finalLabel = new Label("FINAL LEVEL",labelStyle);


        level1Label.setFontScale(1.5f);
        level2Label.setFontScale(1.5f);
        level3Label.setFontScale(1.5f);
        level4Label.setFontScale(1.5f);
        finalLabel.setFontScale(1.5f);



        level1Label.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Si todavia no cruzo el primer nivel, entonces que lo juegue.
                if (!firstLevelWon)
                game.setScreen(new Level1Screen((MegamanMainClass) game , thisLevelSelect));
                return true;
            }
        });

        level2Label.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!secondLevelWon){}
                game.setScreen(new Level2Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        level3Label.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!thirdLevelWon){}
             //   game.setScreen(new Level3Screen((MegamanMainClass) game));
                return true;
            }
        });

        level4Label.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!fourthLevelWon){}
         //       game.setScreen(new Level4Screen((MegamanMainClass) game));
                return true;
            }
        });

        finalLabel.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Si ya gano todo.
                //ENtonces hacemos magia y cambiamos la imagen del medio o algo asi.
                //Por ej.
              //  finalLabel.setText("ACA LA IMAGEN FINAL POR EJ");
                if (firstLevelWon && secondLevelWon && thirdLevelWon && fourthLevelWon){}
              //  game.setScreen(new finalLevelScreen((MegamanMainClass) game));

                return true;
            }
        });

        table.add(level1Label).pad(30,30,0,0);
        table.add();
        table.add(level2Label).pad(30,200,0,30);
        table.row();
        table.add();
        table.add(finalLabel).pad(210,210,0,0);
        table.add();
        table.row();
        table.add(level3Label).pad(200,30,30,0);
        table.add();
        table.add(level4Label).pad(200,200,30,30);

        stage.addActor(table);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

    }

    public void setLastLevelPlayed(Integer integer){
        lastLevelPlayed = integer;
    }

    public Integer getLastLevelPlayed(){
        return lastLevelPlayed;
    }

    public void setWonLevel(WinScreen winScreen){
        if (winScreen.getClass() == Level1WinScreen.class){
            firstLevelWon = true;
        }
        //Aca abajo tendria que poner: Level2Winscreen.class, level3winscreen.class,  y asi con los restantes.
        else if (winScreen.getClass() == Level1WinScreen.class){
            //SECONDLEVELWON = TRUE;
        }else if (winScreen.getClass() == Level1WinScreen.class){
            //THIRDLEVELWON = TRUE, ETC.
        }
        else if (winScreen.getClass() == Level1WinScreen.class){

        }
        else {

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

    }
}
