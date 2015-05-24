// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.eirairc.*;
import net.blay09.mods.eiramoticons.addon.*;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
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

	@SidedProxy(serverSide = "net.blay09.mods.eiramoticons.CommonProxy", clientSide = "net.blay09.mods.eiramoticons.ClientProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
