package spinnery.widget.api;


  is necessary.
 *
 /
public interface WInputFilter<T> {


	  @return the requested string.
	 */
	String toString(T t);



	  @return the requested output value.
	 */
	T toValue(String text);


	  given input or not.
	 *
	  @param text      the full text for context.

	boolean accepts(String character, String text);
}
