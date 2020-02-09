package spinnery.widget.api;

/**
 * Generic interface representing an object that may have a width and height. Utility classes are well-served by this
 * interface; other use cases should probably implement a less generic interface, such as {@link WLayoutElement}.
 */
public interface WSized {
	int getWidth();
	int getHeight();
}
