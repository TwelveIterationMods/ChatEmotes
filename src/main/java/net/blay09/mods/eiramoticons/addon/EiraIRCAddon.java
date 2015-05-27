// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon;

import net.blay09.mods.eirairc.api.event.ApplyEmoticons;
import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
public class EiraIRCAddon {

	public EiraIRCAddon() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	@Optional.Method(modid = "eirairc")
	public void applyEmoticons(ApplyEmoticons event) {
		event.component = EmoticonHandler.adjustChatComponent(event.component);
	}

}
