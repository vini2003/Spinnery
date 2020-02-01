package spinnery.widget;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import com.google.common.collect.ImmutableMap;
import io.github.cottonmc.jankson.JanksonOps;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class WStyle {
	protected final ImmutableMap<String, JsonElement> properties;

	public WStyle(Map<String, JsonElement> properties) {
		this.properties = ImmutableMap.copyOf(properties);
	}

	public WStyle() {
		this.properties = ImmutableMap.of();
	}

	protected JsonElement getElement(String key) {
		String[] keys = key.split("\\.");
		JsonElement pointer = properties.get(keys[0]);
		if (pointer == null) {
			return JanksonOps.INSTANCE.createString("");
		}
		if (keys.length > 1) {
			for (int i = 1; i < keys.length; i++) {
				if (!(pointer instanceof JsonObject)) return pointer;
				pointer = ((JsonObject) pointer).get(keys[i]);
			}
		}
		return pointer;
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
		return JanksonOps.INSTANCE.getNumberValue(getElement(property))
				.map(WColor::of).orElse(WColor.of("0x000000"));
	}

	public WSize asSize(String property) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return WSize.of(0, 0);
		JsonArray array = (JsonArray) el;
		return WSize.of(array.getInt(0, 0), array.getInt(1, 0));
	}

	public WSize asSidedSize(String property) {
		JsonElement el = getElement(property);
		Optional<Number> singleValue = JanksonOps.INSTANCE.getNumberValue(el);
		if (singleValue.isPresent()) {
			int intValue = singleValue.get().intValue();
			return WSize.of(intValue, intValue, intValue, intValue);
		}

		if (!(el instanceof JsonArray)) return WSize.of(0, 0, 0, 0);
		JsonArray array = (JsonArray) el;

		if (array.size() == 1) {
			return WSize.of(array.getInt(0, 0), array.getInt(0, 0), array.getInt(0, 0), array.getInt(0, 0));
		} else if (array.size() == 2) {
			return WSize.of(array.getInt(0, 0), array.getInt(1, 0), array.getInt(0, 0), array.getInt(1, 0));
		} else if (array.size() >= 4) {
			return WSize.of(array.getInt(0, 0), array.getInt(1, 0), array.getInt(2, 0), array.getInt(3, 0));
		} else {
			return WSize.of(0, 0, 0, 0);
		}
	}

	public WPosition asPosition(String property) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return WPosition.of(WType.FREE, 0, 0, 0);
		JsonArray array = (JsonArray) el;
		return WPosition.of(WType.FREE, array.getInt(0, 0), array.getInt(1, 0), array.getInt(2, 0));
	}

	public WPosition asAnchoredPosition(String property, WWidget anchor) {
		JsonElement el = getElement(property);
		if (!(el instanceof JsonArray)) return WPosition.of(WType.ANCHORED, 0, 0, 0, anchor);
		JsonArray array = (JsonArray) el;
		return WPosition.of(WType.ANCHORED, array.getInt(0, 0), array.getInt(1, 0), array.getInt(2, 0), anchor);
	}

	public Identifier asIdentifier(String property) {
		return new Identifier(asString(property));
	}
}
