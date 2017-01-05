package Tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 02/01/2017.
 */

public abstract class InteractiveTileObject {

    //Clase abstracta para crear objetos estaticos y rectangulares.

    protected BodyDef bodyDef = new BodyDef();
    protected PolygonShape polygonShape = new PolygonShape();
    protected CircleShape circleShape = new CircleShape();
    protected FixtureDef fixtureDef = new FixtureDef();
    protected Body body;
    protected Fixture fixture;

    protected TiledMap tiledMap;

    public InteractiveTileObject(MainGameScreen mainGameScreen, Shape2D shape2D){

        //Realizamos un Switch pero sin su forma.
        //Si el shape2d es un rectangulo, entonces ejecutamos lo primero.
        if (shape2D.getClass() == Rectangle.class) {
            World world = mainGameScreen.getWorld();
            tiledMap = mainGameScreen.getTiledMap();
            //Tengo que guardar en memoria el mapa para cada objeto creado, ya que despues...
            //voy a poder manipular su visibilidad en pantalla.
            //Aunque se trata solo de una referencia, no es que se guarda todo en memoria.

            //Decimos que el cuerpo sera estatico.
            bodyDef.type = BodyDef.BodyType.StaticBody;

            //Establecemos la posicion del cuerpo teniendo el objeto rectangulo.
            bodyDef.position.set((((Rectangle)shape2D).getX() + ((Rectangle)shape2D).getWidth() / 2) / MegamanMainClass.PixelsPerMeters, (((Rectangle)shape2D).getY() + ((Rectangle)shape2D).getHeight() / 2) / MegamanMainClass.PixelsPerMeters);

            //Creamos el cuerpo en el mundo.
            body = world.createBody(bodyDef);

            //Creamos una caja para el fixture que contendra el cuerpo.
            polygonShape.setAsBox((((Rectangle)shape2D).getWidth() / 2) / MegamanMainClass.PixelsPerMeters, (((Rectangle)shape2D).getHeight() / 2) / MegamanMainClass.PixelsPerMeters);

            //Al fixture del cuerpo le asignamos la caja anteriormente descrita.
            fixtureDef.shape = polygonShape;

            //Creamos el fixture para nuestro cuerpo.
            fixture = body.createFixture(fixtureDef);
        }
        //En cambio si el shape es un circulo, ejecutamos lo siguiente.
        else if(shape2D.getClass() == Circle.class){
            World world = mainGameScreen.getWorld();
            tiledMap = mainGameScreen.getTiledMap();

            //Tengo que guardar en memoria el mapa para cada objeto creado, ya que despues...
            //voy a poder manipular su visibilidad en pantalla.
            //Aunque se trata solo de una referencia, no es que se guarda todo en memoria.

            //Decimos que el cuerpo sera estatico.
            bodyDef.type = BodyDef.BodyType.StaticBody;

            //Fijarse, quizas hay que sacar el + circle.radius.
            bodyDef.position.set((((Circle)shape2D).x + ((Circle)shape2D).radius) / MegamanMainClass.PixelsPerMeters,(((Circle)shape2D).y + ((Circle)shape2D).radius) / MegamanMainClass.PixelsPerMeters);

            //Creamos el cuerpo en el mundo.
            body = world.createBody(bodyDef);

            //Creamos un circulo para el fixture que contendra el cuerpo.

            circleShape.setRadius(((Circle)shape2D).radius / MegamanMainClass.PixelsPerMeters);

            //Al fixture del cuerpo le asignamos la caja anteriormente descrita.
            fixtureDef.shape = circleShape;

            //Creamos el fixture para nuestro cuerpo.
            fixture = body.createFixture(fixtureDef);
        }
        //Si no es ni circulo ni rectangulo podemos hacer mas cosas.
        else{

        }
    }

    public abstract void onBodyHit();

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
