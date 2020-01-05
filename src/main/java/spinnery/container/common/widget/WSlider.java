package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class WSlider extends WWidget {
	protected double limit = 0;
	protected double position = 0;

	protected String slidTotal;
	protected int slidStringPosition;

	public WSlider(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int limit, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLimit(limit);
	}

	public double getLimit() {
		return limit;
	}

	public void setLimit(double limit) {
		this.limit = limit;
	}

	public void setSlidTotal(String slidTotal) {
		this.slidTotal = slidTotal;
	}

	public String getSlidTotal() {
		return slidTotal;
	}

	public void setSlidStringPosition(int slidStringPosition) {
		this.slidStringPosition = slidStringPosition;
	}

	public int getSlidStringPosition() {
		return slidStringPosition;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
		setSlidTotal(Integer.toString((int) Math.round(getPosition())));
		setSlidStringPosition((int) (getPositionX() + getSizeX() / 2 - BaseRenderer.getTextRenderer().getStringWidth(Integer.toString((int) getPosition())) / 2));
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
		BaseRenderer.getTextRenderer().draw(getSlidTotal(), getSlidStringPosition(), (int) (getPositionY() + getSizeY()) + 4, 16);

		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), getSizeX(), 1, 0xFF373737);
		BaseRenderer.drawRectangle(getPositionX(), getPositionY(), getPositionZ(), 1, getSizeY(), 0xFF373737);
		BaseRenderer.drawRectangle(getPositionX() + 1, getPositionY() + 1, getPositionZ(), (getSizeX() / getLimit()) * getPosition(), getSizeY() - 1, 0xFF00C116);
		BaseRenderer.drawRectangle(getPositionX() + (getSizeX() / getLimit()) * getPosition(), getPositionY() + 1, getPositionZ(), getSizeX() - (getSizeX() / getLimit()) * getPosition(), getSizeY() - 1, 0xFF8b8b8b);
		BaseRenderer.drawRectangle(getPositionX(), getPositionY() + getSizeY(), getPositionZ(), getSizeX(), 1, 0xFFFFFFFF);
		BaseRenderer.drawRectangle(getPositionX() + getSizeX(), getPositionY(), getPositionZ(), 1, getSizeY() + 1, 0xFFFFFFFF);

		BaseRenderer.drawBeveledPanel(getPositionX() + (getSizeX() / getLimit()) * getPosition(), getPositionY() - 1, getPositionZ(), 8, getSizeY() + 3, 0xFFFFFFFF, 0xFF8b8b8b, 0xFF373737);
	}
}
