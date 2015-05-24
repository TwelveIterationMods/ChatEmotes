package net.blay09.mods.eiramoticons.addon;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

public class BTTVAddon implements IEmoticonLoader {

	public BTTVAddon() {
		try {
			URL apiURL = new URL("https://cdn.betterttv.net/emotes/emotes.json");
			InputStreamReader reader = new InputStreamReader(apiURL.openStream());
			Gson gson = new Gson();
			JsonArray root = gson.fromJson(reader, JsonArray.class);
			for(int i = 0; i < root.size(); i++) {
				JsonObject entry = root.get(i).getAsJsonObject();
				String code = entry.get("regex").getAsString();
				// We don't do regex emotes, so we just strip the regex stuff and add a special case for the one emote that actually needs it
				code = code.replace("\\", "");
				if(code.equals("(:trollface:|:tf:)")) {
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(":trollface:", this);
					emoticon.setIdentifier(entry.get("url").getAsString());
					emoticon.setTooltip(new String[] { "\u00a7eEmote: \u00a7r:trollface:", "\u00a7eBetter TwitchTV\u00a7r" });

					emoticon = EiraMoticonsAPI.registerEmoticon(":tf:", this);
					emoticon.setIdentifier(entry.get("url").getAsString());
					emoticon.setTooltip(new String[] { "\u00a7eEmote: \u00a7r:tf:", "\u00a7eBetter TwitchTV\u00a7r" });
				} else {
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
					emoticon.setIdentifier(entry.get("url").getAsString());
					emoticon.setTooltip(new String[] { "\u00a7eEmote: \u00a7r" + code, "\u00a7eBetter TwitchTV\u00a7r" });
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
			BufferedImage image = ImageIO.read(new URL("http:" + emoticon.getIdentifier().toString()));
			if(image != null) {
				emoticon.setImage(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
