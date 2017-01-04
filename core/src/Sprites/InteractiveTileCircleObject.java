package Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 03/01/2017.
 */

public abstract class InteractiveTileCircleObject {

    //Clase abstracta para crear objetos estaticos y rectangulares.

    protected BodyDef bodyDef = new BodyDef();
    protected CircleShape circleShape = new CircleShape();
    protected FixtureDef fixtureDef = new FixtureDef();
    protected Body body;
    protected Fixture fixture;

    protected TiledMap tiledMap;

    public InteractiveTileCircleObject(World world,TiledMap tiledMap,Circle circle){

        //Tengo que guardar en memoria el mapa para cada objeto creado, ya que despues...
        //voy a poder manipular su visibilidad en pantalla.
        this.tiledMap = tiledMap;

        //Decimos que el cuerpo sera estatico.
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //Fijarse, quizas hay que sacar el + circle.radius.
        bodyDef.position.set((circle.x + circle.radius) / MegamanMainClass.PixelsPerMeters,(circle.y + circle.radius) / MegamanMainClass.PixelsPerMeters);

        //Creamos el cuerpo en el mundo.
        body = world.createBody(bodyDef);

        //Creamos un circulo para el fixture que contendra el cuerpo.

        circleShape.setRadius(circle.radius / MegamanMainClass.PixelsPerMeters);

        //Al fixture del cuerpo le asignamos la caja anteriormente descrita.
        fixtureDef.shape = circleShape;

        //Creamos el fixture para nuestro cuerpo.
        fixture = body.createFixture(fixtureDef);

    }

    public abstract void onBodyHit();

    public void setCategoryFilter(short filterBit){
        //Creamos un nuevo filtro.
        Filter filter = new Filter();
        //Le ponemos al objeto la categoria del filtro que nos mandan.
        filter.categoryBits = filterBit;
        //Al fixture del objeto le agregamos el filtro creado.
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell[] getCell(){

        //Obtenemos el layer en el que se encuentran los graficos.
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        //Creamos un array que contendra las celdas a eliminar.
        TiledMapTileLayer.Cell[] tiledMapTileLayersCell = new TiledMapTileLayer.Cell[4];

        //Llenamos las celdas rodeando al circulo en (sentido horario ?).
        //Recordar que solo sirve para esta clase de circulos, para otros circulos puede dar errores.

        tiledMapTileLayersCell[0] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 - 1),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 + 1));

        tiledMapTileLayersCell[1] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 + 1));

        tiledMapTileLayersCell[2] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 - 1),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32));

        tiledMapTileLayersCell[3] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32));

        //Devolvemos el array cargado con las celdas a eliminar.
        return tiledMapTileLayersCell;
    }
}
