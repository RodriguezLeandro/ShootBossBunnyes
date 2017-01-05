package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 04/01/2017.
 */

//Nota, creo que no necesito hacer resize, pero considerarlo en un futuro.

public class Hud {

    public Stage stage;
    public Viewport viewport;

    private Integer score;

    private Label healthLabel;
    private Label manaLabel;
    private Label scoreLabel;
    private Label scoreNumberLabel;

    private Label.LabelStyle labelStyle;

    private ProgressBar healthBar;
    private ProgressBar manaBar;
    private ProgressBar.ProgressBarStyle progressBarStyleRed;
    private ProgressBar.ProgressBarStyle progressBarStyleBlue;

    private boolean upArrowPressed;
    private boolean downArrowPressed;
    private boolean leftArrowPressed;
    private boolean rightArrowPressed;

    private boolean upButtonPressed;
    private boolean downButtonPressed;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    private boolean upArrowReleased;


    //Nota: primero me quedo la impresion de que las barras estan medio jarcodeadas(si,hardcodeadas).
    //Pero luego me convenci de que no estan taaan mal, sin embargo, una revision luego no vendria mal.

    public Hud(SpriteBatch spriteBatch, boolean dispositivoEsAndroid){

        //Cuando creamos el hud, nadie toca la flecha up.
        upArrowReleased = true;

        //Si el dispositivo es android, creamos todo lo del hud.
        if (dispositivoEsAndroid) {
            //Anadimos el score del juego.
            score = 0;

            //Creamos un viewport distinto al principal con una camara nueva. Para que quede fija.
            viewport = new FitViewport(MegamanMainClass.Virtual_Width, MegamanMainClass.Virtual_Height, new OrthographicCamera());

            //Creamos un nuevo stage, le pasamos el viewport y el spritebatch.
            stage = new Stage(viewport, spriteBatch);

            //Decimos que nuestro stage es un inputproccesor.
            Gdx.input.setInputProcessor(stage);

            crearHudTop();

            crearHudBotIzq();

            crearHudBotDer();
        }
        //Si el dispositivo no es android, solo creamos y mostramos el topHud.
        else{
            //Anadimos el score del juego.
            score = 0;

            //Creamos un viewport distinto al principal con una camara nueva. Para que quede fija.
            viewport = new FitViewport(MegamanMainClass.Virtual_Width, MegamanMainClass.Virtual_Height, new OrthographicCamera());

            //Creamos un nuevo stage, le pasamos el viewport y el spritebatch.
            stage = new Stage(viewport, spriteBatch);

            //Decimos que nuestro stage es un inputproccesor.
            Gdx.input.setInputProcessor(stage);

            crearHudTop();
        }
    }

