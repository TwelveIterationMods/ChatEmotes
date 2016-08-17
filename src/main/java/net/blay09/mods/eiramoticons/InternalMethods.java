package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.api.ChatContainer;
import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.blay09.mods.eiramoticons.api.IInternalMethods;
import net.minecraft.util.text.ITextComponent;
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
import java.net.URI;
import java.util.Iterator;

public class InternalMethods implements IInternalMethods {

	@Override
	public IEmoticon registerEmoticon(String name, IEmoticonLoader loader) {
		return EmoticonRegistry.registerEmoticon(name, loader);
	}

	@Override
	public void registerEmoticonGroup(String groupName, ITextComponent listComponent) {
		EmoticonRegistry.registerEmoticonGroup(groupName, listComponent);
	}

	@Override
	public String replaceEmoticons(String s) {
		return EmoticonHandler.replaceEmoticons(s);
	}

	@Override
	public ITextComponent replaceEmoticons(ITextComponent component) {
		return EmoticonHandler.adjustChatComponent(component);
	}

	@Override
	public boolean loadImage(IEmoticon emoticon, URI uri) {
		try(InputStream in = uri.toURL().openStream()) {
			return loadImageInternal(emoticon, in);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean loadImage(IEmoticon emoticon, File file) {
		return loadImageInternal(emoticon, file);
	}

	@Override
	public void setChatContainer(ChatContainer chatContainer) {
		ClientProxy.renderer.setChatContainer(chatContainer);
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
					int[] frameTime = new int[numImages];
					int[] offsetX = new int[numImages];
					int[] offsetY = new int[numImages];
					BufferedImage[] images = new BufferedImage[numImages];
					for(int i = 0; i < images.length; i++) {
						images[i] = reader.read(reader.getMinIndex() + i);
						IIOMetadata metadata = reader.getImageMetadata(i);
						String metaFormatName = metadata.getNativeMetadataFormatName();
						IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);
						NodeList childNodes = root.getChildNodes();
						for(int j = 0; j < childNodes.getLength(); j++) {
							if(childNodes.item(j).getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
								frameTime[i] = Integer.parseInt(((IIOMetadataNode) childNodes.item(j)).getAttribute("delayTime")) * 10;
							}
							if(childNodes.item(j).getNodeName().equalsIgnoreCase("ImageDescriptor")) {
								try {
									offsetX[i] = Integer.parseInt(((IIOMetadataNode) childNodes.item(j)).getAttribute("imageLeftPosition"));
								} catch (NumberFormatException ignored) {}
								try {
									offsetY[i] = Integer.parseInt(((IIOMetadataNode) childNodes.item(j)).getAttribute("imageTopPosition"));
								} catch (NumberFormatException ignored) {}
							}
						}
					}
					IIOMetadata metadata = reader.getImageMetadata(0);
					String metaFormatName = metadata.getNativeMetadataFormatName();
					IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);
					NodeList childNodes = root.getChildNodes();
					for(int i = 0; i < childNodes.getLength(); i++) {
						if(childNodes.item(i).getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
							emoticon.setCumulativeRendering(((IIOMetadataNode) childNodes.item(i)).getAttribute("disposalMethod").equals("doNotDispose"));
							break;
						}
					}
					emoticon.setImages(images, frameTime, offsetX, offsetY);
				} else {
					emoticon.setImage(reader.read(0));
				}
			}
			in.close();
			return true;
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return false;
	}
}
