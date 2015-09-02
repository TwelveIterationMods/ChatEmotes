// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.addon.*;
import net.blay09.mods.eiramoticons.addon.pack.*;
import net.blay09.mods.eiramoticons.api.ChatContainer;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.ReloadEmoticons;
import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.render.EmoticonRenderer;
import net.blay09.mods.eiramoticons.render.FontRendererExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

	public static final ResourceLocation FONT_TEXTURE = new ResourceLocation("textures/font/ascii.png");
	public static int MAX_TEXTURE_SIZE;
	public static EmoticonRenderer renderer;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		EiraMoticonsAPI.setupAPI(new InternalMethods());
		EmoticonConfig.loadFromFile(event.getSuggestedConfigurationFile());

		MAX_TEXTURE_SIZE = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		FontRendererExt fontRenderer = new FontRendererExt(mc.gameSettings, FONT_TEXTURE, mc.renderEngine, false);
		fontRenderer.setUnicodeFlag(mc.isUnicode());
		fontRenderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
		((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(fontRenderer);
		mc.fontRendererObj = fontRenderer;

		ClientCommandHandler.instance.registerCommand(new CommandEmoticons());

		renderer = new EmoticonRenderer(mc);
		fontRenderer.setEmoticonBuffer(renderer.getBuffer());
		MinecraftForge.EVENT_BUS.register(renderer);

		MinecraftForge.EVENT_BUS.register(this);

		TwitchEmotesAPI.initialize(mc.mcDataDir);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.post(new ReloadEmoticons());

		if(EmoticonConfig.enableIRCEmotes) {
			event.buildSoftDependProxy("eirairc", "net.blay09.mods.eiramoticons.addon.EiraIRCAddon");
		}

		ChatContainer customContainer = (ChatContainer) event.buildSoftDependProxy("TabbyChat2", "net.blay09.mods.eiramoticons.addon.TabbyChat2Container");
		if(customContainer != null) {
			renderer.setChatContainer(customContainer);
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
			new TwitchSmileyPack(EmoticonConfig.twitchSmileySet);
		}
		if(EmoticonConfig.twitchGlobalEmotes) {
			new TwitchGlobalPack();
		}
		if(EmoticonConfig.twitchTurboEmotes) {
			new TwitchTurboPack();
		}
		if(EmoticonConfig.twitchSubscriberEmotes) {
			new TwitchSubscriberPack(EmoticonConfig.twitchSubscriberRegex);
		}
		if(EmoticonConfig.bttvEmotes) {
			new BTTVPack();
		}

		if(EmoticonConfig.defaultPack) {
			new IncludedPack("default", new String[] {"eiraRage", "eiraLewd", "eiraScared", "eiraCri", "eiraMeow", "eiraYawn", "eiraFufu", "eiraPraise", "eiraArr", "eiraCute"});
		}

		if(EmoticonConfig.animuPack) {
			new IncludedPack("animu", new String[] {"aniRage", "aniCri", "aniLewd", "aniYui", "aniMeow", "aniNyan", "aniPraise", "aniScared", "aniWoah", "aniLove"});
		}

		// Custom Emotes
		new FileAddon();

		// Tweaks
		if(EmoticonConfig.betterKappas && EmoticonConfig.twitchTurboEmotes) {
			IEmoticon kappaHD = EmoticonRegistry.fromName("KappaHD");
			IEmoticon kappa = EmoticonRegistry.registerEmoticon("Kappa", kappaHD.getLoader());
			kappa.setLoadData(kappaHD.getLoadData());
			kappa.setTooltip(I18n.format("eiramoticons:group.betterkappas"));
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.modID.equals(EiraMoticons.MOD_ID)) {
			EmoticonConfig.lightReload();
			EmoticonRegistry.reloadEmoticons();
		}
	}
}
