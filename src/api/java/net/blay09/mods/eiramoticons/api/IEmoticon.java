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
	 * Sets the BufferedImage for this emote and will trigger a texture upload on the next frame it's rendered
	 * @param image
	 */
	void setImage(BufferedImage image);

	/**
	 * Sets the tooltip that appears when hovering over this emote
	 * @param tooltipLines each entry in this array represents a line in this tooltip
	 */
	void setTooltip(String[] tooltipLines);

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
}
