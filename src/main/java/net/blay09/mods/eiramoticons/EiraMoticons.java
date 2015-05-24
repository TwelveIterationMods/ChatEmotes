// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.eiramoticons.addon.BTTVAddon;
import net.blay09.mods.eiramoticons.addon.FileAddon;
import net.blay09.mods.eiramoticons.addon.TwitchGlobalAddon;
import net.blay09.mods.eiramoticons.addon.TwitchSubscriberAddon;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.render.EmoticonRenderer;
import net.blay09.mods.eiramoticons.render.FontRendererExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = EiraMoticons.MOD_ID, acceptableRemoteVersions="*")
public class EiraMoticons {
    public static final String MOD_ID = "eiramoticons";

	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void preInit(FMLPreInitializationEvent event) {
		EiraMoticonsAPI.setupAPI(new InternalMethods());
		EmoticonConfig.load(event.getSuggestedConfigurationFile());
	}

	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void init(FMLInitializationEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		FontRendererExt fontRenderer = new FontRendererExt(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		fontRenderer.setUnicodeFlag(mc.func_152349_b());
		fontRenderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
		((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(fontRenderer);
		mc.fontRenderer = fontRenderer;

		EmoticonRenderer renderer = new EmoticonRenderer(mc);
		fontRenderer.setEmoticonBuffer(renderer.getBuffer());
		MinecraftForge.EVENT_BUS.register(renderer);

		MinecraftForge.EVENT_BUS.register(this);

		if(EmoticonConfig.twitchGlobalEmotes) {
			new TwitchGlobalAddon();
		}

		if(EmoticonConfig.twitchSubscriberEmotes) {
			new TwitchSubscriberAddon(EmoticonConfig.twitchSubscriberRegex);
		}

		if(EmoticonConfig.bttvEmotes) {
			new BTTVAddon();
		}

		new FileAddon();
	}

	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void postInit(FMLPostInitializationEvent event) {
		event.buildSoftDependProxy("eirairc", "net.blay09.mods.eiramoticons.addon.EiraIRCAddon");
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void clientChatReceived(ClientChatReceivedEvent event) {
		event.message = EmoticonHandler.adjustChatComponent(event.message);
	}



}
