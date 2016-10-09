package net.blay09.mods.eiramoticons.addon.pack;

import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.awt.image.BufferedImage;

public class TwitchSmileyPack extends AbstractEmotePack {

	private static final String TEMPLATE = "http://static-cdn.jtvnw.net/emoticons/v1/{image_id}/1.0";

	private final int smileySet;

	public TwitchSmileyPack(int smileySet) {
		this.smileySet = smileySet;
		switch(smileySet) {
			case 0:
				registerSmiley(":)", 1); registerSmiley(":-)", 1);
				registerSmiley(":(", 2); registerSmiley(":-(", 2); registerSmiley(":'(", 2);
				registerSmiley(":D", 3); registerSmiley(":-D", 3);
				registerSmiley(">(", 4);
				registerSmiley(":z", 5); registerSmiley(":Z", 5); registerSmiley(":-z", 5); registerSmiley(":-Z", 5); registerSmiley(":|", 5); registerSmiley(":-|", 5);
				registerSmiley("o_o", 6); registerSmiley("O_O", 6); registerSmiley("o_O", 6); registerSmiley("O_o", 6); registerSmiley("o.o", 6); registerSmiley("O.O", 6); registerSmiley("o.O", 6); registerSmiley("O.o", 6);
				registerSmiley("B)", 7); registerSmiley("B-)", 7);
				registerSmiley(":o", 8); registerSmiley(":O", 8); registerSmiley(":-o", 8); registerSmiley(":-O", 8);
				registerSmiley("<3", 9);
				registerSmiley(":\\", 10); registerSmiley(":-\\", 10); registerSmiley(":/", 10); registerSmiley(":-/", 10);
				registerSmiley(";)", 11); registerSmiley(";-)", 11);
				registerSmiley(":p", 12); registerSmiley(":P", 12); registerSmiley(":-p", 12); registerSmiley(":-P", 12);
				registerSmiley(";p", 13); registerSmiley(";P", 13); registerSmiley(";-p", 13); registerSmiley(";-P", 13);
				registerSmiley("R)", 14); registerSmiley("R-)", 14);
				break;
			case 1:
				break;
			case 2:
				break;
		}
		ITextComponent linkComponent = new TextComponentTranslation("eiramoticons:command.list.clickHere");
		linkComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitchemotes.com/"));
		linkComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("https://twitchemotes.com/")));
		linkComponent.getStyle().setColor(TextFormatting.GOLD);
		linkComponent.getStyle().setBold(true);
		linkComponent.getStyle().setUnderlined(true);
		EiraMoticonsAPI.registerEmoticonGroup("Twitch Smileys", new TextComponentTranslation("eiramoticons:command.list.twitch.smileys", linkComponent));
	}

	private void registerSmiley(String code, int id) {
		IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(code, this);
		emoticon.setLoadData(id);
		emoticon.setTooltip(I18n.format("eiramoticons:group.twitch.smiley"));
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		BufferedImage image = TwitchEmotesAPI.readTwitchEmoteImage(TEMPLATE, (Integer) emoticon.getLoadData(), "smiley");
		if(image != null) {
			emoticon.setImage(image);
			if(image.getWidth() <= TwitchEmotesAPI.TWITCH_BASE_SIZE || image.getHeight() <= TwitchEmotesAPI.TWITCH_BASE_SIZE) {
				emoticon.setScale(0.5f, 0.5f);
			}
		}
	}

}
