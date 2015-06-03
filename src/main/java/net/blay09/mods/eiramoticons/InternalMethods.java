// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.blay09.mods.eiramoticons.api.IInternalMethods;
import net.minecraft.util.IChatComponent;

public class InternalMethods implements IInternalMethods {

	@Override
	public IEmoticon registerEmoticon(String name, IEmoticonLoader loader) {
		return EmoticonRegistry.registerEmoticon(name, loader);
	}

	@Override
	public void registerEmoticonGroup(String groupName, IChatComponent listComponent) {
		EmoticonRegistry.registerEmoticonGroup(groupName, listComponent);
	}

	@Override
	public String replaceEmoticons(String s) {
		return EmoticonHandler.replaceEmoticons(s);
	}

	@Override
	public IChatComponent replaceEmoticons(IChatComponent component) {
		return EmoticonHandler.adjustChatComponent(component);
	}
}
