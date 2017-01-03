package Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import Sprites.Floor;
import Sprites.FlyingGround;

/**
 * Created by Leandro on 02/01/2017.
 */

public class WorldCreator {

    //Aca creamos todos los objetos(Estaticos?) que se encuentren dentro del mundo.
    public WorldCreator(World world, TiledMap tiledMap){

        //Obtenemos los objetos floor de tiled map y los creamos mediante la clase Floor.
        for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){

            //Obtenemos cada rectangulo de tiled map.
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            //Creamos los objetos en nuestro mundo.
            new Floor(world,tiledMap,rectangle);
        }

        //Obtenemos los objetos flyingground de tiled map y los creamos mediante la clase FlyinGround.
        for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){

            //Obtenemos cada rectangulo de tiled map.
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            //Creamos los objetos en nuestro mundo.
            new FlyingGround(world,tiledMap,rectangle);
        }

    }
}
