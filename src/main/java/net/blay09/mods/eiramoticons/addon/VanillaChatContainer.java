package net.blay09.mods.eiramoticons.addon;

import net.blay09.mods.eiramoticons.api.ChatContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class VanillaChatContainer implements ChatContainer {

    @Override
    public float getOffsetX(ScaledResolution resolution) {
        return 2f;
    }

    @Override
    public float getOffsetY(ScaledResolution resolution) {
        return (float) (resolution.getScaledHeight() - 48) + 20f;
    }

    @Override
    public float getChatScale(ScaledResolution resolution) {
        return Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatScale();
    }

    @Override
    public boolean isCustomTransform() {
        return false;
    }

    @Override
    public void pushTransform(ScaledResolution resolution) {}

    @Override
    public void popTransform(ScaledResolution resolution) {}

}
