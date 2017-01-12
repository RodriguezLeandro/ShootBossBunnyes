package Sprites;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;
import Tools.InteractiveTileObject;

/**
 * Created by Leandro on 02/01/2017.
 */

public class Floor extends InteractiveTileObject {

    public Floor(MainGameScreen mainGameScreen, Rectangle rectangle){

        //Como floor es un objeto estatico y rectangulo, utilizamos super();
        super(mainGameScreen,rectangle);

        //Le mandamos al user data de cada fixture la clase de objeto que es.
        fixture.setUserData(this);

        //Establecemos el filtro de cada circulo que creamos.
        setCategoryFilter(MegamanMainClass.FLOOR_BIT);
    }

    @Override
    public void onBodyHit() {

    }
}
