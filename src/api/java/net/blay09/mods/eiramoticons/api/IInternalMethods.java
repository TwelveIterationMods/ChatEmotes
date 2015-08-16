// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.api;

import net.minecraft.util.IChatComponent;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

public interface IInternalMethods {
	IEmoticon registerEmoticon(String name, IEmoticonLoader loader);
	void registerEmoticonGroup(String groupName, IChatComponent listComponent);
	String replaceEmoticons(String s);
	IChatComponent replaceEmoticons(IChatComponent component);
	boolean loadImage(IEmoticon emoticon, URI uri);
	boolean loadImage(IEmoticon emoticon, InputStream in);
	boolean loadImage(IEmoticon emoticon, File file);
}
