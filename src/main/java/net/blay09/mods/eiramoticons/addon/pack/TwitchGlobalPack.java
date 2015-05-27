// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.resources.I18n;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class TwitchGlobalPack implements IEmoticonLoader {

	private String template;

	public TwitchGlobalPack() {
		try {
			URL apiURL = new URL("https://twitchemotes.com/api_cache/v2/global.json");
			InputStreamReader reader = new InputStreamReader(apiURL.openStream());
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(reader, JsonObject.class);
			if(root != null) {
				template = "http:" + root.getAsJsonObject("template").get("small").getAsString();
				JsonObject emotes = root.getAsJsonObject("emotes");
				for(Map.Entry<String, JsonElement> entry : emotes.entrySet()) {
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(entry.getKey(), this);
					emoticon.setLoadData(entry.getValue().getAsJsonObject().get("image_id").getAsInt());
					emoticon.setTooltip(I18n.format("eiramoticons:group.twitch"));
				}
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
			if(template != null) {
				BufferedImage image = ImageIO.read(new URL(template.replace("{image_id}", emoticon.getLoadData().toString())));
				if (image != null) {
					emoticon.setImage(image);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
