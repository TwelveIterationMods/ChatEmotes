// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

public class TwitchTurboPack implements IEmoticonLoader {

	private String template;

	public TwitchTurboPack() {
		try {
			Reader reader = TwitchEmotesAPI.newSubscriberEmotesReader();
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(reader, JsonObject.class);
			if(root != null) {
				template = "http:" + root.getAsJsonObject("template").get("small").getAsString();
				JsonObject channels = root.getAsJsonObject("channels");
				JsonObject turbo = channels.getAsJsonObject("turbo");
				JsonArray emotes = turbo.getAsJsonArray("emotes");
				for (int i = 0; i < emotes.size(); i++) {
					JsonObject emote = emotes.get(i).getAsJsonObject();
					String code = emote.get("code").getAsString();
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
					emoticon.setLoadData(emote.get("image_id").getAsInt());
					emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.turbo"));
				}
			}
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IChatComponent linkComponent = new ChatComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitchemotes.com/"));
		linkComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("https://twitchemotes.com/")));
		linkComponent.getChatStyle().setColor(EnumChatFormatting.GOLD);
		linkComponent.getChatStyle().setBold(true);
		linkComponent.getChatStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("Twitch Turbo", new ChatComponentTranslation("eiramoticons:command.list.twitch.turbo", linkComponent));
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
