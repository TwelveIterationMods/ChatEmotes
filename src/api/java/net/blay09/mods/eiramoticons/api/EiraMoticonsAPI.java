// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.api;

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

}
