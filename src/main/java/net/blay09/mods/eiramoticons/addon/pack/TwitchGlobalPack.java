// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import java.io.Reader;
import java.util.Map;

public class TwitchGlobalPack implements IEmoticonLoader {

	private String template;

	public TwitchGlobalPack() {
		try {
			Reader reader = TwitchEmotesAPI.newGlobalEmotesReader(false);
			Gson gson = new Gson();
			JsonObject root = null;
			try {
				root = gson.fromJson(reader, JsonObject.class);
			} catch (JsonParseException e) {
				reader = TwitchEmotesAPI.newGlobalEmotesReader(true);
				try {
					root = gson.fromJson(reader, JsonObject.class);
				} catch (JsonParseException e2) {
					System.out.println("Failed to load global emoticon pack: " + e2.getMessage());
					e2.printStackTrace();
				}
			}
			if(root != null) {
				template = root.getAsJsonObject("template").get("small").getAsString();
				JsonObject emotes = root.getAsJsonObject("emotes");
				for(Map.Entry<String, JsonElement> entry : emotes.entrySet()) {
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(entry.getKey(), this);
					emoticon.setLoadData(entry.getValue().getAsJsonObject().get("image_id").getAsInt());
					emoticon.setTooltip(I18n.format("eiramoticons:group.twitch"));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IChatComponent linkComponent = new ChatComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitchemotes.com/"));
		linkComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("https://twitchemotes.com/")));
		linkComponent.getChatStyle().setColor(EnumChatFormatting.GOLD);
		linkComponent.getChatStyle().setBold(true);
		linkComponent.getChatStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("Twitch Global", new ChatComponentTranslation("eiramoticons:command.list.twitch.global", linkComponent));
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		if(template != null) {
			BufferedImage image = TwitchEmotesAPI.readTwitchEmoteImage(template, (Integer) emoticon.getLoadData(), "global");
			if (image != null) {
				emoticon.setImage(image);
				if(image.getWidth() <= TwitchEmotesAPI.TWITCH_BASE_SIZE || image.getHeight() <= TwitchEmotesAPI.TWITCH_BASE_SIZE) {
					emoticon.setScale(0.5f, 0.5f);
				}
			}
		}
	}

}
