package net.blay09.mods.eiramoticons.addon;

import com.google.common.io.Files;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.File;
import java.io.FilenameFilter;

public class FileAddon implements IEmoticonLoader {

	public FileAddon() {
		File emoticonDir = new File(Minecraft.getMinecraft().mcDataDir, "emoticons");
		if(!emoticonDir.exists()) {
			if(!emoticonDir.mkdir()) {
				return;
			}
		}
		File[] emoticons = emoticonDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(".gif");
			}
		});
		StringBuilder sb = new StringBuilder();
		if(emoticons != null) {
			for(File emoticonFile : emoticons) {
				String nameWithoutExt = Files.getNameWithoutExtension(emoticonFile.getName());
				IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(nameWithoutExt, this);
				sb.append(" ").append(nameWithoutExt);
				emoticon.setLoadData(emoticonFile);
				emoticon.setTooltip(I18n.format("eiramoticons:group.custom"));
			}
		}
		if(sb.length() > 0) {
			EiraMoticonsAPI.registerEmoticonGroup("File Addon", EiraMoticonsAPI.replaceEmoticons(new TextComponentTranslation("eiramoticons:command.list.file", sb.toString())));
		}
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		EiraMoticonsAPI.loadImage(emoticon, (File) emoticon.getLoadData());
	}

}
