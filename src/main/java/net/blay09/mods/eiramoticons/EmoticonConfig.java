// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class EmoticonConfig {

	public static final String GENERAL = "general";
	public static final String TWITCH = "twitch";
	public static final String ADDONS = "addons";
	public static final String TWEAKS = "tweaks";

	public static Configuration config;
	public static File configFile;

	public static boolean enableMCEmotes;

	public static boolean twitchGlobalEmotes;
	public static boolean twitchTurboEmotes;
	public static boolean twitchSubscriberEmotes;
	public static String twitchSubscriberRegex;
	public static boolean bttvEmotes;
	public static boolean twitchSmileys;
	public static int twitchSmileySet;

	public static boolean defaultEmotes;
	public static boolean enableEiraIRCEmotes;

	public static boolean betterKappas;

	public static void loadFromFile(File configFile) {
		EmoticonConfig.configFile = configFile;
		config = new Configuration(configFile);
		loadFromConfig();
	}

	private static void loadFromConfig() {
		enableMCEmotes = config.getBoolean("enableMCEmotes", GENERAL, true, "Should emotes be enabled for Vanilla Minecraft chat?");

		twitchGlobalEmotes = config.getBoolean("twitchGlobalEmotes", TWITCH, true, "Should the global Twitch emoticons be registered?");
		twitchTurboEmotes = config.getBoolean("twitchTurboEmotes", TWITCH, true, "Should the Twitch Turbo emoticons be registered?");
		twitchSubscriberEmotes = config.getBoolean("twitchSubscriberEmotes", TWITCH, true, "Should Twitch subscriber emoticons be registered?");
		twitchSubscriberRegex = config.getString("twitchSubscriberRegex", TWITCH, "[a-z0-9][a-z0-9]+[A-Z].*", "A regex pattern to limit the Twitch subscriber emotes that are registered.");
		bttvEmotes = config.getBoolean("bttvEmotes", TWITCH, true, "Should Better TwitchTV emoticons be registered?");
		twitchSmileys = config.getBoolean("twitchSmileys", TWITCH, false, "Should Twitch :) smileys be registered?");
		twitchSmileySet = config.getInt("twitchSmileySet", TWITCH, 0, 0, 2, "The Twitch smiley style to use in chat: 0 for robot, 1 for Turbo, 2 for monkeys (NYI)");

		defaultEmotes = config.getBoolean("defaultEmotes", ADDONS, true, "Should the default EiraMoticons emoticons be registered?");
		enableEiraIRCEmotes = config.getBoolean("enableEiraIRCEmotes", ADDONS, true, "Should emotes be enabled for IRC chat, if EiraIRC is installed?");

		betterKappas = config.getBoolean("betterKappas", TWEAKS, false, "Should all Kappas be turned into the more beautiful KappaHDs? (requires twitchTurboEmotes to be enabled)");

		config.save();
	}

	public static void lightReload() {
		loadFromConfig();
	}

	public static void hardReload() {
		loadFromFile(configFile);
	}
}
