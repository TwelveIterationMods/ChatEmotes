// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.api;

import net.minecraft.util.IChatComponent;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

public class EiraMoticonsAPI {

	private static IInternalMethods internalMethods;

	/**
	 * INTERNAL METHOD. DO NOT CALL.
	 * @param internalMethods implementation of internal API methods
	 */
	public static void setupAPI(IInternalMethods internalMethods) {
		EiraMoticonsAPI.internalMethods = internalMethods;
	}

	/**
	 * Registers an emoticon to be used in Minecraft.
	 * @param name the name that will be the code to use the emote in chat
	 * @param loader the loader that provides the BufferedImage for this emote
	 * @return an emoticon instance that can be used to set up further properties of the emote
	 */
	public static IEmoticon registerEmoticon(String name, IEmoticonLoader loader) {
		return internalMethods.registerEmoticon(name, loader);
	}

	/**
	 * Registers an emoticon group to be displayed in the /emoticons list command.
	 * @param groupName the name of this group
	 * @param listComponent the chat component that should be displayed when the /emoticons list command is run
	 */
	public static void registerEmoticonGroup(String groupName, IChatComponent listComponent) {
		internalMethods.registerEmoticonGroup(groupName, listComponent);
	}

	/**
	 * Replaces emoticon names in the string with the respective emoticon formatting codes.
	 * @param s the string to be searched for emoticons
	 */
	public static String replaceEmoticons(String s) {
		return internalMethods.replaceEmoticons(s);
	}

	/**
	 * Creates a complete copy of this chat component, replacing all emoticon names with the actual emoticons.
	 * @param component the component to be searched for emoticons
	 * @return a copy of the given chat component with emoticons
	 */
	public static IChatComponent replaceEmoticons(IChatComponent component) {
		return internalMethods.replaceEmoticons(component);
	}

	/**
	 * @param emoticon
	 * @param uri
	 * @return
	 */
	public static boolean loadImage(IEmoticon emoticon, URI uri) {
		return internalMethods.loadImage(emoticon, uri);
	}

	/**
	 *
	 * @param emoticon
	 * @param in
	 * @return
	 */
	public static boolean loadImage(IEmoticon emoticon, InputStream in) {
		return internalMethods.loadImage(emoticon, in);
	}

	/**
	 *
	 * @param emoticon
	 * @param file
	 * @return
	 */
	public static boolean loadImage(IEmoticon emoticon, File file) {
		return internalMethods.loadImage(emoticon, file);
	}

	public static void setChatContainer(ChatContainer chatContainer) {
		internalMethods.setChatContainer(chatContainer);
	}
}
