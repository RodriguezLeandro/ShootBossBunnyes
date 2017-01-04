package Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 03/01/2017.
 */

public class RedCircle extends InteractiveTileCircleObject {

    public RedCircle(World world, TiledMap tiledMap, Circle circle){
        super(world,tiledMap,circle);

        //Le mandamos al user data de cada fixture la clase de objeto que es.
        fixture.setUserData(this);

        //Establecemos el filtro de cada circulo que creamos.
        setCategoryFilter(MegamanMainClass.COIN_BIT);
    }

    @Override
    public void onBodyHit() {

        //Decimos que cuando chocamos el circulo rojo obtenga la propiedad filtro: "destruido".
        setCategoryFilter(MegamanMainClass.DESTROYED_BIT);

        //Creamos un array de tiledmaptilelayercells.
        TiledMapTileLayer.Cell[] tiledMapTileLayersCell = new TiledMapTileLayer.Cell[4];

        //Llenamos el array.
        tiledMapTileLayersCell = getCell();

        //Aca podria surroundear con un try-catch, para prevenir errores, pero por el momento
        //lo dejo asi.

        tiledMapTileLayersCell[0].setTile(null);
        tiledMapTileLayersCell[1].setTile(null);
        tiledMapTileLayersCell[2].setTile(null);
        tiledMapTileLayersCell[3].setTile(null);

        //Liberamos la memoria que ocupaba el array.
        tiledMapTileLayersCell = null;

        //Suena la cancion 1, no se escucha bien, pongo la 2.
        MegamanMainClass.assetManager.get("audio/introsong.mp3", Sound.class).play();
    }
}
