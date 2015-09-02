package net.blay09.mods.eiramoticons.api;

import net.minecraft.client.gui.ScaledResolution;

public interface ChatContainer {

    boolean isCustomTransform();
    void pushTransform(ScaledResolution resolution);
    void popTransform(ScaledResolution resolution);
    float getChatScale(ScaledResolution resolution);
    float getOffsetX(ScaledResolution resolution);
    float getOffsetY(ScaledResolution resolution);

}
