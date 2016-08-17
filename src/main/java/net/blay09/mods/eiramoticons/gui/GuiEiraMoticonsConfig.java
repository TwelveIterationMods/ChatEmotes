package net.blay09.mods.eiramoticons.gui;

import net.blay09.mods.eiramoticons.EiraMoticons;
import net.blay09.mods.eiramoticons.EmoticonConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiEiraMoticonsConfig extends GuiConfig {

	public GuiEiraMoticonsConfig(GuiScreen parentScreen) {
		super(parentScreen, getCategories(), EiraMoticons.MOD_ID, false, false, I18n.format("eiramoticons:gui.config.title"));
	}

	@SuppressWarnings("unchecked")
	private static List<IConfigElement> getCategories() {
		List<IConfigElement> list = new ArrayList<>();
		list.add(new DummyConfigElement.DummyCategoryElement(I18n.format("eiramoticons:config.category.general"), "eiramoticons:config.category.general", new ConfigElement(EmoticonConfig.config.getCategory(EmoticonConfig.GENERAL)).getChildElements()));
		list.add(new DummyConfigElement.DummyCategoryElement(I18n.format("eiramoticons:config.category.packs"), "eiramoticons:config.category.packs", new ConfigElement(EmoticonConfig.config.getCategory(EmoticonConfig.PACKS)).getChildElements()));
		list.add(new DummyConfigElement.DummyCategoryElement(I18n.format("eiramoticons:config.category.tweaks"), "eiramoticons:config.category.tweaks", new ConfigElement(EmoticonConfig.config.getCategory(EmoticonConfig.TWEAKS)).getChildElements()));
		return list;
	}

}
