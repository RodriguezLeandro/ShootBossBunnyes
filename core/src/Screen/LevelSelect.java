package Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 11/01/2017.
 */

//Explico de que consta la logica de la clase esta, para su futura utilizacion.
//Esta clase una vez que se instancia no se borra nunca, lo que ocurre es que se manda.
//A todas las otras instancias del juego, para que luego puedan volver a esta clase.
//Esto con el fin de ir guardando los valores del estado actual del juego.
//Notese, que es importante, que existen dos constructores de InitGameScreen.
//Uno de ellos, sirve para cuando iniciamos el juego por primera vez,
//El otro, cuando volvemos a la pantalla principal por medio de GameOverScreen.
//Entonces, cuando finalizemos uno de los niveles, es decir, cuando ganemos, podemos.
//Venir a esta pantalla, y decir que ya hemos ganado el nivel X.
//Entonces, el jugador tendra que jugar los demas niveles para desbloquear el ultimo.
//Si el jugador desbloquea el ultimo nivel, hacemos un setScreen de los creditos del juego.

public class LevelSelect implements Screen {

    private Game game;
    private Viewport viewport;
    private Stage stage;

    private Table table;

    private Label level1Label;
    private Label level2Label;
    private Label level3Label;
    private Label level4Label;
    private Label finalLabel;
    private Label.LabelStyle labelStyle;

    private InputListener inputListener;

    private LevelSelect thisLevelSelect;

    private Integer lastLevelPlayed;

    private Boolean firstLevelWon;
    private Boolean secondLevelWon;
    private Boolean thirdLevelWon;
    private Boolean fourthLevelWon;
    private Boolean lastLevelWon;


    public LevelSelect(MegamanMainClass mainGameScreen){

        game = mainGameScreen;

        viewport = new FitViewport(MegamanMainClass.Virtual_Width, MegamanMainClass.Virtual_Height,new OrthographicCamera());

        stage = new Stage(viewport,((MegamanMainClass) game).batch);

        table = new Table();

        thisLevelSelect = this;

        Gdx.input.setInputProcessor(stage);

        createHud();

        firstLevelWon = false;
        secondLevelWon= false;
        thirdLevelWon = false;
        fourthLevelWon= false;
        lastLevelWon = false;
    }

    public void createHud(){

        table.top().left();
        table.setFillParent(true);

        Image imageMegaman = new Image(new TextureRegion(new Texture("LevelSelectImages/Megaman.png")));
        Image imageZero = new Image(new TextureRegion(new Texture("LevelSelectImages/Zero.png")));
        Image imageBoss1 = new Image(new TextureRegion(new Texture("LevelSelectImages/Boss1.png")));
        Image imageBoss2 = new Image(new TextureRegion(new Texture("LevelSelectImages/Boss2.png")));
        Image imageWho = new Image(new TextureRegion(new Texture("LevelSelectImages/Who.png")));

        imageZero.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Si todavia no cruzo el primer nivel, entonces que lo juegue.
                if (!firstLevelWon)
                game.setScreen(new Level1Screen((MegamanMainClass) game , thisLevelSelect));
                return true;
            }
        });

        imageBoss1.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!secondLevelWon)
                game.setScreen(new Level2Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        imageBoss2.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!thirdLevelWon)
                game.setScreen(new Level3Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        imageWho.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Solo se puede jugar el cuarto nivel si cruzamos los anteriores.
                if (firstLevelWon && secondLevelWon && thirdLevelWon)
                game.setScreen(new Level4Screen((MegamanMainClass) game,thisLevelSelect));
                return true;
            }
        });

        imageMegaman.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Al final no, ya que no tuve ganas de hacer el ultimo y quinto nivel.
                //Si ya gano todo.
                //ENtonces hacemos magia y cambiamos la imagen del medio o algo asi.
                //Por ej.
              //  finalLabel.setText("ACA LA IMAGEN FINAL POR EJ");
          //    if (firstLevelWon && secondLevelWon && thirdLevelWon && fourthLevelWon){}
              //  game.setScreen(new finalLevelScreen((MegamanMainClass) game));

                return true;
            }
        });

        table.add(imageZero).pad(15,30,0,0);
        table.add();
        table.add(imageBoss1).pad(15,100,0,15);
        table.row();
        table.add();
        table.add(imageMegaman).pad(105,250,0,150);
        table.add();
        table.row();
        table.add(imageBoss2).pad(100,30,50,0);
        table.add();
        table.add(imageWho).pad(100,100,50,15);

        stage.addActor(table);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

    }

    public void setLastLevelPlayed(Integer integer){
        lastLevelPlayed = integer;
    }

    public Integer getLastLevelPlayed(){
        return lastLevelPlayed;
    }

    public void setWonLevel(Integer levelWon){

        if (levelWon == 1){
            firstLevelWon = true;
        }
        else if(levelWon == 2){
            secondLevelWon = true;
        }
        else if(levelWon == 3){
            thirdLevelWon = true;
        }
        else if(levelWon == 4){
            fourthLevelWon = true;
        }
        else if (levelWon == 5){
            //final level won. Last screen?
        }
    }

    @Override
    public void render(float delta) {
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
