package net.blay09.mods.eiramoticons.emoticon;

import net.minecraft.util.text.ITextComponent;

public class EmoticonGroup {

	public final String groupName;
	public final ITextComponent listComponent;

	public EmoticonGroup(String groupName, ITextComponent listComponent) {
		this.groupName = groupName;
		this.listComponent = listComponent;
	}

}
