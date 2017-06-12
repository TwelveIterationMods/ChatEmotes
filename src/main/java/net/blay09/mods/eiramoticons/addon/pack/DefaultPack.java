package net.blay09.mods.eiramoticons.addon.pack;

import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.EmoteLoaderException;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.IOException;
import java.util.Locale;

public class DefaultPack implements IEmoticonLoader {

	public DefaultPack() {
		String[] emotes = new String[]{"rage", "lewd", "scared", "cri", "meow", "yawn", "fufu", "praise", "arr"};
		StringBuilder sb = new StringBuilder();
		for (String emote : emotes) {
			IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon("eira" + emote.substring(0, 1).toUpperCase(Locale.ENGLISH) + emote.substring(1), this);
			emoticon.setTooltip(I18n.format("eiramoticons:group.default"));
			emoticon.setLoadData(new ResourceLocation("eiramoticons", "packs/default/" + emote + ".png"));
			sb.append("  ").append(emote);
		}
		EiraMoticonsAPI.registerEmoticonGroup("default", EiraMoticonsAPI.replaceEmoticons(new TextComponentTranslation("eiramoticons:command.list.default", sb.toString())));
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource((ResourceLocation) emoticon.getLoadData());
			EiraMoticonsAPI.loadImage(emoticon, resource.getInputStream());
		} catch (IOException e) {
			throw new EmoteLoaderException(e);
		}
	}
}
