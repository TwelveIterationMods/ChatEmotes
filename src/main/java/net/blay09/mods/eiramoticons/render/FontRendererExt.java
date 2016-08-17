package net.blay09.mods.eiramoticons.render;

import net.blay09.mods.eiramoticons.emoticon.Emoticon;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

public class FontRendererExt extends FontRenderer {

	public static boolean enableEmoticons;

	private EmoticonBuffer emoticonBuffer;

	public FontRendererExt(GameSettings gameSettings, ResourceLocation locationFontTexture, TextureManager renderEngine, boolean unicodeFlag) {
		super(gameSettings, locationFontTexture, renderEngine, unicodeFlag);
	}

	@Override
	public int getStringWidth(String s) {
		return super.getStringWidth(killEmotes(s));
	}

	@Override
	public int drawString(String s, float x, float y, int color, boolean shadow) {
		return super.drawString(enableEmoticons ? extractEmotesToBuffer(s, x, y, color) : s, x, y, color, shadow);
	}

	public void setEmoticonBuffer(EmoticonBuffer emoticonBuffer) {
		this.emoticonBuffer = emoticonBuffer;
	}

	public String killEmotes(String s) {
		if(s == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '\u00a7' && s.length() > i + 1 && s.charAt(i + 1) == 'z') {
				int nextWhitespace = s.indexOf(' ', i);
				if(nextWhitespace == -1) {
					nextWhitespace = s.length();
				}
				i = nextWhitespace;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public String extractEmotesToBuffer(String s, float x, float y, int color) {
		if(s == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '\u00a7' && s.length() > i + 1 && s.charAt(i + 1) == 'z') {
				int nextWhitespace = s.indexOf(' ', i);
				if(nextWhitespace == -1) {
					nextWhitespace = s.length() - 1;
				}
				String emoteId = s.substring(i + 2, nextWhitespace);
				try {
					Emoticon emoticon = EmoticonRegistry.fromId(Integer.parseInt(emoteId));
					if (emoticon != null) {
						emoticonBuffer.addEmoticon(emoticon, x + super.getStringWidth(sb.toString()), y, (float) (color >> 24 & 255) / 255.0f);
					}
				} catch (NumberFormatException ignored) {}
				i = nextWhitespace;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}



}
