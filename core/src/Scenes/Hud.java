package Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

public class Hud {

    public Stage stage;
    public Viewport viewport;

    private Integer score;

    Label healthLabel;
    Label manaLabel;
    Label scoreLabel;
    Label scoreNumberLabel;

    Label.LabelStyle labelStyle;

    ProgressBar healthBar;
    ProgressBar manaBar;
    ProgressBar.ProgressBarStyle progressBarStyleRed;
    ProgressBar.ProgressBarStyle progressBarStyleBlue;

    //Nota: primero me quedo la impresion de que las barras estan medio jarcodeadas(si,hardcodeadas).
    //Pero luego me convenci de que no estan taaan mal, sin embargo, una revision luego no vendria mal.

    public Hud(SpriteBatch spriteBatch){

        //Anadimos el score del juego.
        score = 0;

        //Creamos un viewport distinto al principal con una camara nueva. Para que quede fija.
        viewport = new FitViewport(MegamanMainClass.Virtual_Width, MegamanMainClass.Virtual_Height , new OrthographicCamera());

        //Creamos un nuevo stage, le pasamos el viewport y el spritebatch.
        stage = new Stage(viewport,spriteBatch);

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

}
