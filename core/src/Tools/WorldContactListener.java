package Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.megamangame.MegamanMainClass;

import Sprites.Lava;

/**
 * Created by Leandro on 03/01/2017.
 */

public class WorldContactListener implements ContactListener {


    Fixture fixtureBody;
    Fixture fixtureObject;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int categoryDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (categoryDefinition){

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.COIN_BIT :

                fixtureBody = fixtureA.getUserData() == "mainNinja" ? fixtureA : fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB : fixtureA;

                if (fixtureObject.getUserData() instanceof InteractiveTileObject){
                    ((InteractiveTileObject) fixtureObject.getUserData()).onBodyHit();
                }

                break;

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.LAVA_BIT :

                fixtureBody = fixtureA.getUserData() == "mainNinja" ? fixtureA: fixtureB;
                fixtureObject = fixtureBody == fixtureA ? fixtureB: fixtureA;

                if (fixtureObject.getUserData() instanceof  InteractiveTileObject){
                    ((Lava) fixtureObject.getUserData()).onBodyHit();
                }

                break;
            default:

                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int categoryDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch(categoryDefinition){

            case MegamanMainClass.MEGAMAN_SENSOR_BIT | MegamanMainClass.LAVA_BIT :
                if (fixtureObject.getUserData() instanceof  InteractiveTileObject){
                    ((Lava) fixtureObject.getUserData()).onBodyStopHit();
                }
                break;

            default:

                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
