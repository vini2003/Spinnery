package spinnery.widget.api;


 /
public interface WEventListener {


	  @param character   Character associated with pressed key.

	void onKeyPressed(int keyCode, int character, int keyModifier);



	  @param character   Character associated with pressed key.

	void onKeyReleased(int keyCode, int character, int keyModifier);



	  @param keyCode   Keycode associated with key pressed.
	 */
	void onCharTyped(char character, int keyCode);



	void onFocusGained();



	void onFocusReleased();



	  @param mouseY      Vertical position of mouse cursor.

	void onMouseReleased(float mouseX, float mouseY, int mouseButton);



	  @param mouseY      Vertical position of mouse cursor.

	void onMouseClicked(float mouseX, float mouseY, int mouseButton);



	  @param mouseY      Vertical position of mouse cursor.
	  @param deltaX      Horizontal delta of mouse drag.

	void onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY);



	  @param mouseY Vertical position of mouse cursor.
	 */
	void onMouseMoved(float mouseX, float mouseY);



	  @param mouseY Vertical position of the mouse cursor.

	void onMouseScrolled(float mouseX, float mouseY, double deltaY);



	  @param mouseY Vertical position of mouse cursor.
	 */
	void onDrawTooltip(float mouseX, float mouseY);



	void onAlign();
}
