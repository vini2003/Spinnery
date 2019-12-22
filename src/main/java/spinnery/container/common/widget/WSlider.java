package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class WSlider extends WWidget {
	public static final Identifier DEFAULT_PICKER = new Identifier("spinnery:textures/widget/slider_picker.png");
	public static final Identifier DEFAULT_BAR = new Identifier("spinnery:textures/widget/slider_bar.png");

	protected double limit = 0;
	protected double position = 0;

	protected String slidTotal;
	protected int slidStringPosition;

	public WSlider(int positionX, int positionY, int positionZ, double sizeX, double sizeY, int limit, Identifier texture, WPanel linkedWPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLimit(limit);

		setLinkedPanel(linkedWPanel);
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
		slidTotal = Integer.toString((int) Math.round(getPosition()));
		slidStringPosition = (int) (getPositionX() + getSizeX() / 2 - BaseRenderer.getTextRenderer().getStringWidth(Integer.toString((int) getPosition())) / 2);
	}

	public void updatePosition(double mouseX, double mouseY) {
		if (scanFocus(mouseX, mouseY)) {
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
	public boolean scanFocus(double mouseX, double mouseY) {
		Optional<? extends WWidget> isBelowOtherWidget = linkedWPanel.getLinkedWidgets().stream().filter((widget) ->
			   widget.getPositionZ() > getPositionZ() && widget.isWithinBounds(mouseX, mouseY)
		).findAny();
		return setFocus(!isBelowOtherWidget.isPresent() && isWithinBounds(mouseX, mouseY));
	}

	@Override
	public void drawWidget() {
		BaseRenderer.getTextRenderer().draw(slidTotal, slidStringPosition, (int) (getPositionY() + getSizeY()) + 4, 16);

		BaseRenderer.drawImage(getPositionX(), getPositionY(), getPositionZ(), 100, 10, DEFAULT_BAR);
		BaseRenderer.drawImage(getPositionX() + (getSizeX() / getLimit()) * getPosition(), getPositionY() - 1, getPositionZ(), 7, 12, DEFAULT_PICKER);
	}
}
