package Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
 * Created by Leandro on 29/01/2017.
 */

public class GameWinScreen implements Screen{

    private Game game;
    private Viewport viewport;
    private Stage stage;
    private LevelSelect levelSelect;

    private Label.LabelStyle labelStyle;

    private Label youWinLabel;
    private Label playAgain;
    private Label exitGame;
    private Label scoreLabel1;
    private Label scoreLabel2;
    private Label scoreLabel3;
    private Label scoreLabel4;
    private Label finalScoreLabel;

    private Integer scoreDataInteger;

    private Integer finalScore;

    Preferences preferences;

    private Table table;

    public GameWinScreen(final MegamanMainClass game, Integer scoreDataInteger, final LevelSelect levelSelect) {

        this.game = game;

        this.levelSelect = levelSelect;

        this.scoreDataInteger = scoreDataInteger;

        levelSelect.setScore(levelSelect.getLastLevelPlayed(),scoreDataInteger);

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,game.batch);

        Gdx.input.setInputProcessor(stage);

        table = new Table();

        table.top();
        table.setFillParent(true);

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        youWinLabel = new Label("You won the game, congratulations!!",labelStyle);
        playAgain = new Label("Play again",labelStyle);
        exitGame = new Label("EXIT",labelStyle);


        preferences = Gdx.app.getPreferences("Login");

        //Inicializo el final score en 0 por las dudas.
        finalScore = 0;

        for (int i = 1; i < preferences.getInteger("CantidadJugadores")+1;i++){
            if ((preferences.getString("LastPlayerThatPlayed")).equals(preferences.getString("Jugador"+i))){

                if (!preferences.getBoolean("FourthLevelWonJugador"+i)){
                    preferences.putBoolean("FourthLevelWonJugador"+i,true);
                }
                preferences.flush();

            }
        }

        preferences.flush();

        for (int i = 1; i < preferences.getInteger("CantidadJugadores")+1;i++){
            if ((preferences.getString("LastPlayerThatPlayed")).equals(preferences.getString("Jugador"+i))){

                scoreLabel1 = new Label("Score = "+preferences.getInteger("ScoreLevel1Jugador"+i),labelStyle);
                scoreLabel2 = new Label("Score = "+preferences.getInteger("ScoreLevel2Jugador"+i),labelStyle);
                scoreLabel3 = new Label("Score = "+preferences.getInteger("ScoreLevel3Jugador"+i),labelStyle);
                scoreLabel4 = new Label("Score = "+preferences.getInteger("ScoreLevel4Jugador"+i),labelStyle);

                finalScore = preferences.getInteger("ScoreLevel1Jugador"+i)+preferences.getInteger("ScoreLevel2Jugador"+i)+preferences.getInteger("ScoreLevel3Jugador"+i)+preferences.getInteger("ScoreLevel4Jugador"+i);

                preferences.putInteger("FinalScoreJugador"+i,finalScore);

                preferences.flush();
            }
        }

        //No le veo sentido a lo de abajo, lo borro por el momento, si veo que se rompe todo, vuelvo a ponerlo.
        /*
        for (int i = 1; i < preferences.getInteger("CantidadJugadores")+1;i++){
            if ((preferences.getString("LastPlayerThatPlayed")).equals(preferences.getString("Jugador"+i))){
                preferences.putInteger("JugadorScore"+i,finalScore);
            }
        }
        preferences.flush();

        */

        finalScoreLabel = new Label("Final score = "+finalScore,labelStyle);

        youWinLabel.setFontScale(1.5f);
        playAgain.setFontScale(1.5f);
        exitGame.setFontScale(1.5f);
        scoreLabel1.setFontScale(1.5f);
        scoreLabel2.setFontScale(1.5f);
        scoreLabel3.setFontScale(1.5f);
        scoreLabel4.setFontScale(1.5f);
        finalScoreLabel.setFontScale(1.5f);

        playAgain.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
               //Borramos todos los datos y comenzamos de nuevo.
                levelSelect.dispose();
                game.setScreen(new LevelSelect(game));
                dispose();
                return true;
            }

        });

        exitGame.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                dispose();
                return true;
            }

        });

        table.row().padTop(80);
        table.add(youWinLabel).expandX();

        table.row().padTop(80);

        table.add(scoreLabel1);

        table.row().padTop(20);

        table.add(scoreLabel2);

        table.row().padTop(20);
        table.add(scoreLabel3);

        table.row().padTop(20);

        table.add(scoreLabel4);

        table.row().padTop(20);

        table.add(finalScoreLabel);

        table.row().padTop(40);

        table.add(playAgain).expandX();

        table.row().padTop(20);

        table.add(exitGame).expandX();

        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

            //Limpiamos la pantalla
            Gdx.gl.glClearColor(0.25f, 0.51f, 0, 1);
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
