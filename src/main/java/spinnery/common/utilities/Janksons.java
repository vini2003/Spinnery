package spinnery.common.utilities;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonPrimitive;

import java.util.Optional;

public class Janksons {
	public static Optional<Number> asNumber(JsonElement element) {
		if (!(element instanceof JsonPrimitive)) return Optional.empty();
		if (!(((JsonPrimitive) element).getValue() instanceof Number)) return Optional.empty();
		return Optional.of((Number) ((JsonPrimitive) element).getValue());
	}

	public static Optional<Boolean> asBoolean(JsonElement element) {
		if (!(element instanceof JsonPrimitive)) return Optional.empty();
		if (!(((JsonPrimitive) element).getValue() instanceof Boolean)) return Optional.empty();
		return Optional.of((Boolean) ((JsonPrimitive) element).getValue());
	}

	public static Optional<String> asString(JsonElement element) {
		if (!(element instanceof JsonPrimitive)) return Optional.empty();
		if (!(((JsonPrimitive) element).getValue() instanceof String)) return Optional.empty();
		return Optional.of((String) ((JsonPrimitive) element).getValue());
	}

	public static JsonArray arrayOfPrimitives(Object... values) {
		JsonArray elementArray = new JsonArray();
		for (Object value : values) {
			elementArray.add(new JsonPrimitive(value));
		}
		return elementArray;
	}
}
