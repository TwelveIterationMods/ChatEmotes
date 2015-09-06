package net.blay09.mods.eiramoticons.api;

import net.minecraft.client.gui.ScaledResolution;

/**
 * Interface for mods that override the chat box completely; used to setup the positioning of the emoticons correctly.
 */
public interface ChatContainer {

    /**
     * If this method returns true, pushTransform will be called before rendering emoticons and popTransform after; otherwise a default matrix based on the offset and scale functions will be set up.
     * @return true if setting up a custom matrix instead of using the default one
     */
    boolean isCustomTransform();

    /**
     * Called before emoticons are rendered, allows setup of a default GL matrix. Requires isCustomTransform to return true.
     * @param resolution the game resolution
     */
    void pushTransform(ScaledResolution resolution);

    /**
     * Called after emoticons are rendered, usually you'd just glPopMatrix() here. Requires isCustomTransform to return true.
     * @param resolution the game resolution
     */
    void popTransform(ScaledResolution resolution);

    /**
     * @param resolution the game resolution
     * @return a float value defining the scale of the chat
     */
    float getChatScale(ScaledResolution resolution);

    /**
     * @param resolution the game resolution
     * @return the left position of the chat
     */
    float getOffsetX(ScaledResolution resolution);

    /**
     * @param resolution the game resolution
     * @return the top position of the chat
     */
    float getOffsetY(ScaledResolution resolution);

}
