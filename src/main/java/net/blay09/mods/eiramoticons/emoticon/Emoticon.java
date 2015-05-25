// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.emoticon;

import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.blay09.mods.eiramoticons.emoticon.AsyncEmoticonLoader;
import net.minecraft.client.renderer.texture.TextureUtil;

import java.awt.image.BufferedImage;

public class Emoticon implements IEmoticon {

	public final IEmoticonLoader loader;
	public final int id;
	public final String name;

	private boolean loadRequested;
	private int textureId = -1;
	private Object identifier;
	private int width;
	private int height;
	private String[] tooltipLines;
	private BufferedImage loadBuffer;

	public Emoticon(int id, String name, IEmoticonLoader loader) {
		this.id = id;
		this.name = name;
		this.loader = loader;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getChatString() {
		return "\u00a7z" + id + "    ";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getLoadData() {
		return identifier;
	}

	@Override
	public void setLoadData(Object loadData) {
		this.identifier = loadData;
	}

	@Override
	public IEmoticonLoader getLoader() {
		return loader;
	}

	@Override
	public void setTooltip(String[] tooltipLines) {
		this.tooltipLines = tooltipLines;
	}

	@Override
	public void setImage(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		loadBuffer = image;
	}

	public int getTextureId() {
		if(loadBuffer != null) {
			textureId = TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(), loadBuffer);
			loadBuffer = null;
		}
		return textureId;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void requestTexture() {
		if(!loadRequested) {
			loadRequested = true;
			AsyncEmoticonLoader.instance.loadAsync(this);
		}
	}

	public String[] getTooltip() {
		return tooltipLines;
	}
}
