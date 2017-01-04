package Sprites;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 02/01/2017.
 */

public class Floor extends  InteractiveTileObject{

    public Floor(World world, TiledMap tiledMap, Rectangle rectangle){

        //Como floor es un objeto estatico y rectangulo, utilizamos super();
        super(world,tiledMap,rectangle);

        //Le mandamos al user data de cada fixture la clase de objeto que es.
        fixture.setUserData(this);

        //Establecemos el filtro de cada circulo que creamos.
        setCategoryFilter(MegamanMainClass.FLOOR_BIT);
    }

    @Override
    public void onBodyHit() {

    }
}
