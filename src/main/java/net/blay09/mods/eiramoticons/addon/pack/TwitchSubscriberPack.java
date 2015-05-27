// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.resources.I18n;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitchSubscriberPack implements IEmoticonLoader {

	private String template;

	public TwitchSubscriberPack(String regexFilter) {
		try {
			Pattern pattern = Pattern.compile(regexFilter);
			Matcher matcher = pattern.matcher("");
			Reader reader = TwitchEmotesAPI.newSubscriberEmotesReader();
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(reader, JsonObject.class);
			if(root != null) {
				template = "http:" + root.getAsJsonObject("template").get("small").getAsString();
				JsonObject channels = root.getAsJsonObject("channels");
				for (Map.Entry<String, JsonElement> channelEntry : channels.entrySet()) {
					if (channelEntry.getKey().equals("turbo")) {
						continue;
					}
					JsonObject channel = channelEntry.getValue().getAsJsonObject();
					String title = channel.get("title").getAsString();
					JsonArray emotes = channel.getAsJsonArray("emotes");
					for (int i = 0; i < emotes.size(); i++) {
						JsonObject emote = emotes.get(i).getAsJsonObject();
						String code = emote.get("code").getAsString();
						matcher.reset(code);
						if (matcher.matches()) {
							IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
							emoticon.setLoadData(emote.get("image_id").getAsInt());
							emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.subscriber", title.toLowerCase()));
						}
					}
				}
			}
			reader.close();
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
