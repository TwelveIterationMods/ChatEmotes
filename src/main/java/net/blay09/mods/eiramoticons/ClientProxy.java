package net.blay09.mods.eiramoticons;

import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.eiramoticons.addon.*;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.ReloadEmoticons;
import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.render.EmoticonRenderer;
import net.blay09.mods.eiramoticons.render.FontRendererExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
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

		ClientCommandHandler.instance.registerCommand(new CommandEmoticons());

		EmoticonRenderer renderer = new EmoticonRenderer(mc);
		fontRenderer.setEmoticonBuffer(renderer.getBuffer());
		MinecraftForge.EVENT_BUS.register(renderer);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.post(new ReloadEmoticons());

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

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void reloadEmoticons(ReloadEmoticons event) {
		// Twitch Emotes
		if(EmoticonConfig.twitchSmileys) {
			new TwitchSmileyAddon(EmoticonConfig.twitchSmileySet);
		}
		if(EmoticonConfig.twitchGlobalEmotes) {
			new TwitchGlobalAddon();
		}
		if(EmoticonConfig.twitchTurboEmotes) {
			new TwitchTurboAddon();
		}
		if(EmoticonConfig.twitchSubscriberEmotes) {
			new TwitchSubscriberAddon(EmoticonConfig.twitchSubscriberRegex);
		}
		if(EmoticonConfig.bttvEmotes) {
			new BTTVAddon();
		}

		// Custom Emotes
		new FileAddon();

		// Tweaks
		if(EmoticonConfig.betterKappas && EmoticonConfig.twitchTurboEmotes) {
			IEmoticon kappaHD = EmoticonRegistry.fromName("KappaHD");
			IEmoticon kappa = EmoticonRegistry.registerEmoticon("Kappa", kappaHD.getLoader());
			kappa.setIdentifier(kappaHD.getIdentifier());
			kappa.setTooltip(new String[]{"\u00a7eEmote:\u00a7r Kappa", "\u00a7eBetter Kappas"});
		}
	}
}
