package net.blay09.mods.eiramoticons;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;

public class EmoticonConfig {

	public static final String GENERAL = "general";
	public static final String PACKS = "packs";
	public static final String TWEAKS = "tweaks";

	public static Configuration config;
	public static File configFile;

	public static boolean enableMCEmotes;
	public static boolean enableIRCEmotes;
	public static String twitchSubscriberRegex;
	public static String[] bttvEmoteChannels;
	public static String[] ffzEmoteChannels;
	public static String[] subEmoteChannels;

	public static boolean defaultPack;
	public static boolean twitchGlobalEmotes;
	public static boolean twitchPrimeEmotes;
	public static boolean twitchSubscriberEmotes;
	public static boolean bttvEmotes;
	public static boolean bttvChannelEmotes;

	public static boolean twitchSmileys;
	public static boolean betterKappas;
	public static boolean ffzEmotes;
	public static boolean ffzChannelEmotes;

	public static void loadFromFile(File configFile) {
		EmoticonConfig.configFile = configFile;
		config = new Configuration(configFile);
		loadFromConfig();
	}

	private static void loadFromConfig() {
		enableMCEmotes = config.getBoolean("enableMCEmotes", GENERAL, true, I18n.format("eiramoticons:config.enableMCEmotes.tooltip"), "eiramoticons:config.enableMCEmotes");
		twitchSubscriberRegex = config.getString("twitchSubscriberRegex", GENERAL, "[a-z0-9][a-z0-9]+[A-Z0-9].*", I18n.format("eiramoticons:config.twitchSubscriberRegex.tooltip"), "eiramoticons:config.twitchSubscriberRegex");
		bttvEmoteChannels = config.getStringList("bttvEmoteChannels", GENERAL, new String[0], I18n.format("eiramoticons:config.bttvEmoteChannels.tooltip"), null, "eiramoticons:config.bttvEmoteChannels");
		subEmoteChannels = config.getStringList("subEmoteChannels", GENERAL, new String[0], I18n.format("eiramoticons:config.twitchSubscriberChannels.tooltip"), null, "eiramoticons:config.twitchSubscriberChannels");
		ffzEmoteChannels = config.getStringList("ffzEmoteChannels", GENERAL, new String[] { "tehbasshunter" }, I18n.format("eiramoticons:config.ffzEmoteChannels.tooltip"), null, "eiramoticons:config.ffzEmoteChannels");
		enableIRCEmotes = config.getBoolean("enableIRCEmotes", GENERAL, true, I18n.format("eiramoticons:config.enableIRCEmotes.tooltip"), "eiramoticons:config.enableIRCEmotes");

		twitchGlobalEmotes = config.getBoolean("twitchGlobalEmotes", PACKS, true, I18n.format("eiramoticons:config.twitchGlobalEmotes.tooltip"), "eiramoticons:config.twitchGlobalEmotes");
		twitchPrimeEmotes = config.getBoolean("twitchPrimeEmotes", PACKS, true, I18n.format("eiramoticons:config.twitchPrimeEmotes.tooltip"), "eiramoticons:config.twitchPrimeEmotes");
		twitchSubscriberEmotes = config.getBoolean("twitchSubscriberEmotes", PACKS, true, I18n.format("eiramoticons:config.twitchSubscriberEmotes.tooltip"), "eiramoticons:config.twitchSubscriberEmotes");
		bttvEmotes = config.getBoolean("bttvEmotes", PACKS, false, I18n.format("eiramoticons:config.bttvEmotes.tooltip"), "eiramoticons:config.bttvEmotes");
		bttvChannelEmotes = config.getBoolean("bttvChannelEmotes", PACKS, true, I18n.format("eiramoticons:config.bttvChannelEmotes.tooltip"), "eiramoticons:config.bttvChannelEmotes");
		twitchSmileys = config.getBoolean("twitchSmileys", PACKS, false, I18n.format("eiramoticons:config.twitchSmileys.tooltip"), "eiramoticons:config.twitchSmileys");
		defaultPack = config.getBoolean("defaultPack", PACKS, true, I18n.format("eiramoticons:config.defaultPack.tooltip"), "eiramoticons:config.defaultPack");
		ffzEmotes = config.getBoolean("ffzEmotes", PACKS, false, I18n.format("eiramoticons:config.ffzEmotes.tooltip"), "eiramoticons:config.ffzEmotes");
		ffzChannelEmotes = config.getBoolean("ffzChannelEmotes", PACKS, false, I18n.format("eiramoticons:config.ffzChannelEmotes.tooltip"), "eiramoticons:config.ffzChannelEmotes");

		betterKappas = config.getBoolean("betterKappas", TWEAKS, false, I18n.format("eiramoticons:config.betterKappas.tooltip"), "eiramoticons:config.betterKappas");

		config.save();
	}

	public static void lightReload() {
		loadFromConfig();
	}

	public static void hardReload() {
		loadFromFile(configFile);
	}

	public static void subscribe(String channel) {
		subEmoteChannels = Arrays.copyOf(subEmoteChannels, subEmoteChannels.length + 1);
		subEmoteChannels[subEmoteChannels.length - 1] = channel;
		
		config.get(GENERAL, "subEmoteChannels", new String[0]).set(subEmoteChannels);
		config.save();
	}
}
