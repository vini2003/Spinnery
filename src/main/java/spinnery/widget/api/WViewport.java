package spinnery.widget.api;


  a part is visible.
 */
public interface WViewport {



	Size getUnderlyingSize();




	default float getUnderlyingHeight() {
		return getUnderlyingSize().getHeight();
	}




	default float getUnderlyingWidth() {
		return getUnderlyingSize().getWidth();
	}




	Size getVisibleSize();




	default float getVisibleHeight() {
		return getVisibleSize().getHeight();
	}




	default float getVisibleWidth() {
		return getVisibleSize().getWidth();
	}
}
