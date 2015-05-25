// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.eiramoticons.emoticon.Emoticon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class EmoticonRenderer {

	private static final float EMOTICON_HEIGHT = 14;

	private final Minecraft mc;
	private final EmoticonBuffer buffer = new EmoticonBuffer();

	public EmoticonRenderer(Minecraft mc) {
		this.mc = mc;
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
		int mouseX = event.mouseX;
		int mouseY = event.mouseY;

		float chatScale = guiNewChat.func_146244_h(); // mc.gameSettings.chatScale
		GL11.glPushMatrix();
		GL11.glTranslatef(2f, (float) (event.resolution.getScaledHeight() - 48) + 20f, 0f);
		GL11.glScalef(chatScale, chatScale, 1f);

		Emoticon hoverEmoticon = null;
		for(int i = buffer.emoticons.length - 1; i >= 0; i--) {
			if(buffer.emoticons[i].getTextureId() == -1) {
				buffer.emoticons[i].requestTexture();
				continue;
			}
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer.emoticons[i].getTextureId());
			GL11.glColor4f(1f, 1f, 1f, buffer.alpha[i]);
			float scale = Math.min(1, EMOTICON_HEIGHT / buffer.emoticons[i].getHeight());
			GL11.glTranslatef(buffer.positionX[i], buffer.positionY[i] - 3, 0);
			GL11.glScalef(scale, scale, 1);
			drawTexturedRect(0, 0, buffer.emoticons[i].getWidth(), buffer.emoticons[i].getHeight());
			GL11.glPopMatrix();
			if(hoverEmoticon == null && mouseX > buffer.positionX[i] && mouseX <= buffer.positionX[i] + buffer.emoticons[i].getWidth() * scale * chatScale && mouseY > (event.resolution.getScaledHeight() - 32) + buffer.positionY[i] && mouseY <= (event.resolution.getScaledHeight() - 32) + buffer.positionY[i] + buffer.emoticons[i].getHeight() * scale * chatScale) {
				hoverEmoticon = buffer.emoticons[i];
			}
		}

		GL11.glPopMatrix();

		if(hoverEmoticon != null) {
			drawHoveringText(hoverEmoticon.getTooltip(), event.mouseX, event.mouseY, event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
		}

		// Clear buffer
		buffer.emoticons = new Emoticon[0];
		buffer.positionX = new int[0];
		buffer.positionY = new int[0];
		buffer.alpha = new float[0];
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

	private static void drawGradientRect(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_, float zLevel) {
		float f = (float)(p_73733_5_ >> 24 & 255) / 255.0F;
		float f1 = (float)(p_73733_5_ >> 16 & 255) / 255.0F;
		float f2 = (float)(p_73733_5_ >> 8 & 255) / 255.0F;
		float f3 = (float)(p_73733_5_ & 255) / 255.0F;
		float f4 = (float)(p_73733_6_ >> 24 & 255) / 255.0F;
		float f5 = (float)(p_73733_6_ >> 16 & 255) / 255.0F;
		float f6 = (float)(p_73733_6_ >> 8 & 255) / 255.0F;
		float f7 = (float)(p_73733_6_ & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex((double)p_73733_3_, (double)p_73733_2_, (double) zLevel);
		tessellator.addVertex((double)p_73733_1_, (double)p_73733_2_, (double) zLevel);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex((double)p_73733_1_, (double)p_73733_4_, (double) zLevel);
		tessellator.addVertex((double)p_73733_3_, (double)p_73733_4_, (double) zLevel);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	protected void drawHoveringText(String[] lines, int mouseX, int mouseY, int width, int height) {
		if(lines != null && lines.length > 0) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);

			int maxLineWidth = 0;
			for(String s : lines) {
				int lineWidth = mc.fontRenderer.getStringWidth(s);
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
			drawGradientRect(x - 3, y - 4, x + maxLineWidth + 3, y - 3, bgColor, bgColor, 300f);
			drawGradientRect(x - 3, y + tooltipHeight + 3, x + maxLineWidth + 3, y + tooltipHeight + 4, bgColor, bgColor, 300f);
			drawGradientRect(x - 3, y - 3, x + maxLineWidth + 3, y + tooltipHeight + 3, bgColor, bgColor, 300f);
			drawGradientRect(x - 4, y - 3, x - 3, y + tooltipHeight + 3, bgColor, bgColor, 300f);
			drawGradientRect(x + maxLineWidth + 3, y - 3, x + maxLineWidth + 4, y + tooltipHeight + 3, bgColor, bgColor, 300f);
			int fgColor1 = 1347420415;
			int fgColor2 = (fgColor1 & 16711422) >> 1 | fgColor1 & -16777216;
			drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + tooltipHeight + 3 - 1, fgColor1, fgColor2, 300f);
			drawGradientRect(x + maxLineWidth + 2, y - 3 + 1, x + maxLineWidth + 3, y + tooltipHeight + 3 - 1, fgColor1, fgColor2, 300f);
			drawGradientRect(x - 3, y - 3, x + maxLineWidth + 3, y - 3 + 1, fgColor1, fgColor1, 300f);
			drawGradientRect(x - 3, y + tooltipHeight + 2, x + maxLineWidth + 3, y + tooltipHeight + 3, fgColor2, fgColor2, 300f);

			for(String line : lines) {
				mc.fontRenderer.drawStringWithShadow(line, x, y, -1);
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
