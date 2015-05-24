// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.blay09.mods.eiramoticons.api.IInternalMethods;

public class InternalMethods implements IInternalMethods {

	@Override
	public IEmoticon registerEmoticon(String name, IEmoticonLoader loader) {
		return EmoticonRegistry.registerEmoticon(name, loader);
	}

}
