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
    private Label scoreLabel;

    private Integer scoreDataInteger;

    private Table table;

    public GameWinScreen(final MegamanMainClass game, Integer scoreDataInteger, final LevelSelect levelSelect) {

        this.game = game;

        this.levelSelect = levelSelect;

        this.scoreDataInteger = scoreDataInteger;

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
        scoreLabel = new Label("Score = "+scoreDataInteger,labelStyle);

        youWinLabel.setFontScale(1.5f);
        playAgain.setFontScale(1.5f);
        exitGame.setFontScale(1.5f);
        scoreLabel.setFontScale(1.5f);

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

        table.add(scoreLabel);

        table.row().padTop(120);

        table.add(playAgain).expandX();

        table.row().padTop(60);

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
