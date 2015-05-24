// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.eirairc.api.event.ApplyEmoticons;
import net.blay09.mods.eiramoticons.EmoticonHandler;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class EiraIRCAddon {

	public EiraIRCAddon() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void applyEmoticons(ApplyEmoticons event) {
		event.component = EmoticonHandler.adjustChatComponent(event.component);
	}

}
