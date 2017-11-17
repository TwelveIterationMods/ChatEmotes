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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nullable;
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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		if(EmoticonRegistry.isLoading) {
			sender.addChatMessage(new TextComponentTranslation("eiramoticons:command.still_reloading"));
			return;
		}

		if(args[0].equals("reload")) {
			EmoticonConfig.hardReload();
			EmoticonRegistry.reloadEmoticons();
		} else if(args[0].equals("help")) {
			ITextComponent linkComponent = new TextComponentTranslation("eiramoticons:command.help.clickHere");
			linkComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://blay09.net/?page_id=347"));
			linkComponent.getStyle().setColor(TextFormatting.GOLD);
			linkComponent.getStyle().setBold(true);
			linkComponent.getStyle().setUnderlined(true);
			sender.addChatMessage(new TextComponentTranslation("eiramoticons:command.help", linkComponent));
		} else if(args[0].equals("list")) {
			for (EmoticonGroup group : EmoticonRegistry.getGroups()) {
				sender.addChatMessage(group.listComponent);
			}
		} else if(args[0].equals("clearcache")) {
			TwitchEmotesAPI.clearCache();
			sender.addChatMessage(new TextComponentTranslation("eiramoticons:command.clearcache"));
		} else {
			throw new WrongUsageException(getCommandUsage(sender));
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		if(args.length == 0) {
			return getListOfStringsMatchingLastWord(args, "reload", "help", "list", "clearcache");
		}
		return null;
	}

}
