package spinnery.widget.api;


  interface; other use cases should probably implement a less generic interface, such as {@link WLayoutElement}.
 */
public interface WSized {



	default float getWidth() {
		return 0;
	}




	default float getHeight() {
		return 0;
	}
}
