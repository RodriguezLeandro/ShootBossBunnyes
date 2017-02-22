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
 * Created by Leandro on 13/01/2017.
 */

public class WinScreen implements Screen {

    protected Game game;
    protected Viewport viewport;
    protected Stage stage;
    protected LevelSelect levelSelect;

    protected Table table;

    protected Label.LabelStyle labelStyle;

    protected Label youWinLabel;
    protected Label goToLevelSelect;
    protected Label goToMainMenu;
    protected Label scoreLabel;

    protected Integer scoreDataInteger;


    public WinScreen(final MegamanMainClass game,Integer scoreDataInteger, final LevelSelect levelSelect){

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


        youWinLabel = new Label("YOU WIN !!",labelStyle);
        goToLevelSelect = new Label("SELECT LEVEL",labelStyle);
        goToMainMenu = new Label("MAIN MENU",labelStyle);

        //Aca ponemos el score dependiendo del nivel desde el cual estamos jugando.
        if (levelSelect.getLastLevelPlayed() == 1){
            scoreLabel = new Label("Score level 1 = "+scoreDataInteger,labelStyle);

            Preferences preferences = Gdx.app.getPreferences("Login");


            for (int i = 1; i < preferences.getInteger("CantidadJugadores")+1;i++){
                if ((preferences.getString("LastPlayerThatPlayed")).equals(preferences.getString("Jugador"+i))){

                    if (!preferences.getBoolean("FirstLevelWonJugador"+i)){
                        preferences.putBoolean("FirstLevelWonJugador"+i,true);
                    }
                    preferences.flush();

                }
            }

        }else if (levelSelect.getLastLevelPlayed() == 2){
            scoreLabel = new Label("Score level 2 = "+scoreDataInteger,labelStyle);

            Preferences preferences = Gdx.app.getPreferences("Login");

            for (int i = 1; i < preferences.getInteger("CantidadJugadores")+1;i++){
                if ((preferences.getString("LastPlayerThatPlayed")).equals(preferences.getString("Jugador"+i))){

                    if (!preferences.getBoolean("SecondLevelWonJugador"+i)){
                        preferences.putBoolean("SecondLevelWonJugador"+i,true);
                    }
                    preferences.flush();

                }
            }

        }else if (levelSelect.getLastLevelPlayed() == 3){
            scoreLabel = new Label("Score level 3 = "+scoreDataInteger,labelStyle);
            Preferences preferences = Gdx.app.getPreferences("Login");

            for (int i = 1; i < preferences.getInteger("CantidadJugadores")+1;i++){
                if ((preferences.getString("LastPlayerThatPlayed")).equals(preferences.getString("Jugador"+i))){

                    if (!preferences.getBoolean("ThirdLevelWonJugador"+i)){
                        preferences.putBoolean("ThirdLevelWonJugador"+i,true);
                    }
                    preferences.flush();

                }
            }
        }
        else if (levelSelect.getLastLevelPlayed() == 4){
            //Creo que a esta parte no se llega nunca.
            scoreLabel = new Label("Score level 4 = "+scoreDataInteger,labelStyle);
            Preferences preferences = Gdx.app.getPreferences("Login");

            for (int i = 1; i < preferences.getInteger("CantidadJugadores")+1;i++){
                if ((preferences.getString("LastPlayerThatPlayed")).equals(preferences.getString("Jugador"+i))){

                    if (!preferences.getBoolean("FourthLevelWonJugador"+i)){
                        preferences.putBoolean("FourthLevelWonJugador"+i,true);
                    }
                    preferences.flush();

                }
            }
        }

        //Tengo que updatear el nuevo score del juego.
        levelSelect.updateScore();

        youWinLabel.setFontScale(1.5f);
        goToLevelSelect.setFontScale(1.5f);
        goToMainMenu.setFontScale(1.5f);
        scoreLabel.setFontScale(1.5f);


        goToLevelSelect.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(levelSelect);
                dispose();
                return true;
            }

        });

        goToMainMenu.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new InitGameScreen(game,levelSelect));
                dispose();
                return true;
            }

        });

        table.row().padTop(80);
        table.add(youWinLabel).expandX();

        table.row().padTop(80);

        table.add(scoreLabel);

        table.row().padTop(120);

        table.add(goToLevelSelect).expandX();

        table.row().padTop(60);

        table.add(goToMainMenu).expandX();

        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Limpiamos la pantalla
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
