package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

    private ProgressBar healthBarMegaman;
    private ProgressBar manaBarMegaman;
    private ProgressBar healthBarZero;
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

    private TextureAtlas textureAtlas;

    private TextureRegion textureRegion;

    //Nota: primero me quedo la impresion de que las barras estan medio jarcodeadas(si,hardcodeadas).
    //Pero luego me convenci de que no estan taaan mal, sin embargo, una revision luego no vendria mal.

    public Hud(SpriteBatch spriteBatch, TextureAtlas textureAtlas, boolean dispositivoEsAndroid){

        //Guardamos el textureAtlas.
        this.textureAtlas = textureAtlas;

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


        //Aqui tengo que crear los textureRegion solicitados.
        textureRegion = textureAtlas.findRegion("graybar");

        progressBarStyleRed.background = new TextureRegionDrawable(textureRegion);
        progressBarStyleBlue.background = new TextureRegionDrawable(textureRegion);

        textureRegion = textureAtlas.findRegion("redbar");

        //Creamos un textureregiondrawable para modificar las propiedades del knob.
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);

        //Establecemos el tamaño del texturereciondrawable(future knob propery).
        textureRegionDrawable.setMinWidth(139);
        textureRegionDrawable.setMinHeight(29);

        //Creamos el knob con el tamaño especificado.
        progressBarStyleRed.knob = textureRegionDrawable;

        textureRegion = textureAtlas.findRegion("bluebar");

        //Creamos un knob diferente con el mismo tamaño pero azul.
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);

        textureRegionDrawable.setMinWidth(139);
        textureRegionDrawable.setMinHeight(29);

        progressBarStyleBlue.knob = textureRegionDrawable;

        //Creamos las barras de vida y mana con las especificaciones anteriores.
        healthBarMegaman = new ProgressBar(0,139,10,false,progressBarStyleRed);
        manaBarMegaman = new ProgressBar(0,100,1,false,progressBarStyleBlue);

        //Añadimos a la tabla todo lo anterior, con expand entran varios objetos en una fila.
        table.add(healthLabel).expandX().pad(5);
        table.add(manaLabel).expandX().pad(5);
        table.add(scoreLabel).expandX().pad(5);
        //Con row se crea una fila debajo de la anterior.
        table.row();
        table.add(healthBarMegaman).expandX().pad(5);
        table.add(manaBarMegaman).expandX().pad(5);
        table.add(scoreNumberLabel).expandX().pad(5);

        //Añadimos la tabla al stage.
        stage.addActor(table);
    }

    public void setGameIsInFinalStage(){

        Table table = new Table();

        table.right();

        table.setFillParent(true);

        progressBarStyleRed = new ProgressBar.ProgressBarStyle();

        textureRegion = textureAtlas.findRegion("graybar");

        progressBarStyleRed.background = new TextureRegionDrawable(textureRegion);

        progressBarStyleRed.background.setMinWidth(0);
        progressBarStyleRed.background.setMinHeight(0);

        textureRegion = new TextureRegion(textureAtlas.findRegion("redbar").getTexture(),133,72,120,13);

        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);

        textureRegionDrawable.setMinWidth(29);
        textureRegionDrawable.setMinHeight(239);

        progressBarStyleRed.knob = textureRegionDrawable;

        //Creamos las barras de vida y mana con las especificaciones anteriores.
        healthBarZero = new ProgressBar(0,239,20,true,progressBarStyleRed);

        table.add(healthBarZero).expandY().padRight(30);

        stage.addActor(table);
    }

    public void crearHudBotIzq() {

        final Table table = new Table();
        table.left().bottom();

        textureRegion = textureAtlas.findRegion("upArrow");

        final Image upImage = new Image(textureRegion);
        upImage.setSize(50, 50);
        upImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upArrowPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        textureRegion = textureAtlas.findRegion("downArrow");

        Image downImage = new Image(textureRegion);
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

        textureRegion = textureAtlas.findRegion("leftArrow");

        Image leftImage = new Image(textureRegion);
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

        textureRegion = textureAtlas.findRegion("rightArrow");

        Image rightImage = new Image(textureRegion);
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

        textureRegion = textureAtlas.findRegion("triangleButton");

        Image upImage = new Image(textureRegion);
        upImage.setSize(50, 50);
        upImage.addListener(new InputListener() {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upButtonPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }
        });


        textureRegion = textureAtlas.findRegion("xButton");

        Image downImage = new Image(textureRegion);
        downImage.setSize(50, 50);
        downImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downButtonPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        textureRegion = textureAtlas.findRegion("squareButton");

        Image leftImage = new Image(textureRegion);
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

        textureRegion = textureAtlas.findRegion("circleButton");

        Image rightImage = new Image(textureRegion);
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

    public void setUpArrowPressed(boolean bool){
        upArrowPressed = bool;
    }

    public void setDownButtonPressed(boolean bool){
        downButtonPressed = bool;
    }

    public void setRightButtonPressed(boolean bool){
        rightButtonPressed = bool;
    }

    public void setLeftButtonPressed(boolean bool){
        leftButtonPressed = bool;
    }

    public void setUpButtonPressed(boolean bool){
        upButtonPressed = bool;
    }

    public boolean dañarMegamanPersonaje(float health){

        //Vemos cuanta vida tiene el personaje.
        float healthAnterior = healthBarMegaman.getStyle().knob.getMinWidth();

        //Le restamos el numero que llega a la vida que teniamos.
        float healthNuevo = healthAnterior - health;

        if (healthNuevo > 0) {
            //Hacemos que la barra pierda vida(haciendo desaparecer al knob).
            healthBarMegaman.getStyle().knob.setMinWidth(healthNuevo);

            //Como la vida es superior a 0, devolvemos false.
            //Porque el personaje no murio.
            return false;
        }else{
            //Tenemos que dejar la barra de vida del personaje en 0.
            healthBarMegaman.getStyle().knob.setMinWidth(0);

            //Tenemos que avisar que, lamentablemente, el personaje murio.
            return true;
        }
    }

    public boolean dañarZeroPersonaje(){

        //Vemos cuanta vida tiene el personaje.
        float healthAnterior = healthBarZero.getStyle().knob.getMinHeight();

        //Le restamos el numero que llega a la vida que teniamos.
        float healthNuevo = healthAnterior - 50;

        if (healthNuevo > 0) {
            //Hacemos que la barra pierda vida(haciendo desaparecer al knob).
            healthBarZero.getStyle().knob.setMinHeight(healthNuevo);

            //Como la vida es superior a 0, devolvemos false.
            //Porque el personaje no murio.
            return false;
        }else{
            //Tenemos que dejar la barra de vida del personaje en 0.
            healthBarZero.getStyle().knob.setMinHeight(0);

            //Tenemos que avisar que, lamentablemente, el personaje murio.
            return true;
        }
    }

    public void gastarMana(float mana){

        float manaAnterior = manaBarMegaman.getStyle().knob.getMinWidth();

        float manaNuevo = manaAnterior - mana;

        if (manaNuevo > 0) {
            manaBarMegaman.getStyle().knob.setMinWidth(manaNuevo);
        }else{
            manaBarMegaman.getStyle().knob.setMinWidth(0);
        }
    }

    public void curarPersonaje(float health){

        float healthAnterior = healthBarMegaman.getStyle().knob.getMinWidth();

        float healthNuevo = healthAnterior + health;

        if (healthNuevo > 139) {
            healthBarMegaman.getStyle().knob.setMinWidth(139);
        }else{
            healthBarMegaman.getStyle().knob.setMinWidth(healthNuevo);
        }
    }

    public void recuperarMana(float mana){

        float manaAnterior = manaBarMegaman.getStyle().knob.getMinWidth();

        float manaNuevo = manaAnterior + mana;

        if (manaNuevo > 139) {
            manaBarMegaman.getStyle().knob.setMinWidth(139);
        }else{
            manaBarMegaman.getStyle().knob.setMinWidth(manaNuevo);
        }
    }

    public Integer getScore(){
        return score;
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

}
