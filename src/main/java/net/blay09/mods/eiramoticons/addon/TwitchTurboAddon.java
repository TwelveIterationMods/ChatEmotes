// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

public class TwitchTurboAddon implements IEmoticonLoader {

	private String template;

	public TwitchTurboAddon() {
		try {
			URL apiURL = new URL("http://twitchemotes.com/api_cache/v2/subscriber.json");
			InputStreamReader reader = new InputStreamReader(apiURL.openStream());
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(reader, JsonObject.class);
			template = "http:" + root.getAsJsonObject("template").get("small").getAsString();
			JsonObject channels = root.getAsJsonObject("channels");
			JsonObject turbo = channels.getAsJsonObject("turbo");
			JsonArray emotes = turbo.getAsJsonArray("emotes");
			for(int i = 0; i < emotes.size(); i++) {
				JsonObject emote = emotes.get(i).getAsJsonObject();
				String code = emote.get("code").getAsString();
				IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
				emoticon.setLoadData(emote.get("image_id").getAsInt());
				emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.turbo"));
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
			BufferedImage image = ImageIO.read(new URL(template.replace("{image_id}", emoticon.getLoadData().toString())));
			if(image != null) {
				emoticon.setImage(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}