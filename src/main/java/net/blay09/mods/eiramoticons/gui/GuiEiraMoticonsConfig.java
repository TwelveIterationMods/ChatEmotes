// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.blay09.mods.eiramoticons.EiraMoticons;
import net.blay09.mods.eiramoticons.EmoticonConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiEiraMoticonsConfig extends GuiConfig {

	public GuiEiraMoticonsConfig(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), EiraMoticons.MOD_ID, false, false, I18n.format("eiramoticons:gui.config.title"));
	}

	@SuppressWarnings("unchecked")
	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.addAll(new ConfigElement(EmoticonConfig.config.getCategory(EmoticonConfig.GENERAL)).getChildElements());
		list.addAll(new ConfigElement(EmoticonConfig.config.getCategory(EmoticonConfig.TWITCH)).getChildElements());
		list.addAll(new ConfigElement(EmoticonConfig.config.getCategory(EmoticonConfig.ADDONS)).getChildElements());
		list.addAll(new ConfigElement(EmoticonConfig.config.getCategory(EmoticonConfig.TWEAKS)).getChildElements());
		return list;
	}

}
