package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class TwitchImagesPack implements IEmoticonLoader {

	public TwitchImagesPack() {
		try {
			Reader reader = TwitchEmotesAPI.newImagesEmotesReader(false);
			Gson gson = new Gson();
			JsonObject emoteList = null;
			try {
				emoteList = gson.fromJson(reader, JsonObject.class);
			} catch (JsonParseException e) {
				reader = TwitchEmotesAPI.newImagesEmotesReader(true);
				try {
					emoteList = gson.fromJson(reader, JsonObject.class);
				} catch (JsonParseException e2) {
					System.out.println("Failed to load alternate subscriber emoticon pack: " + e2.getMessage());
					e2.printStackTrace();
				}
			}
			if (emoteList != null) {
                for (Map.Entry<String, JsonElement> entry : emoteList.entrySet()) {
                    IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(entry.getValue().getAsJsonObject().get("code").getAsString(), this);
                    JsonObject emoticonEntry = entry.getValue().getAsJsonObject();
                    emoticon.setLoadData(entry.getValue().getAsJsonObject().get("id").getAsInt());
                    String channelName = emoticonEntry.get("channel_name").getAsString();
                    emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.subscriber", channelName));
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
		EiraMoticonsAPI.registerEmoticonGroup("Twitch Subscriber", new ChatComponentTranslation("eiramoticons:command.list.twitch.subscriber", linkComponent));
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		BufferedImage image = TwitchEmotesAPI.readTwitchEmoteImage(TwitchEmotesAPI.URL_SMALL, (Integer) emoticon.getLoadData(), "global");
		if (image != null) {
			emoticon.setImage(image);
			if(image.getWidth() <= TwitchEmotesAPI.TWITCH_BASE_SIZE || image.getHeight() <= TwitchEmotesAPI.TWITCH_BASE_SIZE) {
				emoticon.setScale(0.5f, 0.5f);
			}
		}
	}

}
