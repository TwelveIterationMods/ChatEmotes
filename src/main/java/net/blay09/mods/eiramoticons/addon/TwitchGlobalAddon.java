// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class TwitchGlobalAddon implements IEmoticonLoader {

	private String template;

	public TwitchGlobalAddon() {
		try {
			URL apiURL = new URL("http://twitchemotes.com/api_cache/v2/global.json");
			InputStreamReader reader = new InputStreamReader(apiURL.openStream());
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(reader, JsonObject.class);
			template = "http:" + root.getAsJsonObject("template").get("small").getAsString();
			JsonObject emotes = root.getAsJsonObject("emotes");
			for(Map.Entry<String, JsonElement> entry : emotes.entrySet()) {
				IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(entry.getKey(), this);
				emoticon.setIdentifier(entry.getValue().getAsJsonObject().get("image_id").getAsInt());
				emoticon.setTooltip(new String[] { "\u00a7eEmote: \u00a7r" + entry.getKey() });
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		try {
			BufferedImage image = ImageIO.read(new URL(template.replace("{image_id}", emoticon.getIdentifier().toString())));
			if(image != null) {
				emoticon.setImage(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
