// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class EmoticonConfig {

	public static boolean enableMCEmotes;

	public static boolean twitchGlobalEmotes;
	public static boolean twitchSubscriberEmotes;
	public static String twitchSubscriberRegex;
	public static boolean bttvEmotes;
	public static boolean twitchSmileys;
	public static int twitchSmileySet;

	public static boolean defaultEmotes;
	public static boolean enableEiraIRCEmotes;

	public static void load(File configFile) {
		Configuration config = new Configuration(configFile);

		final String GENERAL = "general";
		enableMCEmotes = config.getBoolean("enableMCEmotes", GENERAL, true, "Should emotes be enabled for Vanilla Minecraft chat?");

		final String TWITCH = "twitch";
		twitchGlobalEmotes = config.getBoolean("twitchGlobalEmotes", TWITCH, true, "Should the global Twitch emoticons be registered?");
		twitchSubscriberEmotes = config.getBoolean("twitchSubscriberEmotes", TWITCH, true, "Should Twitch subscriber emoticons be registered?");
		twitchSubscriberRegex = config.getString("twitchSubscriberRegex", TWITCH, "[a-z0-9][a-z0-9]+[A-Z].*", "A regex pattern to limit the Twitch subscriber emotes that are registered.");
		bttvEmotes = config.getBoolean("bttvEmotes", TWITCH, true, "Should Better TwitchTV emoticons be registered?");
		twitchSmileys = config.getBoolean("twitchSmileys", TWITCH, false, "Should Twitch :) smileys be registered?");
		twitchSmileySet = config.getInt("twitchSmileySet", TWITCH, 0, 0, 2, "The Twitch smiley style to use in chat: 0 for robot, 1 for Turbo, 2 for monkeys (NYI)");

		final String ADDONS = "addons";
		defaultEmotes = config.getBoolean("defaultEmotes", ADDONS, true, "Should the default EiraMoticons emoticons be registered?");
		enableEiraIRCEmotes = config.getBoolean("enableEiraIRCEmotes", ADDONS, true, "Should emotes be enabled for IRC chat, if EiraIRC is installed?");

		config.save();
	}

}
