package net.blay09.mods.eiramoticons.addon.pack;

import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.EmoteLoaderException;
import net.blay09.mods.eiramoticons.api.IEmoticon;

import com.google.gson.*;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.awt.image.BufferedImage;
import java.io.Reader;

public class TwitchSubscriberPack extends AbstractEmotePack {

	public static void createGroup() {
		ITextComponent linkComponent = new TextComponentTranslation("eiramoticons:command.list.clickHere");
        linkComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitchemotes.com/"));
        linkComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("https://twitchemotes.com/")));
        linkComponent.getStyle().setColor(TextFormatting.GOLD);
        linkComponent.getStyle().setBold(true);
        linkComponent.getStyle().setUnderlined(true);
        EiraMoticonsAPI.registerEmoticonGroup("Twitch Subscriber", new TextComponentTranslation("eiramoticons:command.list.twitch.subscriber", linkComponent));
    }
    
    public TwitchSubscriberPack(String channel){
        try {
			Reader reader = TwitchEmotesAPI.newSubscriberEmotesReader(channel, false);
			Gson gson = new Gson();
			JsonObject emoteList;
			try {
				emoteList = gson.fromJson(reader, JsonObject.class);
			} catch (Exception e) {
				reader = TwitchEmotesAPI.newSubscriberEmotesReader(channel, true);
				try {
					emoteList = gson.fromJson(reader, JsonObject.class);
				} catch (Exception e2) {
					throw new EmoteLoaderException(e2);
				}
			}
			if (emoteList != null) {
                String channelName = getJsonString(emoteList, "display_name");
				JsonArray emotes = getJsonArray(emoteList, "emotes");
				for (int i = 0; i < emotes.size(); i++) {
					JsonObject emote = emotes.get(i).getAsJsonObject();
					String code = getJsonString(emote, "code");
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
					emoticon.setLoadData(getJsonInt(emote, "id"));
                    emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.subscriber", channelName));
				}
			}
			reader.close();
		} catch (Exception e) {
			throw new EmoteLoaderException("Unhandled exception", e);
		}
    }

    @Override
    public void loadEmoticonImage(IEmoticon emoticon) {
        BufferedImage image = TwitchEmotesAPI.readTwitchEmoteImage(TwitchEmotesAPI.URL_SMALL, (Integer) emoticon.getLoadData(), "global");
        if (image != null) {
            emoticon.setImage(image);
            if (image.getWidth() <= TwitchEmotesAPI.TWITCH_BASE_SIZE || image.getHeight() <= TwitchEmotesAPI.TWITCH_BASE_SIZE) {
                emoticon.setScale(0.5f, 0.5f);
            }
        }
    }

}
