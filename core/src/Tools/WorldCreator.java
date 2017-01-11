package Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.megamangame.MegamanMainClass;

import java.util.ArrayList;

import Screen.MainGameScreen;
import Sprites.Bunny;
import Sprites.Floor;
import Sprites.FlyingGround;
import Sprites.GreenCircle;
import Sprites.Lava;
import Sprites.RedCircle;

/**
 * Created by Leandro on 02/01/2017.
 */

public class WorldCreator {

    //Creamos el arraylist que contendra a los bunnys.
    private ArrayList<Bunny> arrayListBunny;


    //Nota, borre los circle 2 x 2 and 4 x 4 porque son innecesarios?

    //Aca creamos todos los objetos(Estaticos?) que se encuentren dentro del mundo.
    public WorldCreator(MainGameScreen mainGameScreen){

        World world = mainGameScreen.getWorld();
        TiledMap tiledMap = mainGameScreen.getTiledMap();

        //Obtenemos los objetos floor de tiled map y los creamos mediante la clase Floor.
        for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){

            //Obtenemos cada rectangulo de tiled map.
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            //Creamos los objetos en nuestro mundo.
            new Floor(mainGameScreen,rectangle);
        }

        //Obtenemos los objetos flyingground de tiled map y los creamos mediante la clase FlyinGround.
        for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){

            //Obtenemos cada rectangulo de tiled map.
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            //Creamos los objetos en nuestro mundo.
            new FlyingGround(mainGameScreen,rectangle);
        }

        //Borrados los circle 2 x 2. Dejo comentado.
        for(MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(EllipseMapObject.class)){

            //Obtenemos cada elipse de tiled map.
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();

            //Convertimos la elipse en un circulo.

            Circle circle = new Circle();
            circle.setRadius(ellipse.width / 2);
            circle.setPosition(new Vector2(ellipse.x,ellipse.y));

            //Liberamos la memoria de la ellipse.
            ellipse = null;

            //Creamos los objetos en nuestro mundo.
            new RedCircle(mainGameScreen,circle);
        }



        //Borrados los circle 4 x 4 dejo comentado.

        for(MapObject object : tiledMap.getLayers().get(5).getObjects().getByType(EllipseMapObject.class)){

            //Obtenemos cada elipse de tiled map.
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();

            //Convertimos la elipse en un circulo.

            Circle circle = new Circle();
            circle.setRadius(ellipse.width / 2);
            circle.setPosition(new Vector2(ellipse.x,ellipse.y));

            //Liberamos la memoria de la ellipse.
            ellipse = null;

            //Creamos los objetos en nuestro mundo.
            new GreenCircle(mainGameScreen,circle);
        }


        //Creamos la lava.
        for(MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            //Obtenemos cada rectangulo de tiled map.
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            //Creamos los objetos en nuestro mundo.
            new Lava(mainGameScreen,rectangle);
        }

        //Creamos el arraylist.
        arrayListBunny = new ArrayList<Bunny>();

        //Creamos los maravillosos Bunnys!!
        for(MapObject object : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            arrayListBunny.add(new Bunny(mainGameScreen, rectangle.x / MegamanMainClass.PixelsPerMeters, rectangle.y / MegamanMainClass.PixelsPerMeters, rectangle.getWidth() / MegamanMainClass.PixelsPerMeters, rectangle.getHeight() / MegamanMainClass.PixelsPerMeters));
        }

    }

    public ArrayList<Bunny> getBunnys(){
        return arrayListBunny;
    }
}
