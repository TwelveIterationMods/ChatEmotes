// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.blay09.mods.eiramoticons.api.IInternalMethods;
import net.minecraft.util.IChatComponent;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Iterator;

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

	@Override
	public boolean loadImage(IEmoticon emoticon, URI uri) {
		try {
			return loadImageInternal(emoticon, uri.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean loadImage(IEmoticon emoticon, File file) {
		return loadImageInternal(emoticon, file);
	}

	@Override
	public boolean loadImage(IEmoticon emoticon, InputStream in) {
		return loadImageInternal(emoticon, in);
	}

	private boolean loadImageInternal(IEmoticon emoticon, Object obj) {
		try {
			ImageInputStream in = ImageIO.createImageInputStream(obj);
			Iterator<ImageReader> it = ImageIO.getImageReaders(in);
			if(it.hasNext()) {
				ImageReader reader = it.next();
				reader.setInput(in);
				int numImages = reader.getNumImages(true);
				if(numImages > 1) {
					BufferedImage[] images = new BufferedImage[numImages];
					for(int i = 0; i < images.length; i++) {
						images[i] = reader.read(reader.getMinIndex() + i);
					}
					IIOMetadata metadata = reader.getImageMetadata(0);
					String metaFormatName = metadata.getNativeMetadataFormatName();
					IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);
					NodeList childNodes = root.getChildNodes();
					for(int i = 0; i < childNodes.getLength(); i++) {
						if(childNodes.item(i).getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
							emoticon.setAnimationSpeed(Integer.parseInt(((IIOMetadataNode) childNodes.item(i)).getAttribute("delayTime")) * 10);
							emoticon.setCumulativeRendering(((IIOMetadataNode) childNodes.item(i)).getAttribute("disposalMethod").equals("doNotDispose"));
							break;
						}
					}
					emoticon.setImages(images);
				} else {
					emoticon.setImage(reader.read(0));
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return false;
	}
}
