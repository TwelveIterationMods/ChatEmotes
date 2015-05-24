// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.render;

import net.blay09.mods.eiramoticons.Emoticon;
import net.blay09.mods.eiramoticons.EmoticonRegistry;
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
		return super.getStringWidth(enableEmoticons ? killEmotes(s) : s);
	}

	@Override
	public int drawString(String s, int x, int y, int color, boolean shadow) {
		return super.drawString(enableEmoticons ? extractEmotesToBuffer(s, x, y, color) : s, x, y, color, shadow);
	}

	public void setEmoticonBuffer(EmoticonBuffer emoticonBuffer) {
		this.emoticonBuffer = emoticonBuffer;
	}

	public String killEmotes(String s) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '\u00a7' && s.charAt(i + 1) == 'z') {
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

	public String extractEmotesToBuffer(String s, int x, int y, int color) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '\u00a7' && s.charAt(i + 1) == 'z') {
				int nextWhitespace = s.indexOf(' ', i);
				if(nextWhitespace == -1) {
					nextWhitespace = s.length();
				}
				int nextFormatting = s.indexOf('\u00a7');
				if(nextFormatting == -1) {
					nextFormatting = s.length();
				}
				String emoteId = s.substring(i + 2, Math.min(nextWhitespace, nextFormatting));
				Emoticon emoticon = EmoticonRegistry.fromId(Integer.parseInt(emoteId));
				if(emoticon != null) {
					emoticonBuffer.emoticons = ArrayUtils.add(emoticonBuffer.emoticons, emoticon);
					emoticonBuffer.positionX = ArrayUtils.add(emoticonBuffer.positionX, x + super.getStringWidth(sb.toString()));
					emoticonBuffer.positionY = ArrayUtils.add(emoticonBuffer.positionY, y);
					emoticonBuffer.alpha = ArrayUtils.add(emoticonBuffer.alpha, (float) (color >> 24 & 255) / 255.0f);
				}
				i = nextWhitespace;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}



}
