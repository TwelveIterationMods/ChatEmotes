package net.blay09.mods.eiramoticons.addon.pack;

import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class IncludedPack implements IEmoticonLoader {

	private String group;
	private String[] emotes;

	public IncludedPack(String group, String[] emotes) {
		this.group = group;
		this.emotes = emotes;

		for(String emote : emotes) {
			IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(emote, this);
			emoticon.setTooltip(I18n.format("eiramoticons:group." + group));
			emoticon.setLoadData(new ResourceLocation("eiramoticons", "/packs/" + group + "/" + emote + ".png"));
		}
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource((ResourceLocation) emoticon.getLoadData());
			if(resource != null) {
				BufferedImage image = ImageIO.read(resource.getInputStream());
				if(image != null) {
					emoticon.setImage(image);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
