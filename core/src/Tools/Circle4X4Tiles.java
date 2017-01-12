package Tools;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Shape2D;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;

/**
 * Created by Leandro on 04/01/2017.
 */

public class Circle4X4Tiles extends InteractiveTileObject {

    protected TiledMapTileLayer.Cell[] tiledMapTileLayersCell;

    public Circle4X4Tiles(MainGameScreen mainGameScreen, Shape2D shape2D) {
        super(mainGameScreen, shape2D);

        //Le mandamos al user data de cada fixture la clase de objeto que es.
        fixture.setUserData(this);

        //Establecemos el filtro de cada circulo que creamos.
        setCategoryFilter(MegamanMainClass.COIN_BIT);
    }

    @Override
    public void onBodyHit() {
        //Decimos que cuando chocamos el circulo grande verde obtenga la propiedad filtro: "destruido".
        setCategoryFilter(MegamanMainClass.DESTROYED_BIT);

        //Llenamos el array.
        tiledMapTileLayersCell = getCell();

        for(int i = 0; i < 16; i ++){
            if(tiledMapTileLayersCell[i] != null){
                tiledMapTileLayersCell[i].setTile(null);
            }
            else{
                //No hacemos nada con el tile que no existe.
            }
        }
        //Liberamos la memoria que ocupaba el array.
        tiledMapTileLayersCell = null;
    }

    public TiledMapTileLayer.Cell[] getCell(){

        //Obtenemos el layer en el que se encuentran los graficos.
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        //Creamos un array que contendra las celdas a eliminar.
        TiledMapTileLayer.Cell[] tiledMapTileLayersCell = new TiledMapTileLayer.Cell[16];

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

        tiledMapTileLayersCell[4] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 - 1 ),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 - 1));

        tiledMapTileLayersCell[5] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 - 1));

        tiledMapTileLayersCell[6] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 + 2));

        tiledMapTileLayersCell[7] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 - 1),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 + 2));

        tiledMapTileLayersCell[8] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 + 1),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32));

        tiledMapTileLayersCell[9] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 + 1),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 + 1));

        tiledMapTileLayersCell[10] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 + 1),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 - 1));

        tiledMapTileLayersCell[11] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 + 1),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 + 2));

        tiledMapTileLayersCell[12] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 - 2),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32));

        tiledMapTileLayersCell[13] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 - 2),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 + 1));

        tiledMapTileLayersCell[14] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 - 2),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 + 2));

        tiledMapTileLayersCell[15] = layer.getCell((int)(body.getPosition().x * MegamanMainClass.PixelsPerMeters / 32 - 2),
                (int)(body.getPosition().y * MegamanMainClass.PixelsPerMeters / 32 - 1));


        //Devolvemos el array cargado con las celdas a eliminar.
        return tiledMapTileLayersCell;
    }
}
