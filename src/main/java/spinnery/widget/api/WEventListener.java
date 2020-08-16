package spinnery.widget.api;

public interface WEventListener {
	void onKeyPressed(int keyCode, int character, int keyModifier);

	void onKeyReleased(int keyCode, int character, int keyModifier);

	void onCharTyped(char character, int keyCode);

	void onFocusGained();

	void onFocusReleased();

	void onMouseReleased(float mouseX, float mouseY, int mouseButton);

	void onMouseClicked(float mouseX, float mouseY, int mouseButton);

	void onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY);

	void onMouseMoved(float mouseX, float mouseY);

	void onMouseScrolled(float mouseX, float mouseY, double deltaY);

	void onDrawTooltip(float mouseX, float mouseY);
}
