// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.api;

public class EiraMoticonsAPI {

	private static IInternalMethods internalMethods;

	public static void setupAPI(IInternalMethods internalMethods) {
		EiraMoticonsAPI.internalMethods = internalMethods;
	}

	public static IEmoticon registerEmoticon(String name, IEmoticonLoader loader) {
		return internalMethods.registerEmoticon(name, loader);
	}

}
