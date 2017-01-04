package Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Sprites.InteractiveTileCircleObject;
import Sprites.InteractiveTileObject;

/**
 * Created by Leandro on 03/01/2017.
 */

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() == ("mainNinja") || fixtureB.getUserData() == ("mainNinja")){
            Fixture fixtureBody = fixtureA.getUserData() == "mainNinja" ? fixtureA : fixtureB;
            Fixture fixtureObject = fixtureBody == fixtureA ? fixtureB : fixtureA;

            if (fixtureObject.getUserData() instanceof InteractiveTileObject || fixtureObject.getUserData() instanceof InteractiveTileCircleObject){
                if(fixtureObject.getUserData() instanceof  InteractiveTileObject) {
                    ((InteractiveTileObject) fixtureObject.getUserData()).onBodyHit();
                }
                else {
                    ((InteractiveTileCircleObject)fixtureObject.getUserData()).onBodyHit();
                }
            }


        }


    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
