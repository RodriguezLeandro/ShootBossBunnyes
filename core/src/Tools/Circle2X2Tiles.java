package Tools;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Shape2D;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 04/01/2017.
 */

public abstract class Circle2X2Tiles extends InteractiveTileObject {

    protected TiledMapTileLayer.Cell[] tiledMapTileLayersCell;

    public Circle2X2Tiles(MainGameScreen mainGameScreen, Shape2D shape2D) {
        super(mainGameScreen, shape2D);

    }

    @Override
    public void onBodyHit() {
        //Decimos que cuando chocamos el circulo rojo obtenga la propiedad filtro: "destruido".
        setCategoryFilter(MegamanMainClass.DESTROYED_BIT);

        //Llenamos el array.
        tiledMapTileLayersCell = getCell();

        //Aca podria surroundear con un try-catch, para prevenir errores, pero por el momento
        //lo dejo asi.
        //Termine verificando si el tiledmap era nulo, creo que es suficiente con eso.

        for (int i = 0; i < 4; i++){
            if (tiledMapTileLayersCell[i] != null){
                tiledMapTileLayersCell[i].setTile(null);
            }
            else {
                //No hago nada aunque el tile sea nulo.
            }
        }
        //Liberamos la memoria que ocupaba el array.
        tiledMapTileLayersCell = null;

        //Suena la cancion 1, no se escucha bien, pongo la 2.
        MegamanMainClass.assetManager.get("audio/introsong.mp3", Sound.class).play();
    }
    //Esta funcion solo sirve para nuestra clase CirculoPequeño.
    //Sin embargo, esta clase puede obtener cualquier objeto circular.
    //Muy bueno.
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
