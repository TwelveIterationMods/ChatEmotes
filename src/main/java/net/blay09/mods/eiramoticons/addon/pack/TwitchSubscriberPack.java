package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.*;
import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Reader;
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
				template = root.getAsJsonObject("template").get("small").getAsString();
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
		ITextComponent linkComponent = new TextComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitchemotes.com/"));
		linkComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("https://twitchemotes.com/")));
		linkComponent.getStyle().setColor(TextFormatting.GOLD);
		linkComponent.getStyle().setBold(true);
		linkComponent.getStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("Twitch Subscriber", new TextComponentTranslation("eiramoticons:command.list.twitch.subscriber", linkComponent));
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
