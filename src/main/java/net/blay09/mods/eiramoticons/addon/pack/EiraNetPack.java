// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.*;
import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class EiraNetPack implements IEmoticonLoader {

	private String urlTemplate;

	public EiraNetPack() {
		try {
			URL apiURL = new URL("http://blay09.net/eiranet/api/emotes.php");
			InputStreamReader reader = new InputStreamReader(apiURL.openStream());
			Gson gson = new Gson();
			JsonObject root = null;
			try {
				root = gson.fromJson(reader, JsonObject.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			}
			if(root != null) {
				urlTemplate = "http:" + root.get("url_template").getAsString();
				JsonArray emotes = root.getAsJsonArray("emotes");
				for(int i = 0; i < emotes.size(); i++) {
					JsonObject emote = emotes.get(i).getAsJsonObject();
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(emote.get("code").getAsString(), this);
					emoticon.setLoadData(emote.get("id").getAsInt());
					emoticon.setTooltip(I18n.format("eiramoticons:group.eiranet", emote.get("owner").getAsString()));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IChatComponent linkComponent = new ChatComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://blay09.net/eiranet/"));
		linkComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("https://blay09.net/eiranet/")));
		linkComponent.getChatStyle().setColor(EnumChatFormatting.GOLD);
		linkComponent.getChatStyle().setBold(true);
		linkComponent.getChatStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("EiraNet User Emotes", new ChatComponentTranslation("eiramoticons:command.list.eiranet", linkComponent));
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		if(urlTemplate != null) {
			try {
				EiraMoticonsAPI.loadImage(emoticon, new URI(urlTemplate.replace("{{id}}", String.valueOf(emoticon.getLoadData()))));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

}
