// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon.pack;

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
import java.net.URL;

public class TwitchSmileyPack implements IEmoticonLoader {

	private static final String TEMPLATE = "http://static-cdn.jtvnw.net/emoticons/v1/{image_id}/1.0";

	private final int smileySet;

	public TwitchSmileyPack(int smileySet) {
		this.smileySet = smileySet;
		switch(smileySet) {
			case 0:
				registerSmiley(":)", 1); registerSmiley(":-)", 1);
				registerSmiley(":(", 2); registerSmiley(":-(", 2);
				registerSmiley(":D", 3); registerSmiley(":-D", 3);
				registerSmiley(">(", 4);
				registerSmiley(":z", 5); registerSmiley(":Z", 5);
				registerSmiley("o_o", 6); registerSmiley("O_O", 6); registerSmiley("o_O", 6); registerSmiley("O_o", 6);
				registerSmiley("B)", 7); registerSmiley("B-)", 7);
				registerSmiley(":o", 8); registerSmiley(":O", 8); registerSmiley(":-o", 8); registerSmiley(":-O", 8);
				registerSmiley("<3", 9);
				registerSmiley(":\\", 10); registerSmiley(":-\\", 10);
				registerSmiley(";)", 11); registerSmiley(";-)", 11);
				registerSmiley(":p", 12); registerSmiley(":P", 12); registerSmiley(":-p", 12); registerSmiley(":-P", 12);
				registerSmiley(";p", 13); registerSmiley(";P", 13);
				registerSmiley("R)", 14); registerSmiley("R-)", 14);
				break;
			case 1:
				break;
			case 2:
				break;
		}
		IChatComponent linkComponent = new ChatComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitchemotes.com/"));
		linkComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("https://twitchemotes.com/")));
		linkComponent.getChatStyle().setColor(EnumChatFormatting.GOLD);
		linkComponent.getChatStyle().setBold(true);
		linkComponent.getChatStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("Twitch Smileys", new ChatComponentTranslation("eiramoticons:command.list.twitch.smileys", linkComponent));
	}

	private void registerSmiley(String code, int id) {
		IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
		emoticon.setLoadData(id);
		emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.smiley"));
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		try {
			BufferedImage image = ImageIO.read(new URL(TEMPLATE.replace("{image_id}", emoticon.getLoadData().toString())));
			if(image != null) {
				emoticon.setImage(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
