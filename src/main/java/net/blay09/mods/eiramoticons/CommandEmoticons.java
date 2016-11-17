package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.emoticon.EmoticonGroup;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;

public class CommandEmoticons extends CommandBase {

	@Override
	public String getCommandName() {
		return "emoticons";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/emoticons reload|list|config|clearcache";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}
		switch (args[0]) {
			case "reload":
				EmoticonConfig.hardReload();
				EmoticonRegistry.reloadEmoticons();
				break;
			case "list":
				for (EmoticonGroup group : EmoticonRegistry.getGroups()) {
					sender.addChatMessage(group.listComponent);
				}
				break;
			case "clearcache":
				TwitchEmotesAPI.clearCache();
				sender.addChatMessage(new TextComponentTranslation("eiramoticons:command.clearcache"));
				break;
			default:
				throw new WrongUsageException(getCommandUsage(sender));
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		if(args.length == 0) {
			return getListOfStringsMatchingLastWord(args, "reload", "help", "list", "clearcache");
		}
		return super.getTabCompletionOptions(server, sender, args, pos);
	}

}
