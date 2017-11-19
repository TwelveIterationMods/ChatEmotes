// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.emoticon.EmoticonGroup;
import net.blay09.mods.eiramoticons.emoticon.EmoticonHandler;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.gui.GuiEiraMoticonsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class CommandEmoticons extends CommandBase {

	@Override
	public String getCommandName() {
		return "emoticons";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/emoticons reload|help|list|config|clearcache";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length != 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		if (EmoticonRegistry.isLoading) {
			sender.addChatMessage(new ChatComponentTranslation("eiramoticons:command.still_reloading"));
			return;
		}

		if (args[0].equals("reload")) {
			EmoticonConfig.hardReload();
			EmoticonRegistry.reloadEmoticons();
		} else if (args[0].equals("help")) {
			IChatComponent linkComponent = new ChatComponentTranslation("eiramoticons:command.help.clickHere");
			linkComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://blay09.net/?page_id=347"));
			linkComponent.getChatStyle().setColor(EnumChatFormatting.GOLD);
			linkComponent.getChatStyle().setBold(true);
			linkComponent.getChatStyle().setUnderlined(true);
			sender.addChatMessage(new ChatComponentTranslation("eiramoticons:command.help", linkComponent));
		} else if (args[0].equals("list")) {
			for (EmoticonGroup group : EmoticonRegistry.getGroups()) {
				sender.addChatMessage(group.listComponent);
			}
		} else if (args[0].equals("clearcache")) {
			TwitchEmotesAPI.clearCache();
			sender.addChatMessage(new ChatComponentTranslation("eiramoticons:command.clearcache"));
		} else {
			throw new WrongUsageException(getCommandUsage(sender));
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			List<String> list = new ArrayList<String>();
			list.add("reload");
			list.add("help");
			list.add("list");
			return list;
		}
		return null;
	}
}
