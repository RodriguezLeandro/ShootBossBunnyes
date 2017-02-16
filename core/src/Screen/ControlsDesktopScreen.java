package Screen;

import com.mygdx.megamangame.MegamanMainClass;

/**
 * Created by Leandro on 29/01/2017.
 */

public class ControlsDesktopScreen extends ControlsScreen {

    public ControlsDesktopScreen(MegamanMainClass game) {
        super(game);

        leftArrowLabel.setText("Press S: It does Nothing");
        downArrowLabel.setText("Press A: Move To Left");
        rightArrowLabel.setText("Press D: Move To Right");
        //Aca medio como que robo pero bueno, ya que upArrowLabel queda con la cantidad que necesito de letras.
        upArrowLabel.setText("Press W: Jump");

        rightButtonLabel.setText("Right Arrow: Throw punch fireball");
        downButtonLabel.setText("Down Arrow: Slide");
        leftButtonLabel.setText("Left Arrow: Crouch");
        upButtonLabel.setText("Up Arrou: Gravity power");

    }

    public ControlsDesktopScreen(MegamanMainClass game, LevelSelect levelSelect) {
        super(game,levelSelect);

        leftArrowLabel.setText("Press S: It does Nothing");
        downArrowLabel.setText("Press A: Move To Left");
        rightArrowLabel.setText("Press D: Move To Right");
        upArrowLabel.setText("Press W: Jump");

        rightButtonLabel.setText("Right Arrow: Throw punch fireball");
        downButtonLabel.setText("Down Arrow: Slide");
        leftButtonLabel.setText("Left Arrow: Crouch");
        upButtonLabel.setText("Up Arrou: Gravity power");

    }

}
