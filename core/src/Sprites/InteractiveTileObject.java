package Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 02/01/2017.
 */

public abstract class InteractiveTileObject {

    //Clase abstracta para crear objetos estaticos y rectangulares.

    protected BodyDef bodyDef = new BodyDef();
    protected PolygonShape polygonShape = new PolygonShape();
    protected FixtureDef fixtureDef = new FixtureDef();
    protected Body body;

    public InteractiveTileObject(World world, TiledMap tiledMap, Rectangle rectangle){

        //Decimos que el cuerpo sera estatico.
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //Establecemos la posicion del cuerpo teniendo el objeto rectangulo.
        bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2) / MegamanMainClass.PixelsPerMeters,(rectangle.getY()+rectangle.getHeight()/2) / MegamanMainClass.PixelsPerMeters);

        //Creamos el cuerpo en el mundo.
        body = world.createBody(bodyDef);

        //Creamos una caja para el fixture que contendra el cuerpo.
        polygonShape.setAsBox((rectangle.getWidth()/2) / MegamanMainClass.PixelsPerMeters,(rectangle.getHeight()/2) / MegamanMainClass.PixelsPerMeters);

        //Al fixture del cuerpo le asignamos la caja anteriormente descrita.
        fixtureDef.shape = polygonShape;

        //Creamos el fixture para nuestro cuerpo.
        body.createFixture(fixtureDef);
    }

}
