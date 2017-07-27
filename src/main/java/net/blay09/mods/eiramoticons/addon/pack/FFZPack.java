package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.EmoteLoaderException;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FFZPack extends AbstractEmotePack {

	public FFZPack() {
		try {
			URL apiURL = new URL("http://api.frankerfacez.com/v1/set/global");
			InputStreamReader reader = new InputStreamReader(apiURL.openStream());
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(reader, JsonObject.class);
			if(root == null) {
				throw new EmoteLoaderException("Failed to grab FrankerFaceZ emotes");
			}
			JsonArray defaultSets = root.getAsJsonArray("default_sets");
			JsonObject sets = root.getAsJsonObject("sets");
			for(int i = 0; i < defaultSets.size(); i++) {
				int setId = defaultSets.get(i).getAsInt();
				JsonObject set = sets.getAsJsonObject(String.valueOf(setId));
				JsonArray emoticons = set.getAsJsonArray("emoticons");
				for(int j = 0; j < emoticons.size(); j++) {
					JsonObject emoticonObject = emoticons.get(j).getAsJsonObject();
					String code = emoticonObject.get("name").getAsString();
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
					emoticon.setLoadData(emoticonObject.getAsJsonObject("urls").get("1").getAsString());
					emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.ffz"));
				}
			}
		} catch (Exception e) {
			throw new EmoteLoaderException("Unhandled exception", e);
		}
		ITextComponent linkComponent = new TextComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.frankerfacez.com/channel/__ffz_global"));
		linkComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("https://www.frankerfacez.com/channel/__ffz_global")));
		linkComponent.getStyle().setColor(TextFormatting.GOLD);
		linkComponent.getStyle().setBold(true);
		linkComponent.getStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("FrankerFaceZ", new TextComponentTranslation("eiramoticons:command.list.twitch.ffz", linkComponent));
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		try {
			EiraMoticonsAPI.loadImage(emoticon, new URI("https:" + emoticon.getLoadData()));
		} catch (URISyntaxException e) {
			throw new EmoteLoaderException(e);
		}
	}

}
