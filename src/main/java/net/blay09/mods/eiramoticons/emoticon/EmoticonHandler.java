// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.emoticon;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoticonHandler {

	private static final Pattern EMOTICON_PATTERN = Pattern.compile("[A-Za-z0-9&'#:;<>_/\\-\\|\\(\\)\\.\\\\\\]\\[]+");

	public static String replaceEmoticons(String text) {
		int currentIdx = 0;
		StringBuilder sb = new StringBuilder();
		Matcher matcher = EMOTICON_PATTERN.matcher(text);
		while(matcher.find()) {
			if(matcher.start() > currentIdx) {
				sb.append(text.substring(currentIdx, matcher.start()));
			}
			String word = matcher.group();
			Emoticon emoticon = EmoticonRegistry.fromName(word);
			if(emoticon != null && !emoticon.isManualOnly()) {
				sb.append("\u00a7z").append(emoticon.id).append("    ");
			} else {
				sb.append(word);
			}
			currentIdx = matcher.end();
		}
		if(currentIdx < text.length()) {
			sb.append(text.substring(currentIdx));
		}
		return sb.toString();
	}

	public static IChatComponent adjustChatComponent(IChatComponent chatComponent) {
		if(chatComponent instanceof ChatComponentText) {
			return adjustTextComponent((ChatComponentText) chatComponent);
		} else if(chatComponent instanceof ChatComponentTranslation) {
			return adjustTranslationComponent((ChatComponentTranslation) chatComponent);
		}
		return null;
	}

	public static IChatComponent adjustTextComponent(ChatComponentText chatComponent) {
		ChatComponentText copyComponent = new ChatComponentText(replaceEmoticons(chatComponent.getChatComponentText_TextValue()));
		copyComponent.setChatStyle(chatComponent.getChatStyle());
		for(Object object : chatComponent.getSiblings()) {
			IChatComponent adjustedComponent = adjustChatComponent((IChatComponent) object);
			if(adjustedComponent != null) {
				copyComponent.appendSibling(adjustedComponent);
			}
		}
		return copyComponent;
	}

	public static IChatComponent adjustTranslationComponent(ChatComponentTranslation chatComponent) {
		Object[] formatArgs = chatComponent.getFormatArgs();
		Object[] copyFormatArgs = new Object[formatArgs.length];
		for(int i = 0; i < formatArgs.length; i++) {
			if(formatArgs[i] instanceof IChatComponent) {
				copyFormatArgs[i] = adjustChatComponent((IChatComponent) formatArgs[i]);
			} else {
				ChatComponentText textComponent = new ChatComponentText(formatArgs[i] == null ? "null" : replaceEmoticons(formatArgs[i].toString()));
				textComponent.getChatStyle().setParentStyle(chatComponent.getChatStyle());
				copyFormatArgs[i] = textComponent;
			}
		}
		ChatComponentTranslation copyComponent = new ChatComponentTranslation(chatComponent.getKey(), copyFormatArgs);
		copyComponent.setChatStyle(chatComponent.getChatStyle());
		for(Object object : chatComponent.getSiblings()) {
			IChatComponent adjustedComponent = adjustChatComponent((IChatComponent) object);
			if(adjustedComponent != null) {
				copyComponent.appendSibling(adjustedComponent);
			}
		}
		return copyComponent;
	}

}
