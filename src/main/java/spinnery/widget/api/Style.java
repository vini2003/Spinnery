package spinnery.widget.api;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonPrimitive;
import net.minecraft.util.Identifier;
import spinnery.Spinnery;
import spinnery.common.utilities.Janksons;
import spinnery.widget.WAbstractWidget;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


  It is usually attached to a theme.
 */
@SuppressWarnings("unused")
public class Style {
	protected static Map<Class<?>, Function<?, JsonElement>> jsonSerializers = new HashMap<>();

	static {
		registerSerializer(Number.class, value -> new JsonPrimitive(value.longValue()));
		registerSerializer(String.class, JsonPrimitive::new);
		registerSerializer(Boolean.class, JsonPrimitive::new);
		registerSerializer(JanksonSerializable.class, JanksonSerializable::toJson);
	}

	protected final Map<String, JsonElement> properties = new HashMap<>();

	public Style(Map<String, JsonElement> properties) {
		this.properties.putAll(properties);
	}

	public Style() {
	}

	public static Style of(Style other) {
		return new Style(other.properties);
	}

	protected static <T> void registerSerializer(Class<T> vClass, Function<T, JsonElement> serializer) {
		jsonSerializers.put(vClass, serializer);
	}



	  @return True if the key exists; false if not.
	 */
	public boolean contains(String property) {
		return properties.get(property) != null;
	}



	  @return JsonElement of the given property.
	 */
	protected JsonElement getElement(String key) {
		return properties.get(key);
	}



	  @return Property as a Boolean; defaulting to false if conversion fails.
	 */
	public boolean asBoolean(String property) {
		return Janksons.asBoolean(getElement(property)).orElse(false);
	}



	  @return Property as a Number; defaulting to zero if conversion fails.
	 */
	protected Number asNumber(String property) {
		return Janksons.asNumber(getElement(property)).orElse(0);
	}



	  @return Property as an int; defaulting to zero if conversion fails.
	 */
	public int asInt(String property) {
		return asNumber(property).intValue();
	}



	  @return Property as a long; defaulting to zero if conversion fails.
	 */
	public long asLong(String property) {
		return asNumber(property).longValue();
	}



	  @return Property as a float; defaulting to zero if conversion fails.
	 */
	public float asFloat(String property) {
		return asNumber(property).floatValue();
	}



	  @return Property as a double; defaulting to zero if conversion fails.
	 */
	public double asDouble(String property) {
		return asNumber(property).doubleValue();
	}



	  @return Property as a Color; defaulting to black if conversion fails.
	 */
	public Color asColor(String property) {
		return asColor(property, Color.of("0xff000000"));
	}



	  @param defaultColor Default color in case conversion fails.

	public Color asColor(String property, Color defaultColor) {
		return Janksons.asNumber(getElement(property)).map(Color::of).orElse(defaultColor);
	}



	  @return Property as a Size; defaulting to 0, 0 if conversion fails.
	 */
	public Size asSize(String property) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return Size.of(0, 0);
		JsonArray array = (JsonArray) el;
		return Size.of(array.getInt(0, 0), array.getInt(1, 0));
	}



	  @return Property as a Padding, defaulting to 0 if conversion fails, starting clockwise from the top.
	 */
	public Padding asPadding(String property) {
		JsonElement el = getElement(property);
		Optional<Number> singleValue = Janksons.asNumber(el);
		if (singleValue.isPresent()) {
			int intValue = singleValue.get().intValue();
			Size size = Size.of(intValue, intValue);
			return Padding.of(intValue);
		}

		if (!(el instanceof JsonArray)) return Padding.of(0);
		JsonArray array = (JsonArray) el;

		if (array.size() == 1) {
			return Padding.of(array.getInt(0, 0));
		} else if (array.size() == 2) {
			return Padding.of(array.getInt(0, 0), array.getInt(1, 0));
		} else if (array.size() >= 4) {
			return Padding.of(array.getInt(0, 0), array.getInt(1, 0), array.getInt(2, 0), array.getInt(3, 0));
		}
		return Padding.of(0);
	}



	  @return Property as a Position, defaulting to 0, 0 if conversion fails.
	 */
	public Position asPosition(String property) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return Position.origin();
		JsonArray array = (JsonArray) el;
		return Position.of(array.getInt(0, 0), array.getInt(1, 0), array.getInt(2, 0));
	}



	  @param anchor   Property as an anchored Position, defaulting to the anchor's position if conversion fails.

	public Position asAnchoredPosition(String property, WAbstractWidget anchor) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return Position.of(anchor);
		JsonArray array = (JsonArray) el;
		return Position.of(anchor, array.getInt(0, 0), array.getInt(1, 0), array.getInt(2, 0));
	}



	  @return Property as an Identifier, defaulting to an empty one if conversion fails.
	 */
	public Identifier asIdentifier(String property) {
		return new Identifier(asString(property));
	}



	  @return Property as a String, defaulting to an empty one if conversion fails.
	 */
	public String asString(String property) {
		return Janksons.asString(getElement(property)).orElse("");
	}



	  @param value    Value for property to be associated with.
	 */
	public <T> Style override(String property, T value) {
		Function<T, JsonElement> ser = getSerializer(value);
		if (ser != null) {
			properties.put(property, ser.apply(value));
		} else {
			Spinnery.LOGGER.warn("Failed to override {}: themes do not support values of class {}",
					property, value.getClass().getSimpleName());
		}
		return this;
	}



	  @return Serializer for the value; defaulting to null if search fails.
	 */
	@SuppressWarnings("unchecked")
	protected static <T> Function<T, JsonElement> getSerializer(T value) {
		for (Class<?> serClass : jsonSerializers.keySet()) {
			if (serClass.isAssignableFrom(value.getClass())) {
				return (Function<T, JsonElement>) jsonSerializers.get(serClass);
			}
		}
		return null;
	}



	  @return Style containing both styles.
	 */
	public Style mergeFrom(Style other) {
		this.properties.putAll(other.properties);
		return this;
	}
}
