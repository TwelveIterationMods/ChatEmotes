// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.eirairc.api.event.FormatMessage;
import net.blay09.mods.eirairc.api.event.IRCChannelJoinedEvent;
import net.blay09.mods.eiramoticons.EmoticonConfig;
import net.blay09.mods.eiramoticons.addon.pack.BTTVChannelPack;
import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class EiraIRCAddon {

	public EiraIRCAddon() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	@Optional.Method(modid = "eirairc")
	public void applyEmoticons(FormatMessage event) {
		event.component = EmoticonHandler.adjustChatComponent(event.component);
	}

	@SubscribeEvent
	@Optional.Method(modid = "eirairc")
	public void onChannelJoined(IRCChannelJoinedEvent event) {
		if(event.connection.isTwitch() && EmoticonConfig.bttvChannelEmotes) {
			new BTTVChannelPack(event.channel.getName().substring(1));
		}
	}

}
