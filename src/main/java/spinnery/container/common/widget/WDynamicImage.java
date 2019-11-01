package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.util.Identifier;

/**
 * Represents a dynamic image (an image with multiple possible textures).
 */
public class WDynamicImage extends WWidget {
	protected Identifier[] textures;

	protected int cyclePos = 0;

	public WDynamicImage(WPanel linkedWPanel, int positionX, int positionY, int positionZ,
						 double sizeX, double sizeY, Identifier... textures) {
		setPosition(positionX, positionY, positionZ);
		setSize(sizeX, sizeY);

		setTextures(textures);

		setLinkedPanel(linkedWPanel);
	}

	/**
	 * Moves the cycle position forwards one, and returns the new cycle position.
	 */
	public int nextCyclePos() {
		setCyclePos((getCyclePos() + 1) % getTextures().length);
		return getCyclePos();
	}

	/**
	 * Moves the cycle position backwards one, and returns the previous cycle position.
	 */
	public int previousCyclePos() {
		if (getCyclePos() > 0) {
			setCyclePos(getCyclePos() - 1);
		} else {
			setCyclePos(getTextures().length - 1);
		}
		return getCyclePos();
	}


	public void setCyclePos(int position) { this.cyclePos = position; }

	public int getCyclePos() { return this.cyclePos; }

	public void setTextures(Identifier... textures) {
		this.textures = textures;
	}

	public Identifier[] getTextures() {
		return textures;
	}

	public Identifier getTexture(int cyclePos) {
		return textures[cyclePos];
	}

	/**
	 * Gets the current texture in the cycle.
	 */
	public Identifier getCurrentTexture() {
		return textures[cyclePos];
	}

	@Override
	public void drawWidget() {
		nextCyclePos();
		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), getSizeY(), getCurrentTexture());
	}
}
