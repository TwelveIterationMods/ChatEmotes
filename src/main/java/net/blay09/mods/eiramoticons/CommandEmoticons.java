package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.addon.TwitchEmotesAPI;
import net.blay09.mods.eiramoticons.emoticon.EmoticonGroup;
import net.blay09.mods.eiramoticons.emoticon.EmoticonRegistry;
import net.blay09.mods.eiramoticons.addon.pack.TwitchSubscriberPack;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandEmoticons implements ICommand {
	@Override
	public String getName() {
		return "emoticons";
	}

	@Override
	public String getUsage(final ICommandSender sender) {
		return "/emoticons reload | list | config | clearcache | subscribe <channel>";
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
		if (args.length == 0) {
			throw new WrongUsageException(getUsage(sender));
		}
		switch (args[0]) {
			case "reload":
			case "load":
			case "list":
			case "clearcache":
			case "cleanup":
			case "clear":
				if (args.length != 1) {
					throw new WrongUsageException(getUsage(sender));
				}
				break;
			case "subscribe":
			case "sub":
				if (args.length != 2){
					throw new WrongUsageException(getUsage(sender));
				}
				break;
			default:
				throw new WrongUsageException(getUsage(sender));
		}
		
		if(EmoticonRegistry.isLoading) {
			sender.sendMessage(new TextComponentTranslation("eiramoticons:command.still_reloading"));
			return;
		}

		switch (args[0]) {
			case "reload":
			case "load":
				EmoticonConfig.hardReload();
				EmoticonRegistry.reloadEmoticons();
				sender.sendMessage(new TextComponentTranslation("eiramoticons:command.reload"));
				break;
			case "list":
				for (final EmoticonGroup group : EmoticonRegistry.getGroups()) {
					sender.sendMessage(group.listComponent);
				}
				break;
			case "clearcache":
			case "cleanup":
			case "clear":
				TwitchEmotesAPI.clearCache();
				sender.sendMessage(new TextComponentTranslation("eiramoticons:command.clearcache"));
				break;
			case "subscribe":
			case "sub":
				final String channel = args[1];			
				EmoticonConfig.subscribe(channel);
				try {
					new TwitchSubscriberPack(channel);
					final String channelName = TwitchEmotesAPI.getChannelName(channel);
					if (channelName != null){
						sender.sendMessage(new TextComponentString(I18n.format("eiramoticons:command.subscribe", channelName)));
					} else {
						sender.sendMessage(new TextComponentString(I18n.format("eiramoticons:command.channelnotfound", channel)));
					}
				} catch(final Exception e){
					sender.sendMessage(new TextComponentString(I18n.format("eiramoticons:command.channelnotfound", channel)));
				}
				break;
			default:
				throw new WrongUsageException(getUsage(sender));
		}
	}

	@Override
	public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
		
		final List<String> auto = new ArrayList<String>();
		if (args.length <= 1){
			auto.add("reload");
			auto.add("load");
			auto.add("help");
			auto.add("list");
			auto.add("clearcache");
			auto.add("clear");
			auto.add("cleanup");
			auto.add("sub");
			auto.add("subscribe");
		}
		return auto;
	}

	@Override
	public int compareTo(final ICommand o) {
		return 0;
	}

	@Override
	public List<String> getAliases() {
		final List<String> aliases = new ArrayList<String>();
		aliases.add("emoticons");
		return aliases;
	}

	@Override
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return true;
	}

	@Override
	public boolean isUsernameIndex(final String[] args, final int index) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
