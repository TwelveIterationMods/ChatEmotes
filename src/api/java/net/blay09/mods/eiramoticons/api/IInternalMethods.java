package net.blay09.mods.eiramoticons.api;

import net.minecraft.util.text.ITextComponent;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

public interface IInternalMethods {
	IEmoticon registerEmoticon(String name, IEmoticonLoader loader);
	void registerEmoticonGroup(String groupName, ITextComponent listComponent);
	String replaceEmoticons(String s);
	ITextComponent replaceEmoticons(ITextComponent component);
	boolean loadImage(IEmoticon emoticon, URI uri);
	boolean loadImage(IEmoticon emoticon, InputStream in);
	boolean loadImage(IEmoticon emoticon, File file);
	void setChatContainer(ChatContainer chatContainer);
}
