package net.blay09.mods.eiramoticons.emoticon;

import net.minecraft.util.IChatComponent;

public class EmoticonGroup {

	public final String groupName;
	public final IChatComponent listComponent;

	public EmoticonGroup(String groupName, IChatComponent listComponent) {
		this.groupName = groupName;
		this.listComponent = listComponent;
	}

}
