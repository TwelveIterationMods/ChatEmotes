// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.api;

import net.minecraft.util.IChatComponent;

public interface IInternalMethods {
	IEmoticon registerEmoticon(String name, IEmoticonLoader loader);
	void registerEmoticonGroup(String groupName, IChatComponent listComponent);
	String replaceEmoticons(String s);
	IChatComponent replaceEmoticons(IChatComponent component);
}
