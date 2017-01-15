package Sprites;

import com.badlogic.gdx.math.Shape2D;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.MainGameScreen;
import Tools.InteractiveTileObject;

/**
 * Created by Leandro on 04/01/2017.
 */

public class Lava extends InteractiveTileObject {

    MainGameScreen mainGameScreen;

    public Lava(MainGameScreen mainGameScreen, Shape2D shape2D) {
        super(mainGameScreen, shape2D);

        this.mainGameScreen = mainGameScreen;

        //Le mandamos al user data de cada fixture la clase de objeto que es.
        fixture.setUserData(this);

        //Establecemos el filtro de cada circulo que creamos.
        setCategoryFilter(MegamanMainClass.LAVA_BIT);
    }

    @Override
    public void onBodyHit() {
        mainGameScreen.dañarPersonajeProgresivamente(1,true);
    }

    public void onBodyStopHit(){
        //Notese que el 1 que mandamos es inservible, podriamos no mandar nada.
        mainGameScreen.dañarPersonajeProgresivamente(1,false);
    }

}
