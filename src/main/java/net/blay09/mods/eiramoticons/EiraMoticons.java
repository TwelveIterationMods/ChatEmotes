// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = EiraMoticons.MOD_ID, acceptableRemoteVersions="*", guiFactory = "net.blay09.mods.eiramoticons.gui.EiraMoticonsGuiFactory")
public class EiraMoticons {
    public static final String MOD_ID = "eiramoticons";

	@Mod.Instance(MOD_ID)
	public static EiraMoticons instance;

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
