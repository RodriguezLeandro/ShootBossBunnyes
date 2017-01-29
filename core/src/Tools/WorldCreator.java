package Tools;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.megamangame.MegamanMainClass;

import java.util.ArrayList;

import Screen.Level1Screen;
import Screen.Level2Screen;
import Screen.Level3Screen;
import Screen.Level4Screen;
import Sprites.Asteroid;
import Sprites.Bat;
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

    private ArrayList<Bat> arrayListBat;

    private ArrayList<Asteroid> arrayListAsteroid;

    //Nota, borre los circle 2 x 2 and 4 x 4 porque son innecesarios?

    //Aca creamos todos los objetos(Estaticos?) que se encuentren dentro del mundo.
    public WorldCreator(Object screen){

        if (screen instanceof Level1Screen){
            TiledMap tiledMap = ((Level1Screen)screen).getTiledMap();

            //Obtenemos los objetos floor de tiled map y los creamos mediante la clase Floor.
            for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){

                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new Floor(((Level1Screen)screen),rectangle);
            }

            //Obtenemos los objetos flyingground de tiled map y los creamos mediante la clase FlyinGround.
            for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){

                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new FlyingGround(((Level1Screen)screen),rectangle);
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
                new RedCircle(((Level1Screen)screen),circle);
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
                new GreenCircle(((Level1Screen)screen),circle);
            }


            //Creamos la lava.
            for(MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new Lava(((Level1Screen)screen),rectangle);
            }

            //Creamos el arraylist.
            arrayListBunny = new ArrayList<Bunny>();

            //Creamos los maravillosos Bunnys!!
            for(MapObject object : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){

                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                arrayListBunny.add(new Bunny(((Level1Screen)screen), rectangle.x / MegamanMainClass.PixelsPerMeters, rectangle.y / MegamanMainClass.PixelsPerMeters, rectangle.getWidth() / MegamanMainClass.PixelsPerMeters, rectangle.getHeight() / MegamanMainClass.PixelsPerMeters));
            }
        }
        else if(screen instanceof Level2Screen){

            TiledMap tiledMap = ((Level2Screen)screen).getTiledMap();

            for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new Floor(((Level2Screen)screen),rectangle);
            }

            for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new FlyingGround(((Level2Screen)screen),rectangle);
            }

            arrayListBat = new ArrayList<Bat>();

            for(MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                arrayListBat.add(new Bat(((Level2Screen)screen),rectangle.x / MegamanMainClass.PixelsPerMeters,rectangle.y / MegamanMainClass.PixelsPerMeters,rectangle.getWidth() / MegamanMainClass.PixelsPerMeters, rectangle.getHeight() / MegamanMainClass.PixelsPerMeters));
            }


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
                new RedCircle(((Level2Screen)screen),circle);
            }



            //Borrados los circle 4 x 4 dejo comentado.

            for(MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(EllipseMapObject.class)){

                //Obtenemos cada elipse de tiled map.
                Ellipse ellipse = ((EllipseMapObject) object).getEllipse();

                //Convertimos la elipse en un circulo.

                Circle circle = new Circle();
                circle.setRadius(ellipse.width / 2);
                circle.setPosition(new Vector2(ellipse.x,ellipse.y));

                //Liberamos la memoria de la ellipse.
                ellipse = null;

                //Creamos los objetos en nuestro mundo.
                new GreenCircle(((Level2Screen)screen),circle);
            }
        }
        else if (screen instanceof Level3Screen){

            TiledMap tiledMap = ((Level3Screen)screen).getTiledMap();

            for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new Floor(((Level3Screen)screen),rectangle);
            }

            for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(EllipseMapObject.class)){

                //Obtenemos cada elipse de tiled map.
                Ellipse ellipse = ((EllipseMapObject) object).getEllipse();

                //Convertimos la elipse en un circulo.

                Circle circle = new Circle();
                circle.setRadius(ellipse.width / 2);
                circle.setPosition(new Vector2(ellipse.x,ellipse.y));

                //Liberamos la memoria de la ellipse.
                ellipse = null;

                //Creamos los objetos en nuestro mundo.
                new RedCircle(((Level3Screen)screen),circle);
            }



            //Borrados los circle 4 x 4 dejo comentado.

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
                new GreenCircle(((Level3Screen)screen),circle);
            }

            for(MapObject object : tiledMap.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new FlyingGround(((Level3Screen)screen),rectangle);
            }

            arrayListAsteroid = new ArrayList<Asteroid>();
            for(MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(EllipseMapObject.class)){
                //Obtenemos cada elipse de tiled map.
                Ellipse ellipse = ((EllipseMapObject) object).getEllipse();

                //Convertimos la elipse en un circulo.

                Circle circle = new Circle();
                circle.setRadius(ellipse.width / 2);
                circle.setPosition(new Vector2(ellipse.x,ellipse.y));

                //Liberamos la memoria de la ellipse.
                ellipse = null;

                //Creamos los objetos en nuestro mundo.
                arrayListAsteroid.add(new Asteroid(((Level3Screen)screen),circle.x / MegamanMainClass.PixelsPerMeters,circle.y / MegamanMainClass.PixelsPerMeters));
            }

        }
        else if (screen instanceof Level4Screen){
            TiledMap tiledMap = ((Level4Screen)screen).getTiledMap();

            for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new Floor(((Level4Screen)screen),rectangle);
            }

            for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new Lava(((Level4Screen)screen),rectangle);
            }

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
                new RedCircle(((Level4Screen)screen),circle);
            }

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
                new GreenCircle(((Level4Screen)screen),circle);
            }

            for(MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                new FlyingGround(((Level4Screen)screen),rectangle);
            }

            arrayListBat = new ArrayList<Bat>();

            for(MapObject object : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
                //Obtenemos cada rectangulo de tiled map.
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                //Creamos los objetos en nuestro mundo.
                arrayListBat.add(new Bat(((Level4Screen)screen),rectangle.x / MegamanMainClass.PixelsPerMeters,rectangle.y / MegamanMainClass.PixelsPerMeters,rectangle.getWidth() / MegamanMainClass.PixelsPerMeters, rectangle.getHeight() / MegamanMainClass.PixelsPerMeters));
            }

            arrayListBunny = new ArrayList<Bunny>();

            //Creamos los maravillosos Bunnys!!
            for(MapObject object : tiledMap.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){

                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                arrayListBunny.add(new Bunny(((Level4Screen)screen), rectangle.x / MegamanMainClass.PixelsPerMeters, rectangle.y / MegamanMainClass.PixelsPerMeters, rectangle.getWidth() / MegamanMainClass.PixelsPerMeters, rectangle.getHeight() / MegamanMainClass.PixelsPerMeters));
            }


            arrayListAsteroid = new ArrayList<Asteroid>();

            for(MapObject object : tiledMap.getLayers().get(9).getObjects().getByType(EllipseMapObject.class)){
                //Obtenemos cada elipse de tiled map.
                Ellipse ellipse = ((EllipseMapObject) object).getEllipse();

                //Convertimos la elipse en un circulo.

                Circle circle = new Circle();
                circle.setRadius(ellipse.width / 2);
                circle.setPosition(new Vector2(ellipse.x,ellipse.y));

                //Liberamos la memoria de la ellipse.
                ellipse = null;

                //Creamos los objetos en nuestro mundo.
                arrayListAsteroid.add(new Asteroid(((Level4Screen)screen),circle.x / MegamanMainClass.PixelsPerMeters,circle.y / MegamanMainClass.PixelsPerMeters));
            }

        }
        else {
            //Aqui va el codigo para el levelfinalscreen?.
        }

    }

    public ArrayList<Bunny> getBunnys(){
        return arrayListBunny;
    }

    public ArrayList<Bat> getBats(){
        return arrayListBat;
    }

    public ArrayList<Asteroid> getAsteroids(){
        return  arrayListAsteroid;
    }
}
