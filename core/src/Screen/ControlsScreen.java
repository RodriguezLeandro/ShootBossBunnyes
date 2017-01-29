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
 * Created by Leandro on 06/01/2017.
 */

public class ControlsScreen implements Screen {

    private Game game;

    private Viewport viewport;
    private Stage stage;
    private LevelSelect levelSelect;

    private Label backToMenuScreen;
    private Label upArrowLabel;
    private Label leftArrowLabel;
    private Label downArrowLabel;
    private Label rightArrowLabel;

    private Label rightButtonLabel;
    private Label leftButtonLabel;
    private Label downButtonLabel;
    private Label upButtonLabel;

    private Label.LabelStyle labelStyle;

    private Table table;

    //Cuando tenga definidas las acciones de los controles, lleno esta clase.
    public ControlsScreen(final MegamanMainClass game){
        this.game = game;

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,game.batch);

        Gdx.input.setInputProcessor(stage);

        table = new Table();

        table.top();
        table.setFillParent(true);

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        upArrowLabel = new Label("Up arrow:        It does nothing",labelStyle);
        leftArrowLabel = new Label("Left arrow:     You can move to the left",labelStyle);
        downArrowLabel = new Label("Down arrow:     It does nothing",labelStyle);
        rightArrowLabel = new Label("Right arrow:    You can move to the right",labelStyle);

        rightButtonLabel = new Label("Right Button:  Slide",labelStyle);
        leftButtonLabel = new Label("Left Button: Throw punch fireball",labelStyle);
        downButtonLabel = new Label("Down Button: Jump",labelStyle);
        upButtonLabel = new Label("Up Button: Gravity power",labelStyle);

        backToMenuScreen = new Label("Back to Menu Screen",labelStyle);

        backToMenuScreen.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new InitGameScreen(game));
                dispose();
                return true;
            }
        });

        upArrowLabel.setFontScale(1.5f);
        leftArrowLabel.setFontScale(1.5f);
        rightArrowLabel.setFontScale(1.5f);
        downArrowLabel.setFontScale(1.5f);

        upButtonLabel.setFontScale(1.5f);
        leftButtonLabel.setFontScale(1.5f);
        rightButtonLabel.setFontScale(1.5f);
        downButtonLabel.setFontScale(1.5f);

        backToMenuScreen.setFontScale(2);

        table.row();
        table.add(upArrowLabel).pad(40,0,0,100);
        table.row();
        table.add(downArrowLabel).fill().padTop(20);
        table.row();
        table.add(leftArrowLabel).fill().padTop(20);
        table.row();
        table.add(rightArrowLabel).fill().padTop(20);
        table.row();

        table.add(upButtonLabel).fill().padTop(40);
        table.row();
        table.add(downButtonLabel).fill().padTop(20);
        table.row();
        table.add(leftButtonLabel).fill().padTop(20);
        table.row();
        table.add(rightButtonLabel).fill().padTop(20);
        table.row();

        table.add(backToMenuScreen).fill().padTop(40);

        stage.addActor(table);
    }

    public ControlsScreen(final MegamanMainClass game, final LevelSelect levelSelect){
        this.game = game;

        this.levelSelect = levelSelect;

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,game.batch);

        Gdx.input.setInputProcessor(stage);

        table = new Table();

        table.top();
        table.setFillParent(true);

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        upArrowLabel = new Label("Up arrow:        It does nothing",labelStyle);
        leftArrowLabel = new Label("Left arrow:     You can move to the left",labelStyle);
        downArrowLabel = new Label("Down arrow:     It does nothing",labelStyle);
        rightArrowLabel = new Label("Right arrow:    You can move to the right",labelStyle);

        rightButtonLabel = new Label("Right Button:  Slide",labelStyle);
        leftButtonLabel = new Label("Left Button: Throw punch fireball",labelStyle);
        downButtonLabel = new Label("Down Button: Jump",labelStyle);
        upButtonLabel = new Label("Up Button: Gravity power",labelStyle);

        backToMenuScreen = new Label("Back to Menu Screen",labelStyle);

        backToMenuScreen.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new InitGameScreen(game,levelSelect));
                dispose();
                return true;
            }
        });

        upArrowLabel.setFontScale(1.5f);
        leftArrowLabel.setFontScale(1.5f);
        rightArrowLabel.setFontScale(1.5f);
        downArrowLabel.setFontScale(1.5f);

        upButtonLabel.setFontScale(1.5f);
        leftButtonLabel.setFontScale(1.5f);
        rightButtonLabel.setFontScale(1.5f);
        downButtonLabel.setFontScale(1.5f);

        backToMenuScreen.setFontScale(2);

        table.row();
        table.add(upArrowLabel).pad(40,0,0,100);
        table.row();
        table.add(downArrowLabel).fill().padTop(20);
        table.row();
        table.add(leftArrowLabel).fill().padTop(20);
        table.row();
        table.add(rightArrowLabel).fill().padTop(20);
        table.row();

        table.add(upButtonLabel).fill().padTop(40);
        table.row();
        table.add(downButtonLabel).fill().padTop(20);
        table.row();
        table.add(leftButtonLabel).fill().padTop(20);
        table.row();
        table.add(rightButtonLabel).fill().padTop(20);
        table.row();

        table.add(backToMenuScreen).fill().padTop(40);

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

    }
}
