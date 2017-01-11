package Sprites;

import com.badlogic.gdx.math.Shape2D;
import com.mygdx.megamangame.MegamanMainClass;

import Screen.Level1Screen;
import Tools.InteractiveTileObject;

/**
 * Created by Leandro on 04/01/2017.
 */

public class Lava extends InteractiveTileObject {

    Level1Screen level1Screen;

    public Lava(Level1Screen level1Screen, Shape2D shape2D) {
        super(level1Screen, shape2D);

        this.level1Screen = level1Screen;

        //Le mandamos al user data de cada fixture la clase de objeto que es.
        fixture.setUserData(this);

        //Establecemos el filtro de cada circulo que creamos.
        setCategoryFilter(MegamanMainClass.LAVA_BIT);
    }

    @Override
    public void onBodyHit() {
        level1Screen.dañarPersonajeProgresivamente(1);
    }

    public void onBodyStopHit(){
        level1Screen.dejarDañoPersonajeProgresivo(false);
    }

}
