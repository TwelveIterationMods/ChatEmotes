package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.emoticon.EmoticonGroup;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.addon.pack.TwitchSubscriberPack;
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
	public String getName() {
		return "emoticons";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/emoticons reload | list | config | clearcache | subscribe <channel>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0 || (args.length > 1 && args[0] != "subscribe") || (args[0] == "subscribe" && args.length != 2)) {
			throw new WrongUsageException(getUsage(sender));
		}
		if(EmoticonRegistry.isLoading) {
			sender.sendMessage(new TextComponentTranslation("eiramoticons:command.still_reloading"));
			return;
		}
		switch (args[0]) {
			case "reload":
				EmoticonConfig.hardReload();
				EmoticonRegistry.reloadEmoticons();
				break;
			case "list":
				for (EmoticonGroup group : EmoticonRegistry.getGroups()) {
					sender.sendMessage(group.listComponent);
				}
				break;
			case "clearcache":
				TwitchEmotesAPI.clearCache();
				sender.sendMessage(new TextComponentTranslation("eiramoticons:command.clearcache"));
				break;
			case "subscribe":
				String channel = args[1];
				EmoticonConfig.subscribe(channel);
				new TwitchSubscriberPack(channel);
			default:
				throw new WrongUsageException(getUsage(sender));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		if(args.length == 0) {
			return getListOfStringsMatchingLastWord(args, "reload", "help", "list", "clearcache", "subscribe");
		}
		return super.getTabCompletions(server, sender, args, pos);
	}

}
