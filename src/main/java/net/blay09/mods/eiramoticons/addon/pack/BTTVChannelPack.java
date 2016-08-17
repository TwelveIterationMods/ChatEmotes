package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class BTTVChannelPack implements IEmoticonLoader {

	private String urlTemplate;

	public static void createGroup() {
		ITextComponent linkComponent = new TextComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://manage.betterttv.net/"));
		linkComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("https://manage.betterttv.net/")));
		linkComponent.getStyle().setColor(TextFormatting.GOLD);
		linkComponent.getStyle().setBold(true);
		linkComponent.getStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("Better TwitchTV Channels", new TextComponentTranslation("eiramoticons:command.list.twitch.bttvChannels", linkComponent));
	}

	public BTTVChannelPack(String channelName) {
		try {
			URL apiURL = new URL("https://api.betterttv.net/2/channels/" + channelName);
			InputStreamReader reader = new InputStreamReader(apiURL.openStream());
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(reader, JsonObject.class);
			if(!root.has("status") && root.get("status").getAsInt() != 200) {
				System.out.println("Failed to grab BTTV emotes.");
				return;
			}
			urlTemplate = root.get("urlTemplate").getAsString();
			JsonArray emotes = root.getAsJsonArray("emotes");
			for(int i = 0; i < emotes.size(); i++) {
				JsonObject entry = emotes.get(i).getAsJsonObject();
				String code = entry.get("code").getAsString();
				IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
				emoticon.setLoadData(new String[] { entry.get("id").getAsString(), entry.get("imageType").getAsString() });
				emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.bttvChannels", entry.get("channel").getAsString()));
			}
		} catch (IOException | JsonParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		try {
			String[] data = (String[]) emoticon.getLoadData();
			EiraMoticonsAPI.loadImage(emoticon, new URI("https:" + urlTemplate.replace("{{id}}", data[0]).replace("{{image}}", "1x")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
