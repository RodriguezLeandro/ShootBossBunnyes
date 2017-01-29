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
 * Created by Leandro on 05/01/2017.
 */

public class GameOverScreen implements Screen {

    private Game game;
    private Viewport viewport;
    private Stage stage;

    private Label gameOverLabel;
    private Label playAgainLabel;
    private Label scoreLabel;
    private Label backToMainMenu;
    private Label backToLevelSelect;
    private Label.LabelStyle labelStyle;
    private Table table;
    private Integer scoreDataInteger;

    public GameOverScreen(final Game game, Integer scoreDataInteger, final LevelSelect levelSelectScreen){

        this.game = game;

        this.scoreDataInteger = scoreDataInteger;

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,((MegamanMainClass) game).batch);

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.top();
        table.setFillParent(true);

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        gameOverLabel = new Label("GAME OVER",labelStyle);
        playAgainLabel = new Label("PLAY AGAIN",labelStyle);
        scoreLabel = new Label("Score = "+scoreDataInteger,labelStyle);
        backToMainMenu = new Label("MAIN MENU",labelStyle);
        backToLevelSelect = new Label("SELECT LEVEL",labelStyle);

        scoreLabel.setFontScale(1.5f);
        gameOverLabel.setFontScale(2f);
        playAgainLabel.setFontScale(1.5f);
        backToMainMenu.setFontScale(1.5f);
        backToLevelSelect.setFontScale(1.5f);

        playAgainLabel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                switch (levelSelectScreen.getLastLevelPlayed()){

                    case 1:
                        game.setScreen(new Level1Screen((MegamanMainClass) game,levelSelectScreen));
                        dispose();

                        break;
                    case 2:
                        game.setScreen(new Level2Screen((MegamanMainClass) game,levelSelectScreen));
                        dispose();
                        break;
                    case 3:
                        game.setScreen(new Level3Screen((MegamanMainClass)game,levelSelectScreen));
                        dispose();
                        break;
                    case 4:
                        game.setScreen(new Level4Screen((MegamanMainClass)game,levelSelectScreen));
                        dispose();
                        break;
                }
                return true;

            }

        });

        backToMainMenu.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new InitGameScreen(game,levelSelectScreen));
                dispose();
                return true;
            }

        });

        backToLevelSelect.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(levelSelectScreen);
                dispose();
                return true;
            }

        });


        table.row().padTop(80);
        table.add(gameOverLabel).expandX();
        table.row().padTop(80);
        table.add(scoreLabel);

        table.row().padTop(120);

        table.add(playAgainLabel).expandX();

        table.row().padTop(60);

        table.add(backToLevelSelect).expandX();

        table.row().padTop(60);

        table.add(backToMainMenu).expandX();

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
