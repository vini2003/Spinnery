package spinnery.widget.api;

public interface WEventListener {
	void onKeyPressed(int keyPressed, int character, int keyModifier);

	void onKeyReleased(int keyReleased, int character, int keyModifier);

	void onCharTyped(char character, int keyCode);

	void onFocusGained();

	void onFocusReleased();

	void onMouseReleased(int mouseX, int mouseY, int mouseButton);

	void onMouseClicked(int mouseX, int mouseY, int mouseButton);

	void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY);

	void onMouseMoved(int mouseX, int mouseY);

	void onMouseScrolled(int mouseX, int mouseY, double deltaY);

	void onDrawTooltip(int mouseX, int mouseY);

	void onAlign();
}
