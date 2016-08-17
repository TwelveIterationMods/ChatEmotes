package net.blay09.mods.eiramoticons;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = EiraMoticons.MOD_ID, acceptedMinecraftVersions = "[1.10]", acceptableRemoteVersions="*", guiFactory = "net.blay09.mods.eiramoticons.gui.EiraMoticonsGuiFactory", clientSideOnly = true)
public class EiraMoticons {

    public static final String MOD_ID = "eiramoticons";
	public static boolean hasSuperiorModInstalled;

	@Mod.Instance(MOD_ID)
	public static EiraMoticons instance;

	@SidedProxy(serverSide = "net.blay09.mods.eiramoticons.CommonProxy", clientSide = "net.blay09.mods.eiramoticons.ClientProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	@SuppressWarnings("unused")
	public void preInit(FMLPreInitializationEvent event) {
		hasSuperiorModInstalled = Loader.isModLoaded("betterminecraftchat");

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
