package Screen;

import com.badlogic.gdx.Application;
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
    private Label loginLabel;
    private Label viewScoreLabel;

    private Label.LabelStyle labelStyle;


    public InitGameScreen(final Game game){
        this.game = game;

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,((MegamanMainClass)game).batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        nameGameLabel = new Label("Punch-Man",labelStyle);
        newGameLabel = new Label("New Game",labelStyle);
        controlsLabel = new Label("Controls",labelStyle);
        exitLabel = new Label("Exit",labelStyle);
        loginLabel = new Label("Login",labelStyle);
        viewScoreLabel = new Label("View Score Chart",labelStyle);

        newGameLabel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new LevelSelect((MegamanMainClass) game));

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

                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    //Hay que enviar al usuario a la pantalla de controles..
                    game.setScreen(new ControlsAndroidScreen((MegamanMainClass) game));
                    dispose();

                    return true;

                }else {
                    game.setScreen(new ControlsDesktopScreen((MegamanMainClass) game));
                    dispose();
                    return true;
                }
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

        loginLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LoginPlayerScreen((MegamanMainClass)game));
                dispose();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

        });

        viewScoreLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ScoreChartScreen((MegamanMainClass)game));
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
        loginLabel.setFontScale(2);
        viewScoreLabel.setFontScale(2);

        table.add(nameGameLabel).expandX();
        table.row().padTop(150);
        table.add(newGameLabel).expandX();
        table.row().padTop(20);
        table.add(loginLabel).expandX();
        table.row().padTop(20);
        table.add(viewScoreLabel).expandX();
        table.row().padTop(20);
        table.add(controlsLabel).expandX();
        table.row().padTop(20);
        table.add(exitLabel).expandX();

        stage.addActor(table);
    }

    public InitGameScreen(final Game game, final LevelSelect levelSelect){
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
        loginLabel = new Label("Login",labelStyle);
        viewScoreLabel = new Label("View Score Chart",labelStyle);

        newGameLabel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(levelSelect);

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
                //Si el usuario esta utilizando un dispositivo android, lo llevamos a la pantalla de controles de android,
                //de lo contrario, lo llevamos a la pantalla de controles de desktop.
                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    //Hay que enviar al usuario a la pantalla de controles..
                    game.setScreen(new ControlsAndroidScreen((MegamanMainClass) game, levelSelect));
                    dispose();
                    return true;
                }
                else {
                    game.setScreen(new ControlsDesktopScreen((MegamanMainClass) game,levelSelect));
                    dispose();
                    return true;
                }
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

        loginLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LoginPlayerScreen((MegamanMainClass)game,levelSelect));
                dispose();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

        });

        viewScoreLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ScoreChartScreen((MegamanMainClass)game,levelSelect));
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
        loginLabel.setFontScale(2);
        viewScoreLabel.setFontScale(2);

        table.add(nameGameLabel).expandX();
        table.row().padTop(150);
        table.add(newGameLabel).expandX();
        table.row().padTop(20);
        table.add(loginLabel).expandX();
        table.row().padTop(20);
        table.add(viewScoreLabel).expandX();
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
