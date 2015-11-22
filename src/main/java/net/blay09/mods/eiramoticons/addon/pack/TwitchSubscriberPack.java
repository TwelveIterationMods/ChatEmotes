// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.*;
import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.JsonException;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

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
			Reader reader = TwitchEmotesAPI.newSubscriberEmotesReader(false);
			Gson gson = new Gson();
			JsonObject root = null;
			try {
				root = gson.fromJson(reader, JsonObject.class);
			} catch (JsonParseException e) {
				reader = TwitchEmotesAPI.newSubscriberEmotesReader(true);
				try {
					root = gson.fromJson(reader, JsonObject.class);
				} catch (JsonParseException e2) {
					System.out.println("Failed to load subscriber emoticon pack: " + e2.getMessage());
					e2.printStackTrace();
				}
			}
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		IChatComponent linkComponent = new ChatComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitchemotes.com/"));
		linkComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("https://twitchemotes.com/")));
		linkComponent.getChatStyle().setColor(EnumChatFormatting.GOLD);
		linkComponent.getChatStyle().setBold(true);
		linkComponent.getChatStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("Twitch Subscriber", new ChatComponentTranslation("eiramoticons:command.list.twitch.subscriber", linkComponent));
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		if(template != null) {
			BufferedImage image = TwitchEmotesAPI.readTwitchEmoteImage(template, (Integer) emoticon.getLoadData(), "subscriber");
			if (image != null) {
				emoticon.setImage(image);
				if(image.getWidth() <= TwitchEmotesAPI.TWITCH_BASE_SIZE || image.getHeight() <= TwitchEmotesAPI.TWITCH_BASE_SIZE) {
					emoticon.setScale(0.5f, 0.5f);
				}
			}
		}
	}

}
