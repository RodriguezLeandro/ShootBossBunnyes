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

import Sprites.Megaman;

/**
 * Created by Leandro on 06/01/2017.
 */

public class InitGameScreen implements Screen {

    private Game game;
    private Viewport viewport;
    private Stage stage;

    private Label nameGameLabel;
    private Label newGameLabel;
    private Label controlsLabel;
    private Label exitLabel;

    private Label.LabelStyle labelStyle;


    public InitGameScreen(final Game game){
        this.game = game;

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,((MegamanMainClass)game).batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        nameGameLabel = new Label("Geometry-Man",labelStyle);
        newGameLabel = new Label("New Game",labelStyle);
        controlsLabel = new Label("Controls",labelStyle);
        exitLabel = new Label("Exit",labelStyle);

        newGameLabel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new MainGameScreen((MegamanMainClass) game));

                dispose();

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        controlsLabel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Hay que enviar al usuario a la pantalla de controles..
                game.setScreen(new ControlsScreen((MegamanMainClass) game));
                dispose();

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        exitLabel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Hay que eliminar todo? O sacar al usuario al menos de la app.
                Gdx.app.exit();
                dispose();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        table.top().padTop(100);

        table.setFillParent(true);

        nameGameLabel.setFontScale(3);

        newGameLabel.setFontScale(2);
        controlsLabel.setFontScale(2);
        exitLabel.setFontScale(2);

        table.add(nameGameLabel).expandX();
        table.row().padTop(150);
        table.add(newGameLabel).expandX();
        table.row().padTop(20);
        table.add(controlsLabel).expandX();
        table.row().padTop(20);
        table.add(exitLabel).expandX();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
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