    public void crearHudTop(){
        //Creamos una nuva tabla.
        Table table = new Table();
        //Decimos que la tabla este en la parte superior de la camara??.
        table.top();
        //Que la tabla llene lo que le falte en pantalla.
        table.setFillParent(true);

        //Creamos un labelstyle, para luego ponerle fuente a las letras o tamaño.
        labelStyle = new Label.LabelStyle();

        //Ponemos fuente del labelstyle.
        labelStyle.font = new BitmapFont();
        //Ponemos color del labelstyle.
        labelStyle.fontColor = Color.WHITE;

        //Creamos el label health.
        healthLabel = new Label("Health", labelStyle);
        //Creamos el label mana.
        manaLabel = new Label("Mana", labelStyle);
        //Creamos el label score.
        scoreLabel = new Label("Score", labelStyle);
        //Creamos el label scoreData.
        scoreNumberLabel = new Label(String.format("%06d",score),labelStyle);

        //Aumentamos el tamaño de los labels.
        healthLabel.setFontScale(2);
        manaLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        scoreNumberLabel.setFontScale(2);

        //Creamos una barra de progreso roja.
        progressBarStyleRed = new ProgressBar.ProgressBarStyle();
        //Creamos una barra de progreso azul.
        progressBarStyleBlue = new ProgressBar.ProgressBarStyle();

        //Creamos un progressbarstyle background que contendra la vida perdida...
        //Siempre tendremos el background en gris, y cuando el player gaste vida o mana,
        //iremos sacando de a poco el knob que se encuentra arriba del background,
        //por lo que dara la impresion de perder vida, aunque solo estemos sacando el knob.
        progressBarStyleRed.background = new TextureRegionDrawable(new TextureRegion(new Texture("bar/graybar.png")));
        progressBarStyleBlue.background = new TextureRegionDrawable(new TextureRegion(new Texture("bar/graybar.png")));

        //Creamos un textureregiondrawable para modificar las propiedades del knob.
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("bar/redbar.png")));

        //Establecemos el tamaño del texturereciondrawable(future knob propery).
        textureRegionDrawable.setMinWidth(139);
        textureRegionDrawable.setMinHeight(29);

        //Creamos el knob con el tamaño especificado.
        progressBarStyleRed.knob = textureRegionDrawable;

        //Creamos un knob diferente con el mismo tamaño pero azul.
        textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("bar/bluebar.png")));

        textureRegionDrawable.setMinWidth(139);
        textureRegionDrawable.setMinHeight(29);

        progressBarStyleBlue.knob = textureRegionDrawable;

        //Creamos las barras de vida y mana con las especificaciones anteriores.
        healthBar = new ProgressBar(0,139,10,false,progressBarStyleRed);
        manaBar = new ProgressBar(0,100,1,false,progressBarStyleBlue);

        //Añadimos a la tabla todo lo anterior, con expand entran varios objetos en una fila.
        table.add(healthLabel).expandX().pad(5);
        table.add(manaLabel).expandX().pad(5);
        table.add(scoreLabel).expandX().pad(5);
        //Con row se crea una fila debajo de la anterior.
        table.row();
        table.add(healthBar).expandX().pad(5);
        table.add(manaBar).expandX().pad(5);
        table.add(scoreNumberLabel).expandX().pad(5);

        //Añadimos la tabla al stage.
        stage.addActor(table);
    }

    public void crearHudBotIzq() {

        Table table = new Table();
        table.left().bottom();

        Image upImage = new Image(new Texture("arrow/upArrow.png"));
        upImage.setSize(50, 50);
        upImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upArrowPressed = true;
                upArrowReleased = false;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upArrowPressed = false;
                upArrowReleased = true;
            }
        });

        Image downImage = new Image(new Texture("arrow/downArrow.png"));
        downImage.setSize(50, 50);
        downImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downArrowPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downArrowPressed = false;
            }
        });

        Image leftImage = new Image(new Texture("arrow/leftArrow.png"));
        leftImage.setSize(50, 50);
        leftImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftArrowPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftArrowPressed = false;
            }
        });

        Image rightImage = new Image(new Texture("arrow/rightArrow.png"));
        rightImage.setSize(50, 50);
        rightImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightArrowPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightArrowPressed = false;
            }
        });

        table.pad(0,20,20,0);
        table.add();
        table.add(upImage).size(upImage.getWidth(),upImage.getHeight());
        table.add();

        table.row().pad(5,5,5,5);

        table.add(leftImage).size(leftImage.getWidth(),leftImage.getHeight());
        table.add();
        table.add(rightImage).size(rightImage.getWidth(),rightImage.getHeight());

        table.row().padTop(5);

        table.add();
        table.add(downImage).size(downImage.getWidth(),downImage.getHeight());
        table.add();

        stage.addActor(table);
    }

    public void crearHudBotDer(){

        Table table = new Table();
        table.right().bottom();
        table.setFillParent(true);

        Image upImage = new Image(new Texture("button/triangleButton.png"));
        upImage.setSize(50, 50);
        upImage.addListener(new InputListener() {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upButtonPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upButtonPressed = false;
            }
        });


        Image downImage = new Image(new Texture("button/xButton.png"));
        downImage.setSize(50, 50);
        downImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downButtonPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downButtonPressed = false;
            }
        });

        Image leftImage = new Image(new Texture("button/squareButton.png"));
        leftImage.setSize(50, 50);
        leftImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftButtonPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftButtonPressed = false;
            }
        });

        Image rightImage = new Image(new Texture("button/circleButton.png"));
        rightImage.setSize(50, 50);
        rightImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightButtonPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightButtonPressed = false;
            }
        });

        table.pad(0,0,20,20);
        table.add();
        table.add(upImage).size(upImage.getWidth(),upImage.getHeight());
        table.add();

        table.row().pad(5,5,5,5);

        table.add(leftImage).size(leftImage.getWidth(),leftImage.getHeight());
        table.add();
        table.add(rightImage).size(rightImage.getWidth(),rightImage.getHeight());

        table.row().padTop(5);

        table.add();
        table.add(downImage).size(downImage.getWidth(),downImage.getHeight());
        table.add();

        stage.addActor(table);
    }

    public boolean dañarPersonaje(float health){

        //Vemos cuanta vida tiene el personaje.
        float healthAnterior = healthBar.getStyle().knob.getMinWidth();

        //Le restamos el numero que llega a la vida que teniamos.
        float healthNuevo = healthAnterior - health;

        if (healthNuevo > 0) {
            //Hacemos que la barra pierda vida(haciendo desaparecer al knob).
            healthBar.getStyle().knob.setMinWidth(healthNuevo);

            //Como la vida es superior a 0, devolvemos false.
            //Porque el personaje no murio.
            return false;
        }else{
            //Tenemos que dejar la barra de vida del personaje en 0.
            healthBar.getStyle().knob.setMinWidth(0);

            //Tenemos que avisar que, lamentablemente, el personaje murio.
            return true;
        }
    }

    public void gastarMana(float mana){

        float manaAnterior = manaBar.getStyle().knob.getMinWidth();

        float manaNuevo = manaAnterior - mana;

        if (manaNuevo > 0) {
            manaBar.getStyle().knob.setMinWidth(manaNuevo);
        }else{
            manaBar.getStyle().knob.setMinWidth(0);
        }
    }

    public void curarPersonaje(float health){

        float healthAnterior = healthBar.getStyle().knob.getMinWidth();

        float healthNuevo = healthAnterior + health;

        if (healthNuevo > 139) {
            healthBar.getStyle().knob.setMinWidth(139);
        }else{
            healthBar.getStyle().knob.setMinWidth(healthNuevo);
        }
    }

    public void recuperarMana(float mana){

        float manaAnterior = manaBar.getStyle().knob.getMinWidth();

        float manaNuevo = manaAnterior + mana;

        if (manaNuevo > 139) {
            manaBar.getStyle().knob.setMinWidth(139);
        }else{
            manaBar.getStyle().knob.setMinWidth(manaNuevo);
        }
    }

    public boolean isUpArrowPressed() {
        return upArrowPressed;
    }

    public boolean isDownArrowPressed() {
        return downArrowPressed;
    }

    public boolean isLeftArrowPressed() {
        return leftArrowPressed;
    }

    public boolean isRightArrowPressed() {
        return rightArrowPressed;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isDownButtonPressed() {
        return downButtonPressed;
    }

    public boolean isUpButtonPressed() {
        return upButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public boolean isUpArrowReleased(){
        return upArrowReleased;
    }
}
