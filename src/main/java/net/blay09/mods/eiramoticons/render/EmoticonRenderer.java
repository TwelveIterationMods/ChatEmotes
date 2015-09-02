// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.render;

import net.blay09.mods.eiramoticons.emoticon.Emoticon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class EmoticonRenderer {

	public static final float EMOTICON_WIDTH = 16;
	public static final float EMOTICON_HEIGHT = 14;

	private final Minecraft mc;
	private final int spaceWidth;
	private final EmoticonBuffer buffer = new EmoticonBuffer();

	public EmoticonRenderer(Minecraft mc) {
		this.mc = mc;
		spaceWidth = mc.fontRendererObj.getStringWidth("   ");
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void startRenderChat(RenderGameOverlayEvent.Chat event) {
		FontRendererExt.enableEmoticons = true;
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void renderOverlay(RenderGameOverlayEvent.Post event) {
		if(event.type != RenderGameOverlayEvent.ElementType.CHAT) {
			return;
		}
		FontRendererExt.enableEmoticons = false;

		GuiNewChat guiNewChat = mc.ingameGUI.getChatGUI();
		int mouseX = Mouse.getX() * event.resolution.getScaledWidth() / mc.displayWidth;
		int mouseY = event.resolution.getScaledHeight() - Mouse.getY() * event.resolution.getScaledHeight() / mc.displayHeight - 1;

		float chatScale = guiNewChat.getChatScale();
		GlStateManager.pushMatrix();
		GlStateManager.translate(2f, (float) (event.resolution.getScaledHeight() - 48) + 20f, 0f);
		GlStateManager.scale(chatScale, chatScale, 1f);

		Emoticon hoverEmoticon = null;
		for(int i = buffer.count - 1; i >= 0; i--) {
			if(buffer.emoticons[i].getTextureId() == -1) {
				buffer.emoticons[i].requestTexture();
				continue;
			}
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.func_179144_i(buffer.emoticons[i].getTextureId()); // bindTexture
			GlStateManager.color(1f, 1f, 1f, buffer.alpha[i]);

			float renderWidth = (buffer.emoticons[i].getWidth() * buffer.emoticons[i].getScaleX());
			float renderHeight = (buffer.emoticons[i].getHeight() * buffer.emoticons[i].getScaleY());
			float renderX = buffer.positionX[i] + (spaceWidth / 2 - renderWidth / 2);
			float renderY = buffer.positionY[i] + (mc.fontRendererObj.FONT_HEIGHT / 2 - renderHeight / 2);
			GlStateManager.translate(renderX, renderY, 0);
			GlStateManager.scale(buffer.emoticons[i].getScaleX(), buffer.emoticons[i].getScaleY(), 1);
			drawTexturedRect(0, 0, buffer.emoticons[i].getWidth(), buffer.emoticons[i].getHeight());
			GlStateManager.popMatrix();
			if(hoverEmoticon == null) {
				if(mouseX > renderX && mouseX <= renderX + renderWidth * chatScale) {
					if(mouseY > (event.resolution.getScaledHeight() - 32) + renderY && mouseY <= (event.resolution.getScaledHeight() - 32) + renderY + renderHeight * chatScale) {
						hoverEmoticon = buffer.emoticons[i];
					}
				}
			}
		}

		GlStateManager.popMatrix();

		if(hoverEmoticon != null && mc.currentScreen instanceof GuiChat) {
			drawHoveringText(hoverEmoticon.getTooltip(), mouseX, mouseY, event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
		}

		// Clear buffer
		buffer.freeMemory();
	}

	private static void drawTexturedRect(float x, float y, float width, float height) {
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x, y + height);
		GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + width, y);
		GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x, y + height);
		GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + width, y + height);
		GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + width, y);
		GL11.glEnd();
	}

	protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, double zLevel)
	{
		float f = (float)(startColor >> 24 & 255) / 255.0F;
		float f1 = (float)(startColor >> 16 & 255) / 255.0F;
		float f2 = (float)(startColor >> 8 & 255) / 255.0F;
		float f3 = (float)(startColor & 255) / 255.0F;
		float f4 = (float)(endColor >> 24 & 255) / 255.0F;
		float f5 = (float)(endColor >> 16 & 255) / 255.0F;
		float f6 = (float)(endColor >> 8 & 255) / 255.0F;
		float f7 = (float)(endColor & 255) / 255.0F;
		GlStateManager.func_179090_x(); // disableTexture2D
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		worldrenderer.func_178960_a(f1, f2, f3, f); // setColorRGBA_F
		worldrenderer.addVertex((double) right, (double) top, zLevel);
		worldrenderer.addVertex((double) left, (double)top, zLevel);
		worldrenderer.func_178960_a(f5, f6, f7, f4); // setColorRGBA_F
		worldrenderer.addVertex((double) left, (double) bottom, zLevel);
		worldrenderer.addVertex((double) right, (double) bottom, zLevel);
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w(); // enableTexture2D
	}

	protected void drawHoveringText(String[] lines, int mouseX, int mouseY, int width, int height) {
		if(lines != null && lines.length > 0) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);

			int maxLineWidth = 0;
			for(String s : lines) {
				int lineWidth = mc.fontRendererObj.getStringWidth(s);
				if (lineWidth > maxLineWidth)
				{
					maxLineWidth = lineWidth;
				}
			}

			int x = mouseX + 12;
			int y = mouseY - 10;
			int tooltipHeight = 8;

			if(lines.length > 1) {
				tooltipHeight += 2 + (lines.length - 1) * 10;
			}

			if(x + maxLineWidth > width) {
				x -= 28 + maxLineWidth;
			}

			if(y + tooltipHeight + 6 > height) {
				y = height - tooltipHeight - 6;
			}

			int bgColor = -267386864;
			drawGradientRect(x - 3, y - 4, x + maxLineWidth + 3, y - 3, bgColor, bgColor, 300);
			drawGradientRect(x - 3, y + tooltipHeight + 3, x + maxLineWidth + 3, y + tooltipHeight + 4, bgColor, bgColor, 300);
			drawGradientRect(x - 3, y - 3, x + maxLineWidth + 3, y + tooltipHeight + 3, bgColor, bgColor, 300);
			drawGradientRect(x - 4, y - 3, x - 3, y + tooltipHeight + 3, bgColor, bgColor, 300);
			drawGradientRect(x + maxLineWidth + 3, y - 3, x + maxLineWidth + 4, y + tooltipHeight + 3, bgColor, bgColor, 300);
			int fgColor1 = 1347420415;
			int fgColor2 = (fgColor1 & 16711422) >> 1 | fgColor1 & -16777216;
			drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + tooltipHeight + 3 - 1, fgColor1, fgColor2, 300);
			drawGradientRect(x + maxLineWidth + 2, y - 3 + 1, x + maxLineWidth + 3, y + tooltipHeight + 3 - 1, fgColor1, fgColor2, 300);
			drawGradientRect(x - 3, y - 3, x + maxLineWidth + 3, y - 3 + 1, fgColor1, fgColor1, 300);
			drawGradientRect(x - 3, y + tooltipHeight + 2, x + maxLineWidth + 3, y + tooltipHeight + 3, fgColor2, fgColor2, 300);

			for(String line : lines) {
				mc.fontRendererObj.func_175065_a(line, x, y, -1, true);
				y += 10;
			}

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}

	public EmoticonBuffer getBuffer() {
		return buffer;
	}
}
