package Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 17/02/2017.
 */

public class LoginPlayerScreen implements Screen {


    private Game game;
    private Viewport viewport;
    private Stage stage;

    private Label nombreJugadorActual;

    private TextField nombreNuevoJugador;

    private Button actualizarJugador;

    private Label.LabelStyle labelStyle;

    private Preferences preferences;

    public LoginPlayerScreen(MegamanMainClass game) {
        this.game = game;
        crearLoginPlayerScreen();

    }

    public LoginPlayerScreen(MegamanMainClass game,LevelSelect levelSelect) {
        this.game = game;
        crearLoginPlayerScreen();

    }

    public void crearLoginPlayerScreen(){

        viewport = new FitViewport(MegamanMainClass.Virtual_Width,MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,((MegamanMainClass)game).batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        preferences = Gdx.app.getPreferences("Login");

        //Si el ultimo jugador que jugo, esta vacio.(O sea nunca jugo nadie)
        if (preferences.getString("LastPlayerThatPlayed") == ""){

            nombreJugadorActual = new Label("Welcome, please insert your name.",labelStyle);

            nombreNuevoJugador = new TextField("",skin);

            actualizarJugador = new Button(skin);

            table.top().padTop(100);

            table.setFillParent(true);

            nombreJugadorActual.setFontScale(1.5f);


            table.add(nombreJugadorActual).expandX();
            table.row().padTop(20);

            table.add(nombreNuevoJugador).expandX();
            table.row().padTop(80);

            table.add(actualizarJugador);

        }
        //Si ya habia un jugador anteriormente.
        else{

            nombreJugadorActual = new Label("Nombre jugador actual : "+preferences.getString("LastPlayerThatPlayed"),labelStyle);

            nombreNuevoJugador = new TextField("",skin);

            actualizarJugador = new Button(skin);

            table.top().padTop(100);

            table.setFillParent(true);


            nombreJugadorActual.setFontScale(1.5f);
            actualizarJugador.setSize(200,10);

            table.add(nombreJugadorActual).expandX();
            table.row().padTop(20);

            table.add(nombreNuevoJugador).expandX();
            table.row().padTop(80);

            table.add(actualizarJugador).expandX();

        }

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
