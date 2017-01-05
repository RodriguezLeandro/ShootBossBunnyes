package Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Label.LabelStyle labelStyle;
    private Table table;

    public GameOverScreen(Game game){

        this.game = game;

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,((MegamanMainClass) game).batch);

        table = new Table();
        table.center();
        table.setFillParent(true);

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        gameOverLabel = new Label("GAME OVER",labelStyle);
        playAgainLabel = new Label("CLICK HERE TO PLAY AGAIN",labelStyle);

        gameOverLabel.setFontScale(1.5f);
        playAgainLabel.setFontScale(1.5f);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(20);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Limpiamos la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        if (Gdx.input.isTouched()){
            game.setScreen(new MainGameScreen((MegamanMainClass) game));
            dispose();
        }
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
