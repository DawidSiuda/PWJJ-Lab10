package lab10;

import javafx.scene.shape.Rectangle;

public class RectangleWrapper extends Rectangle {

	private Sign currentSign = Sign.NONE;

	public Sign getCurrentSign() {
		return currentSign;
	}

	public void setCurrentSign(Sign currentSign) {
		this.currentSign = currentSign;
	}

	public RectangleWrapper(int posX, int posY, int width, int height) {
		super(posX, posY, width, height);
	}
}
