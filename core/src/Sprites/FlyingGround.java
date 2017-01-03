package Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Leandro on 02/01/2017.
 */

public class FlyingGround extends InteractiveTileObject {

    public FlyingGround(World world, TiledMap tiledMap, Rectangle rectangle){

        //Como floor es un objeto estatico y rectangulo, utilizamos super();
        super(world,tiledMap,rectangle);
    }
}
