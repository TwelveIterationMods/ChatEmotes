// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.api;

import java.awt.image.BufferedImage;

public interface IEmoticon {
	int getId();
	String getChatString();
	String getName();
	void setImage(BufferedImage image);
	void setTooltip(String[] tooltipLines);
	Object getIdentifier();
	void setIdentifier(Object identifier);
	IEmoticonLoader getLoader();
}
