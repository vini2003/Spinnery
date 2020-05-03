package com.github.vini2003.spinnery.widget.api;

import blue.endless.jankson.JsonElement;

/**
 * Generic interface for values that may be stored as a Jankson element. Used for serialization of theme styles.
 */
public interface JanksonSerializable {
	JsonElement toJson();
}
