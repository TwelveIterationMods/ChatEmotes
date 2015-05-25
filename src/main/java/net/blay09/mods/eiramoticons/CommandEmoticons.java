package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandEmoticons extends CommandBase {

	@Override
	public String getCommandName() {
		return "emoticons";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/emoticons reload";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length != 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}
		if(args[0].equals("reload")) {
			EmoticonRegistry.reloadEmoticons();
		} else {
			throw new WrongUsageException(getCommandUsage(sender));
		}
	}

}
