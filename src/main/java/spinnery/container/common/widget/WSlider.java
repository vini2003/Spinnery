package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class WSlider extends WWidget {
	public static final Identifier DEFAULT_PICKER = new Identifier("spinnery:textures/widget/slider_picker.png");
	public static final Identifier DEFAULT_BAR = new Identifier("spinnery:textures/widget/slider_bar.png");

	protected double limit = 0;
	protected double position = 0;

	protected Identifier barTexture;
	protected Identifier pickerTexture;

	public WSlider(WPanel linkedWPanel, int positionX, int positionY, int positionZ,
					 double sizeX, double sizeY, int limit) {
		this(linkedWPanel, positionX, positionY, positionZ, sizeX, sizeY, limit, DEFAULT_PICKER, DEFAULT_BAR);
	}

	public WSlider(WPanel linkedWPanel, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int limit, Identifier barTexture, Identifier pickerTexture) {
		setPosition(positionX, positionY, positionZ);
		setSize(sizeX, sizeY);

		setLimit(limit);

		setLinkedPanel(linkedWPanel);

		setBarTexture(barTexture);
		setPickerTexture(pickerTexture);
	}

	public double getLimit() {
		return limit;
	}

	public void setLimit(double limit) {
		this.limit = limit;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public Identifier getBarTexture() { return this.barTexture; }

	public void setBarTexture(Identifier tex) { this.barTexture = tex; }

	public Identifier getPickerTexture() { return this.pickerTexture; }

	public void setPickerTexture(Identifier tex) { this.pickerTexture = tex; }

	public void updatePosition(double mouseX, double mouseY) {
		if (isFocused(mouseX, mouseY)) {
			setPosition((mouseX - getPositionX()) * (getLimit() / (getSizeX())));
		}
	}

	@Override
	public void onKeyPressed(int keyPressed) {
		if (getFocus() && keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT) {
			setPosition(Math.min(getPosition() + 1, getLimit() - 1));
		}
		if (getFocus() && keyPressed == GLFW.GLFW_KEY_KP_DIVIDE) {
			setPosition(getPosition() - 1 >= 0 ? getPosition() - 1 : 0);
		}
		super.onKeyPressed(keyPressed);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		updatePosition(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		updatePosition(mouseX, mouseY);
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public boolean isWithinBounds(double positionX, double positionY) {
		return (positionX >= getPositionX()
			 && positionX <= getPositionX() + getSizeX() - 7
			 && positionY >= getPositionY()
			 && positionY <= getPositionY() + getSizeY());
	}

	@Override
	public boolean isFocused(double mouseX, double mouseY) {
		Optional<? extends WWidget> isBelowOtherWidget = linkedWPanel.getLinkedWidgets().stream().filter((widget) ->
			   widget.getPositionZ() > getPositionZ() && widget.isWithinBounds(mouseX, mouseY)
		).findAny();
		return setFocus(!isBelowOtherWidget.isPresent() && isWithinBounds(mouseX, mouseY));
	}

	@Override
	public void drawWidget() {
		MinecraftClient.getInstance().textRenderer.draw(Integer.toString((int) Math.round(getPosition())), (int) (getPositionX() + getSizeX() / 2 - BaseRenderer.textRenderer.getStringWidth(Integer.toString((int) getPosition())) / 2), (int) (getPositionY() + getSizeY()) + 4, 16);

		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), 100, 10, getBarTexture());
		BaseRenderer.drawImage(getPositionX() + (getSizeX() / getLimit()) * getPosition(), getPositionY() - 1, getPositionZ(), 7, 12, getPickerTexture());
	}
}
