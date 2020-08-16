package spinnery.widget.api;



  typed objects, e.g. {@link WLayoutElement}, {@link Position} and others. It is generally inadvisable to
  provides more convenience methods and standard logic.
 */
public interface WPositioned {



	default float getX() {
		return 0;
	}




	default float getY() {
		return 0;
	}




	default float getZ() {
		return 0;
	}
}
