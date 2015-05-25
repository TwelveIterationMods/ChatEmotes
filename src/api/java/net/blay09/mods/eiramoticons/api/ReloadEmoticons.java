// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.api;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * This event is published on the MinecraftForge.EVENTBUS bus whenever EiraMoticons reloads it's emoticons.
 * It is also published once during startup.
 * Other mods can listen on this event to register their own emoticons.
 */
public class ReloadEmoticons extends Event {}
