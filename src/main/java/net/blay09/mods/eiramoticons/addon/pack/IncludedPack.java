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

public class IncludedPack implements IEmoticonLoader {

	public IncludedPack(String group, String[] emotes) {
		StringBuilder sb = new StringBuilder();
		for(String emote : emotes) {
			IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(emote, this);
			emoticon.setTooltip(I18n.format("eiramoticons:group." + group));
			emoticon.setLoadData(new ResourceLocation("eiramoticons", "packs/" + group + "/" + emote + ".png"));
			sb.append("  ").append(emote);
		}
		EiraMoticonsAPI.registerEmoticonGroup(group, EiraMoticonsAPI.replaceEmoticons(new TextComponentTranslation("eiramoticons:command.list." + group, sb.toString())));
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
