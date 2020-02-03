package spinnery.widget.api;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import io.github.cottonmc.jankson.JanksonOps;
import net.minecraft.util.Identifier;
import spinnery.Spinnery;
import spinnery.widget.WWidget;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class WStyle {
	protected final Map<String, JsonElement> properties = new HashMap<>();

	public WStyle(Map<String, JsonElement> properties) {
		this.properties.putAll(properties);
	}

	public WStyle(WStyle other) {
		this.properties.putAll(other.properties);
	}

	public WStyle() {
	}

	protected JsonElement getElement(String key) {
		return properties.get(key);
	}

	public boolean contains(String key) {
		return properties.get(key) != null;
	}

	// GETTERS

	public boolean asBoolean(String property) {
		return JanksonOps.INSTANCE.getNumberValue(getElement(property)).orElse(0).intValue() == 1;
	}

	public String asString(String property) {
		return JanksonOps.INSTANCE.getStringValue(getElement(property)).orElse("");
	}

	protected Number asNumber(String property) {
		return JanksonOps.INSTANCE.getNumberValue(getElement(property)).orElse(0);
	}

	public int asInt(String property) {
		return asNumber(property).intValue();
	}

	public long asLong(String property) {
		return asNumber(property).longValue();
	}

	public float asFloat(String property) {
		return asNumber(property).floatValue();
	}

	public double asDouble(String property) {
		return asNumber(property).doubleValue();
	}

	public WColor asColor(String property) {
		return asColor(property, WColor.of("0xff000000"));
	}

	public WColor asColor(String property, WColor defaultColor) {
		return JanksonOps.INSTANCE.getNumberValue(getElement(property))
				.map(WColor::of).orElse(defaultColor);
	}

	public WSize asSize(String property) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return new WSize().put(0, 0);
		JsonArray array = (JsonArray) el;
		return new WSize().put(array.getInt(0, 0), array.getInt(1, 0));
	}

	public WSize asSidedSize(String property) {
		JsonElement el = getElement(property);
		Optional<Number> singleValue = JanksonOps.INSTANCE.getNumberValue(el);
		if (singleValue.isPresent()) {
			int intValue = singleValue.get().intValue();
			return new WSize().put(intValue, intValue).put(intValue, intValue);
		}

		if (!(el instanceof JsonArray)) return new WSize().put(0, 0).put(0, 0);
		JsonArray array = (JsonArray) el;

		if (array.size() == 1) {
			return new WSize().put(array.getInt(0, 0), array.getInt(0, 0)).put(array.getInt(0, 0), array.getInt(0, 0));
		} else if (array.size() == 2) {
			return new WSize().put(array.getInt(0, 0), array.getInt(1, 0)).put(array.getInt(0, 0), array.getInt(1, 0));
		} else if (array.size() >= 4) {
			return new WSize().put(array.getInt(0, 0), array.getInt(1, 0)).put(array.getInt(2, 0), array.getInt(3, 0));
		} else {
			return new WSize().put(0, 0).put(0, 0);
		}
	}

	public WPosition asPosition(String property) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return new WPosition().position(0, 0, 0);
		JsonArray array = (JsonArray) el;
		return new WPosition().position(array.getInt(0, 0), array.getInt(1, 0), array.getInt(2, 0));
	}

	public WPosition asAnchoredPosition(String property, WWidget anchor) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return new WPosition().anchor(anchor).position(0, 0, 0);
		JsonArray array = (JsonArray) el;
		return new WPosition().anchor(anchor).position(array.getInt(0, 0), array.getInt(1, 0), array.getInt(2, 0));
	}

	public Identifier asIdentifier(String property) {
		return new Identifier(asString(property));
	}

	// SETTERS

	protected static Map<Class<?>, Function<?, JsonElement>> jsonSerializers = new HashMap<>();
	protected static <T> void registerSerializer(Class<T> vClass, Function<T, JsonElement> serializer) {
		jsonSerializers.put(vClass, serializer);
	}
	@SuppressWarnings("unchecked")
	protected static <T> Function<T, JsonElement> getSerializer(T value) {
		for (Class<?> serClass : jsonSerializers.keySet()) {
			if (serClass.isAssignableFrom(value.getClass())) {
				return (Function<T, JsonElement>) jsonSerializers.get(serClass);
			}
		}
		return null;
	}
	static {
		registerSerializer(Number.class, JanksonOps.INSTANCE::createNumeric);
		registerSerializer(String.class, JanksonOps.INSTANCE::createString);
		registerSerializer(Boolean.class, JanksonOps.INSTANCE::createBoolean);
		registerSerializer(WPosition.class, v -> JanksonOps.INSTANCE.createIntList(IntStream.of(v.rawX, v.rawY, v.rawZ)));
		registerSerializer(WSize.class, v -> {
			int[] values = new int[4];
			for (int i : v.sizes.keySet()) {
				if (i == 0) {
					values[0] = v.getX(0);
					values[1] = v.getY(0);
				}
				if (i == 1) {
					values[2] = v.getX(1);
					values[3] = v.getY(1);
				}
			}
			return JanksonOps.INSTANCE.createIntList(IntStream.of(values).filter(Objects::nonNull));
		});
		registerSerializer(WColor.class, v -> JanksonOps.INSTANCE.createLong(v.ARGB));
	}

	public <T> void override(String property, T value) {
		Function<T, JsonElement> ser = getSerializer(value);
		if (ser != null) {
			properties.put(property, ser.apply(value));
		} else {
			Spinnery.LOGGER.warn("Failed to override {}: themes do not support values of class {}",
					property, value.getClass().getSimpleName());
		}
	}
}
