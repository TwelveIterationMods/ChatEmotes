package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.*;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.EmoteLoaderException;
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

public class EiraNetPack implements IEmoticonLoader {

	private String urlTemplate;

	public EiraNetPack() {
		try {
			URL apiURL = new URL("http://blay09.net/mods/control-panel/api/emotes.php");
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
			throw new EmoteLoaderException(e);
		}
		ITextComponent linkComponent = new TextComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.patreon.com/blay09"));
		linkComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("https://www.patreon.com/blay09")));
		linkComponent.getStyle().setColor(TextFormatting.GOLD);
		linkComponent.getStyle().setBold(true);
		linkComponent.getStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("Patreon Emotes", new TextComponentTranslation("eiramoticons:command.list.eiranet", linkComponent));
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
