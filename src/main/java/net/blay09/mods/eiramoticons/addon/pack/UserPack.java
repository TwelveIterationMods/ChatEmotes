package net.blay09.mods.eiramoticons.addon.pack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
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
import java.io.InputStreamReader;
import java.util.Map;

public class UserPack implements IEmoticonLoader {

	private ResourceLocation jsonLocation = new ResourceLocation("eiramoticons", "packs/user/user.json");

	public UserPack() {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(jsonLocation);
			if(resource != null) {
				Gson gson = new Gson();
				JsonObject emoticons = gson.fromJson(new InputStreamReader(resource.getInputStream()), JsonObject.class);
				for(Map.Entry<String, JsonElement> entry : emoticons.entrySet()) {
					IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(entry.getKey(), this);
					emoticon.setTooltip(I18n.format("eiramoticons:group.user", entry.getValue().getAsString()));
					emoticon.setLoadData(new ResourceLocation("eiramoticons", "packs/user/" + entry.getKey() + ".png"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
