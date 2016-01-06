// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.emoticon.EmoticonGroup;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.*;

import java.util.List;

public class CommandEmoticons extends CommandBase {

	@Override
	public String getCommandName() {
		return "emoticons";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/emoticons reload|help|list|clearcache";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}
		switch (args[0]) {
			case "reload":
				EmoticonConfig.hardReload();
				EmoticonRegistry.reloadEmoticons();
				break;
			case "help":
				IChatComponent linkComponent = new ChatComponentTranslation("eiramoticons:command.help.clickHere");
				linkComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://blay09.net/?page_id=347"));
				linkComponent.getChatStyle().setColor(EnumChatFormatting.GOLD);
				linkComponent.getChatStyle().setBold(true);
				linkComponent.getChatStyle().setUnderlined(true);
				sender.addChatMessage(new ChatComponentTranslation("eiramoticons:command.help", linkComponent));
				break;
			case "list":
				for (EmoticonGroup group : EmoticonRegistry.getGroups()) {
					sender.addChatMessage(group.listComponent);
				}
				break;
			case "clearcache":
				TwitchEmotesAPI.clearCache();
				sender.addChatMessage(new ChatComponentTranslation("eiramoticons:command.clearcache"));
				break;
			default:
				throw new WrongUsageException(getCommandUsage(sender));
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length == 0) {
			return getListOfStringsMatchingLastWord(args, "reload", "help", "list", "clearcache");
		}
		return null;
	}

}
