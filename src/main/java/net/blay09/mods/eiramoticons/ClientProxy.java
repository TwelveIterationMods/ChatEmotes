package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.addon.*;
import net.blay09.mods.eiramoticons.addon.pack.*;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.EmoteLoaderException;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String FONT_TEXTURE = "textures/font/ascii.png";
	public static int MAX_TEXTURE_SIZE;
	public static EmoticonRenderer renderer;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		EiraMoticonsAPI.setupAPI(new InternalMethods());
		EmoticonConfig.loadFromFile(event.getSuggestedConfigurationFile());
	}

	@Override
	public void init(FMLInitializationEvent event) {
		if (EiraMoticons.hasSuperiorModInstalled) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		FontRendererExt fontRenderer = new FontRendererExt(mc.gameSettings, new ResourceLocation(FONT_TEXTURE), mc.renderEngine, false);
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

		MAX_TEXTURE_SIZE = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void clientChatReceived(ClientChatReceivedEvent event) {
		if (EmoticonConfig.enableMCEmotes) {
			event.setMessage(EmoticonHandler.adjustChatComponent(event.getMessage()));
		}
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void reloadEmoticons(ReloadEmoticons event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				EmoticonRegistry.isLoading = true;

				// Twitch Emotes
				if (EmoticonConfig.twitchSmileys) {
					try {
						new TwitchSmileyPack(EmoticonConfig.twitchSmileySet);
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load Twitch smiley emotes: {}", e);
					}
				}

				if (EmoticonConfig.twitchGlobalEmotes) {
					try {
						new TwitchGlobalPack();
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load Twitch global emotes: {}", e);
					}
				}

				if (EmoticonConfig.twitchPrimeEmotes) {
					try {
						new TwitchPrimePack();
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load Twitch prime emotes: {}", e);
					}
				}

				if (EmoticonConfig.twitchSubscriberEmotes) {
					try {
						new TwitchSubscriberPack(EmoticonConfig.twitchSubscriberRegex);
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load Twitch subscriber emotes: {}", e);
					}
				}

				if (EmoticonConfig.bttvEmotes) {
					try {
						new BTTVPack();
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load BTTV emotes: {}", e);
					}
				}

				if (EmoticonConfig.bttvChannelEmotes) {
					try {
						BTTVChannelPack.createGroup();
						for (String channel : EmoticonConfig.bttvEmoteChannels) {
							new BTTVChannelPack(channel);
						}
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load BTTV channel emotes: {}", e);
					}
				}

				if (EmoticonConfig.ffzEmotes) {
					try {
						new FFZPack();
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load FrankerFaceZ emotes: {}", e);
					}
				}

				if (EmoticonConfig.ffzChannelEmotes) {
					try {
						FFZChannelPack.createGroup();
						for (String channel : EmoticonConfig.ffzEmoteChannels) {
							new FFZChannelPack(channel);
						}
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load FrankerFaceZ channel emotes: {}", e);
					}
				}

				if (EmoticonConfig.defaultPack) {
					try {
						new IncludedPack("default", new String[]{"eiraRage", "eiraLewd", "eiraScared", "eiraCri", "eiraMeow", "eiraYawn", "eiraFufu", "eiraPraise", "eiraArr", "eiraCute"});
					} catch (EmoteLoaderException e) {
						LOGGER.error("Failed to load default emotes: {}", e.getMessage());
					}
				}

				// Custom Emotes
				try {
					new FileAddon();
				} catch (EmoteLoaderException e) {
					LOGGER.error("Failed to load file emotes: {}", e.getMessage());
				}

				// Tweaks
				if (EmoticonConfig.betterKappas && EmoticonConfig.twitchPrimeEmotes) {
					IEmoticon kappaHD = EmoticonRegistry.fromName("KappaHD");
					if (kappaHD != null) {
						IEmoticon kappa = EmoticonRegistry.registerEmoticon("Kappa", kappaHD.getLoader());
						kappa.setLoadData(kappaHD.getLoadData());
						kappa.setTooltip(I18n.format("eiramoticons:group.betterkappas"));
					}
				}

				EmoticonRegistry.isLoading = false;
			}
		}, ("Emote Loader")).start();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(EiraMoticons.MOD_ID)) {
			EmoticonConfig.lightReload();
			EmoticonRegistry.reloadEmoticons();
		}
	}
}
