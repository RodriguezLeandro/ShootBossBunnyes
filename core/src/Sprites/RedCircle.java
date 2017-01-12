package Sprites;

import com.badlogic.gdx.math.Circle;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;
import Tools.Circle2X2Tiles;

/**
 * Created by Leandro on 03/01/2017.
 */

public class RedCircle extends Circle2X2Tiles{

    public RedCircle(MainGameScreen mainGameScreen, Circle circle){
        super(mainGameScreen,circle);

        //Le mandamos al user data de cada fixture la clase de objeto que es.
        fixture.setUserData(this);

        //Establecemos el filtro de cada objeto que creamos.
        setCategoryFilter(MegamanMainClass.COIN_BIT);
    }

}
