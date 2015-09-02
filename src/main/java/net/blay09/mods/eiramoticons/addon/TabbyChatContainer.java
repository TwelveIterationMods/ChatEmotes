package net.blay09.mods.eiramoticons.addon;

import acs.tabbychat.gui.ChatBox;
import net.blay09.mods.eiramoticons.api.ChatContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class TabbyChatContainer implements ChatContainer {

    @Override
    public boolean isCustomTransform() {
        return false;
    }

    @Override
    public void pushTransform(ScaledResolution resolution) {}

    @Override
    public void popTransform(ScaledResolution resolution) {}

    @Override
    public float getChatScale(ScaledResolution resolution) {
        return Minecraft.getMinecraft().ingameGUI.getChatGUI().func_146244_h(); // getChatScale
    }

    @Override
    public float getOffsetX(ScaledResolution resolution) {
        return ChatBox.current.x;
    }

    @Override
    public float getOffsetY(ScaledResolution resolution) {
        return resolution.getScaledHeight() + ChatBox.current.y;
    }

}
