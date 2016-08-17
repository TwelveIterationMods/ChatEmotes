package net.blay09.mods.eiramoticons.api;

/**
 * An interface representing an emoticon loader that will supply the BufferedImage for an emoticon.
 */
public interface IEmoticonLoader {

	/**
	 * Should load the BufferedImage for the given emoticon and call setImage() on it.
	 * This will be called from a separate thread, so don't call anything that requires a GL context here.
	 * @param emoticon the emoticon that should be loaded
	 */
	void loadEmoticonImage(IEmoticon emoticon);

}
