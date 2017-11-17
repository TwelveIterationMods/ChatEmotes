package net.blay09.mods.eiramoticons.emoticon;

import net.blay09.mods.eiramoticons.ClientProxy;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.blay09.mods.eiramoticons.render.EmoticonRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Emoticon implements IEmoticon {

	private static class DrawImageCallback implements ImageObserver {
		private boolean isReady;

		public void prepare() {
			isReady = false;
		}

		@Override
		public boolean imageUpdate(Image img, int infoFlags, int x, int y, int width, int height) {
			if((infoFlags & ALLBITS) == ALLBITS) {
				isReady = true;
			} else if((infoFlags & ABORT) == ABORT) {
				isReady = true;
			}
			return false;
		}

		public boolean isReady() {
			return isReady;
		}

	}

	public final IEmoticonLoader loader;
	public final int id;
	public final String code;

	private Object identifier;
	private String[] tooltipLines;
	private boolean manualOnly;

	private boolean loadRequested;
	private int textureId = -1;
	private int width;
	private int height;
	private float scaleX;
	private float scaleY;
	private BufferedImage loadBuffer;

	private int[] frameTimes;
	private boolean cumulativeRendering;
	private int animationFrames;
	private int spriteSheetWidth;
	private int spriteSheetHeight;

	private int animationTime;
	private int currentFrameTime;
	private int currentFrame;
	private int currentFrameTexCoordX;
	private int currentFrameTexCoordY;
	private long lastRenderTime;

	public Emoticon(int id, String code, IEmoticonLoader loader) {
		this.id = id;
		this.code = code;
		this.loader = loader;

		tooltipLines = new String[] {I18n.format("eiramoticons:tooltip.name", code)};
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
		return code;
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
	public boolean isManualOnly() {
		return manualOnly;
	}

	@Override
	public void setManualOnly(boolean manualOnly) {
		this.manualOnly = manualOnly;
	}

	@Override
	public void setTooltip(String emoticonGroup) {
		tooltipLines = new String[] {I18n.format("eiramoticons:tooltip.name", code), I18n.format("eiramoticons:tooltip.group", emoticonGroup)};
	}

	@Override
	public void setCustomTooltip(String[] tooltipLines) {
		this.tooltipLines = tooltipLines;
	}

	private void calculateScale() {
		float renderWidth = width;
		float renderHeight = height;
		if(renderWidth > EmoticonRenderer.EMOTICON_WIDTH) {
			float factor = EmoticonRenderer.EMOTICON_WIDTH / renderWidth;
			renderWidth *= factor;
			renderHeight *= factor;
		}
		if(renderHeight > EmoticonRenderer.EMOTICON_HEIGHT) {
			float factor = EmoticonRenderer.EMOTICON_HEIGHT / renderHeight;
			renderWidth *= factor;
			renderHeight *= factor;
		}
		scaleX = renderWidth / width;
		scaleY = renderHeight / height;
	}

	@Override
	public void setImage(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		calculateScale();
		loadBuffer = image;
	}

	@Override
	public void setImages(BufferedImage[] images, int[] frameTime, int[] offsetX, int[] offsetY) {
		this.frameTimes = frameTime;
		width = images[0].getWidth();
		height = images[0].getHeight();
		animationFrames = images.length;
		calculateScale();
		spriteSheetWidth = width * images.length;
		spriteSheetHeight = height;
		if(spriteSheetWidth > ClientProxy.MAX_TEXTURE_SIZE) {
			int overflowX = (ClientProxy.MAX_TEXTURE_SIZE % width);
			spriteSheetWidth = ClientProxy.MAX_TEXTURE_SIZE - overflowX;
			spriteSheetHeight = (int) (height * (Math.ceil(overflowX / ClientProxy.MAX_TEXTURE_SIZE) + 1));
			if(spriteSheetHeight > ClientProxy.MAX_TEXTURE_SIZE) {
				loadBuffer = images[0];
				return;
			}
		}
		int framesPerX = spriteSheetWidth / width;
		int framesPerY = spriteSheetHeight / height;
		loadBuffer = new BufferedImage(spriteSheetWidth, spriteSheetHeight, BufferedImage.TYPE_INT_ARGB);
		DrawImageCallback callback = new DrawImageCallback();
		Graphics2D g = loadBuffer.createGraphics();
		for(int y = 0; y < framesPerY; y++) {
			for(int x = 0; x < framesPerX; x++) {
				int frameIdx = x + y * framesPerX;
				if(cumulativeRendering) {
					if (frameIdx > 0) {
						int prevFrameIdx = frameIdx - 1;
						int prevFrameX = prevFrameIdx % framesPerX;
						int prevFrameY = (int) Math.floor((float) prevFrameIdx / (float) framesPerX);
						int dx = x * width - prevFrameX * width;
						int dy = y * height - prevFrameY * height;
						g.copyArea(prevFrameX * width, prevFrameY * height, width, height, dx, dy);
					}
				}
				callback.prepare();
				if(!g.drawImage(images[frameIdx], x * width + offsetX[frameIdx], y * height + offsetY[frameIdx], callback)) {
					while (!callback.isReady()) ;
				}
			}
		}
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

	public void disposeTexture() {
		if(textureId != -1) {
			TextureUtil.deleteTexture(textureId);
		}
	}

	public String[] getTooltip() {
		return tooltipLines;
	}

	@Override
	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public float getScaleX() {
		return scaleX;
	}

	@Override
	public float getScaleY() {
		return scaleY;
	}

	@Override
	public boolean isAnimated() {
		return frameTimes != null;
	}

	public void updateAnimation() {
		long now = System.currentTimeMillis();
		if(lastRenderTime == 0) {
			lastRenderTime = now;
			currentFrameTime = frameTimes[0];
		}
		animationTime += now - lastRenderTime;
		int lastFrame = currentFrame;
		while(animationTime > currentFrameTime) {
			animationTime -= currentFrameTime;
			currentFrame++;
			if(currentFrame >= animationFrames) {
				currentFrame = 0;
			}
			currentFrameTime = frameTimes[currentFrame];
		}
		if(currentFrame != lastFrame) {
			currentFrameTexCoordX = currentFrame * width;
			// TODO turn this into math when you got a brain
			while(currentFrameTexCoordX > spriteSheetWidth) {
				currentFrameTexCoordX -= spriteSheetWidth;
				currentFrameTexCoordY += height;
			}
		}
		lastRenderTime = now;
	}

	public int getCurrentFrameTexCoordX() {
		return currentFrameTexCoordX;
	}

	public int getCurrentFrameTexCoordY() {
		return currentFrameTexCoordY;
	}

	public int getSheetWidth() {
		return spriteSheetWidth;
	}

	public int getSheetHeight() {
		return spriteSheetHeight;
	}

	@Override
	public void setCumulativeRendering(boolean cumulativeRendering) {
		this.cumulativeRendering = cumulativeRendering;
	}

	@Override
	public boolean isCumulativeRendering() {
		return cumulativeRendering;
	}
}
