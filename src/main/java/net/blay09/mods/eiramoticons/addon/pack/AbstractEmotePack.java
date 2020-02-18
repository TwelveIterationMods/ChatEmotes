package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.eiramoticons.api.EmoteLoaderException;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;

public abstract class AbstractEmotePack implements IEmoticonLoader {

	public static String getJsonString(JsonObject object, String name) {
		JsonElement element = object.get(name);
		if(element == null) {
			throw new EmoteLoaderException("'" + name + "' is null");
		}
		try {
			return element.getAsString();
		} catch (ClassCastException e) {
			throw new EmoteLoaderException("name: " + name, e);
		}
	}

	public static int getJsonInt(JsonObject object, String name) {
		JsonElement element = object.get(name);
		if(element == null) {
			throw new EmoteLoaderException("'" + name + "' is null");
		}
		try {
			return element.getAsInt();
		} catch (ClassCastException e) {
			throw new EmoteLoaderException("name: " + name, e);
		}
	}


	public static JsonObject getJsonObject(JsonObject object, String name) {
		try {
			JsonObject result = object.getAsJsonObject(name);
			if(result == null) {
				throw new EmoteLoaderException("'" + name + "' is null");
			}
			return result;
		} catch (ClassCastException e) {
			throw new EmoteLoaderException("name: " + name, e);
		}
	}

	public static JsonArray getJsonArray(JsonObject object, String name) {
		try {
			JsonArray result = object.getAsJsonArray(name);
			if(result == null) {
				throw new EmoteLoaderException("'" + name + "' is null");
			}
			return result;
		} catch (ClassCastException e) {
			throw new EmoteLoaderException("name: " + name, e);
		}
	}

}
