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
 * Created by Leandro on 17/02/2017.
 */

public class ScoreChartScreen implements Screen {


    private Game game;
    private Viewport viewport;
    private Stage stage;
    private LevelSelect levelSelect;

    private Label jugadoresLabel;
    private Label scoreJugadoresLabel;

    private Label jugador1Label;
    private Label scoreJugador1Label;

    private Label jugador2Label;
    private Label scoreJugador2Label;

    private Label jugador3Label;
    private Label scoreJugador3Label;

    private Label jugador4Label;
    private Label scoreJugador4Label;

    private Label jugador5Label;
    private Label scoreJugador5Label;

    private Label jugador6Label;
    private Label scoreJugador6Label;

    private Label jugador7Label;
    private Label scoreJugador7Label;

    private Label jugador8Label;
    private Label scoreJugador8Label;

    private Label jugador9Label;
    private Label scoreJugador9Label;

    private Label backToMainMenuLabel;

    private Label.LabelStyle labelStyle;

    private Preferences preferences;


    public ScoreChartScreen(MegamanMainClass game){
        this.game = game;
        crearScoreChartScreen();
    }

    public ScoreChartScreen(MegamanMainClass game,LevelSelect levelSelect) {
        this.game = game;
        this.levelSelect = levelSelect;
        crearScoreChartScreen();
    }

    public void crearScoreChartScreen(){

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,((MegamanMainClass)game).batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        preferences = Gdx.app.getPreferences("Login");


        jugadoresLabel = new Label("Jugador",labelStyle);
        scoreJugadoresLabel = new Label("High Score",labelStyle);

        jugador1Label = new Label("Jugador 1",labelStyle);
        scoreJugador1Label = new Label("000000",labelStyle);

        jugador2Label = new Label("Jugador 2",labelStyle);
        scoreJugador2Label = new Label("000000",labelStyle);

        jugador3Label = new Label("Jugador 3",labelStyle);
        scoreJugador3Label = new Label("000000",labelStyle);

        jugador4Label = new Label("Jugador 4",labelStyle);
        scoreJugador4Label = new Label("000000",labelStyle);

        jugador5Label = new Label("Jugador 5",labelStyle);
        scoreJugador5Label = new Label("000000",labelStyle);

        jugador6Label = new Label("Jugador 6",labelStyle);
        scoreJugador6Label = new Label("000000",labelStyle);

        jugador7Label = new Label("Jugador 7",labelStyle);
        scoreJugador7Label = new Label("000000",labelStyle);

        jugador8Label = new Label("Jugador 8",labelStyle);
        scoreJugador8Label = new Label("000000",labelStyle);

        jugador9Label = new Label("Jugador 9",labelStyle);
        scoreJugador9Label = new Label("000000",labelStyle);

        backToMainMenuLabel = new Label("Back to Main Menu",labelStyle);


        for (int i = 1; i < (preferences.getInteger("CantidadJugadores")+1);i++){
            if (i == 1){
                jugador1Label.setText(preferences.getString("Jugador1"));
                scoreJugador1Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador1")));
            }else if (i == 2){
                jugador2Label.setText(preferences.getString("Jugador2"));
                scoreJugador2Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador2")));
            }else if (i == 3){
                jugador3Label.setText(preferences.getString("Jugador3"));
                scoreJugador3Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador3")));
            }else if (i == 4){
                jugador4Label.setText(preferences.getString("Jugador4"));
                scoreJugador4Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador4")));
            }else if (i == 5){
                jugador5Label.setText(preferences.getString("Jugador5"));
                scoreJugador5Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador5")));
            }else if (i == 6){
                jugador6Label.setText(preferences.getString("Jugador6"));
                scoreJugador6Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador6")));
            }else if (i == 7){
                jugador7Label.setText(preferences.getString("Jugador7"));
                scoreJugador7Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador7")));
            }else if (i == 8){
                jugador8Label.setText(preferences.getString("Jugador8"));
                scoreJugador8Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador8")));
            }else if (i == 9){
                jugador9Label.setText(preferences.getString("Jugador9"));
                scoreJugador9Label.setText(String.format("%06d",preferences.getInteger("FinalScoreJugador9")));
            }

        }

        jugadoresLabel.setFontScale(2);
        scoreJugadoresLabel.setFontScale(2);
        backToMainMenuLabel.setFontScale(2);

        jugador1Label.setFontScale(1.5f);
        jugador2Label.setFontScale(1.5f);
        jugador3Label.setFontScale(1.5f);
        jugador4Label.setFontScale(1.5f);
        jugador5Label.setFontScale(1.5f);
        jugador6Label.setFontScale(1.5f);
        jugador7Label.setFontScale(1.5f);
        jugador8Label.setFontScale(1.5f);
        jugador9Label.setFontScale(1.5f);

        scoreJugador1Label.setFontScale(1.5f);
        scoreJugador2Label.setFontScale(1.5f);
        scoreJugador3Label.setFontScale(1.5f);
        scoreJugador4Label.setFontScale(1.5f);
        scoreJugador5Label.setFontScale(1.5f);
        scoreJugador6Label.setFontScale(1.5f);
        scoreJugador7Label.setFontScale(1.5f);
        scoreJugador8Label.setFontScale(1.5f);
        scoreJugador9Label.setFontScale(1.5f);

        backToMainMenuLabel.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (levelSelect == null){
                    game.setScreen(new InitGameScreen(game));
                    dispose();
                }
                else {
                    game.setScreen(new InitGameScreen(game,levelSelect));
                    dispose();
                }

                return  true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        table.top().padTop(20);

        table.setFillParent(true);

        table.add(jugadoresLabel).padRight(30);
        table.add(scoreJugadoresLabel);
        table.row().padTop(60);

        table.add(jugador1Label).padRight(30);
        table.add(scoreJugador1Label);
        table.row().padTop(10);

        table.add(jugador2Label).padRight(30);
        table.add(scoreJugador2Label);
        table.row().padTop(10);

        table.add(jugador3Label).padRight(30);
        table.add(scoreJugador3Label);
        table.row().padTop(10);

        table.add(jugador4Label).padRight(30);
        table.add(scoreJugador4Label);
        table.row().padTop(10);

        table.add(jugador5Label).padRight(30);
        table.add(scoreJugador5Label);
        table.row().padTop(10);

        table.add(jugador6Label).padRight(30);
        table.add(scoreJugador6Label);
        table.row().padTop(10);

        table.add(jugador7Label).padRight(30);
        table.add(scoreJugador7Label);
        table.row().padTop(10);

        table.add(jugador8Label).padRight(30);
        table.add(scoreJugador8Label);
        table.row().padTop(10);

        table.add(jugador9Label).padRight(30);
        table.add(scoreJugador9Label);
        table.row().padTop(30);

        table.add(backToMainMenuLabel);

        stage.addActor(table);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.51f, 0.51f, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
