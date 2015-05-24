package net.blay09.mods.eiramoticons;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		EiraMoticonsAPI.setupAPI(new InternalMethods());
		EmoticonConfig.load(event.getSuggestedConfigurationFile());
	}

	@Override
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

		if(EmoticonConfig.twitchSmileys) {
			new TwitchSmileyAddon(EmoticonConfig.twitchSmileySet);
		}

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

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		if(EmoticonConfig.enableEiraIRCEmotes) {
			event.buildSoftDependProxy("eirairc", "net.blay09.mods.eiramoticons.addon.EiraIRCAddon");
		}
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void clientChatReceived(ClientChatReceivedEvent event) {
		if(EmoticonConfig.enableMCEmotes) {
			event.message = EmoticonHandler.adjustChatComponent(event.message);
		}
	}
}
