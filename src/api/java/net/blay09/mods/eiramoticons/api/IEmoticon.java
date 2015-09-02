// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.api;

import java.awt.image.BufferedImage;

/**
 * An interface representing a registered emoticon.
 */
public interface IEmoticon {
	/**
	 * @return the temporary, internal ID of this emoticon
	 */
	int getId();

	/**
	 * @return the string that can be used in a chat message to display this emoticon
	 */
	String getChatString();

	/**
	 * @return the code to use this emoticon in chat
	 */
	String getName();

	/**
	 * Sets the BufferedImage for this emote and will trigger a texture upload on the next frame it's rendered.
	 * It also sets a default scaling based on the image dimensions.
	 * @param image
	 */
	void setImage(BufferedImage image);

	/**
	 * Sets the BufferedImages for this emote, will create a spritesheet for it and will trigger a texture upload on the next frame it's rendered.
	 * It also sets a default scaling based on the image dimensions.
	 * @param images
	 * @param frameTime
	 * @param offsetX
	 * @param offsetY
	 */
	void setImages(BufferedImage[] images, int[] frameTime, int[] offsetX, int[] offsetY);

	/**
	 * Sets the scale factor for this emote. By default, EiraMoticons will scale emotes down to a maximum  size of 16x14.
	 * @param scaleX
	 * @param scaleY
	 */
	void setScale(float scaleX, float scaleY);

	/**
	 * @return The x scaling for this emoticon.  By default, EiraMoticons will scale emotes down to a maximum  size of 16x14.
	 */
	float getScaleX();

	/**
	 * @return The y scaling for this emoticon.  By default, EiraMoticons will scale emotes down to a maximum  size of 16x14.
	 */
	float getScaleY();

	/**
	 * Use the default tooltip for this emoticon, specifying a group as second line.
	 * If you want to specify a fully customized tooltip, use setCustomTooltip instead.
	 * @param emoticonGroup the group for this emoticon, will be displayed in the second line
	 */
	void setTooltip(String emoticonGroup);

	/**
	 * Sets the tooltip that appears when hovering over this emote
	 * @param tooltipLines each entry in this array represents a line in this tooltip
	 */
	void setCustomTooltip(String[] tooltipLines);

	/**
	 * @return optional load data to identify the emoticon in the IEmoticonLoader without requiring the name
	 */
	Object getLoadData();

	/**
	 * Set optional load data to identify the emoticon in the IEmoticonLoader without requiring the name
	 * @param loadData can be anything your IEmotionLoader can deal with
	 */
	void setLoadData(Object loadData);

	/**
	 * @return the loader that supplies the BufferedImage for this emoticon
	 */
	IEmoticonLoader getLoader();

	/**
	 * @return true if this emoticon can not be used with an emoticon code, only by a direct formatting code
	 */
	boolean isManualOnly();

	/**
	 * Set this emoticon to not be parsed from it's emoticon code and instead only appear when the formatting code was used directly
	 * @param manualOnly true if this emoticon can not be used by players
	 */
	void setManualOnly(boolean manualOnly);

	/**
	 * @return true if this emoticon is animated
	 */
	boolean isAnimated();

	/**
	 *
	 * @param cumulativeRendering
	 */
	void setCumulativeRendering(boolean cumulativeRendering);

	/**
	 *
	 * @return
	 */
	boolean isCumulativeRendering();
}
