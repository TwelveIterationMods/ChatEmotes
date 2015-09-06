package net.blay09.mods.eiramoticons.addon;

import mnm.mods.tabbychat.api.TabbyAPI;
import net.blay09.mods.eiramoticons.api.ChatContainer;
import net.minecraft.client.gui.ScaledResolution;

public class TabbyChat2Container implements ChatContainer {

    @Override
    public float getOffsetX(ScaledResolution resolution) {
        return TabbyAPI.getAPI().getGui().getChatArea().getBounds().x;
    }

    @Override
    public float getOffsetY(ScaledResolution resolution) {
        return TabbyAPI.getAPI().getGui().getChatArea().getBounds().y;
    }

    @Override
    public float getChatScale(ScaledResolution resolution) {
        return TabbyAPI.getAPI().getGui().getChatArea().getScale();
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
