// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.emoticon;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoticonHandler {

	private static final Pattern EMOTICON_PATTERN = Pattern.compile("[A-Za-z0-9#:;<>_/\\|\\(\\)\\.\\\\\\]\\[]+");

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

	public static ITextComponent adjustChatComponent(ITextComponent chatComponent) {
		if(chatComponent instanceof TextComponentString) {
			return adjustTextComponent((TextComponentString) chatComponent);
		} else if(chatComponent instanceof TextComponentTranslation) {
			return adjustTranslationComponent((TextComponentTranslation) chatComponent);
		}
		return chatComponent;
	}

	public static ITextComponent adjustTextComponent(TextComponentString chatComponent) {
		TextComponentString copyComponent = new TextComponentString(replaceEmoticons(chatComponent.getText()));
		copyComponent.setStyle(chatComponent.getStyle());
		for(Object object : chatComponent.getSiblings()) {
			ITextComponent adjustedComponent = adjustChatComponent((ITextComponent) object);
			copyComponent.appendSibling(adjustedComponent);
		}
		return copyComponent;
	}

	public static ITextComponent adjustTranslationComponent(TextComponentTranslation chatComponent) {
		Object[] formatArgs = chatComponent.getFormatArgs();
		Object[] copyFormatArgs = new Object[formatArgs.length];
		for(int i = 0; i < formatArgs.length; i++) {
			if(formatArgs[i] instanceof ITextComponent) {
				copyFormatArgs[i] = adjustChatComponent((ITextComponent) formatArgs[i]);
			} else {
				TextComponentString textComponent = new TextComponentString(formatArgs[i] == null ? "null" : replaceEmoticons(formatArgs[i].toString()));
				textComponent.getStyle().setParentStyle(chatComponent.getStyle());
				copyFormatArgs[i] = textComponent;
			}
		}
		TextComponentTranslation copyComponent = new TextComponentTranslation(chatComponent.getKey(), copyFormatArgs);
		copyComponent.setStyle(chatComponent.getStyle());
		for(Object object : chatComponent.getSiblings()) {
			ITextComponent adjustedComponent = adjustChatComponent((ITextComponent) object);
			copyComponent.appendSibling(adjustedComponent);
		}
		return copyComponent;
	}

}
