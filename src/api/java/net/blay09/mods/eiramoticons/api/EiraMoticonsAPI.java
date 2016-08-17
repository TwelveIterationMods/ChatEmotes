package net.blay09.mods.eiramoticons.api;

import net.minecraft.util.text.ITextComponent;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

public class EiraMoticonsAPI {

	private static IInternalMethods internalMethods;

	/**
	 * INTERNAL METHOD. DO NOT CALL.
	 * @param internalMethods implementation of internal API methods
	 */
	public static void setupAPI(IInternalMethods internalMethods) {
		EiraMoticonsAPI.internalMethods = internalMethods;
	}

	/**
	 * Registers an emoticon to be used in Minecraft.
	 * @param name the name that will be the code to use the emote in chat
	 * @param loader the loader that provides the BufferedImage for this emote
	 * @return an emoticon instance that can be used to set up further properties of the emote
	 */
	public static IEmoticon registerEmoticon(String name, IEmoticonLoader loader) {
		return internalMethods.registerEmoticon(name, loader);
	}

	/**
	 * Registers an emoticon group to be displayed in the /emoticons list command.
	 * @param groupName the name of this group
	 * @param listComponent the chat component that should be displayed when the /emoticons list command is run
	 */
	public static void registerEmoticonGroup(String groupName, ITextComponent listComponent) {
		internalMethods.registerEmoticonGroup(groupName, listComponent);
	}

	/**
	 * Replaces emoticon names in the string with the respective emoticon formatting codes.
	 * @param s the string to be searched for emoticons
	 */
	public static String replaceEmoticons(String s) {
		return internalMethods.replaceEmoticons(s);
	}

	/**
	 * Creates a complete copy of this chat component, replacing all emoticon names with the actual emoticons.
	 * @param component the component to be searched for emoticons
	 * @return a copy of the given chat component with emoticons
	 */
	public static ITextComponent replaceEmoticons(ITextComponent component) {
		return internalMethods.replaceEmoticons(component);
	}

	/**
	 * Loads the image at that uri into the given emoticon; takes care of .gif animations and all that stuff.
	 * @param emoticon the emoticon to have the image loaded in
	 * @param uri the uri of the image
	 * @return true if success, false if error
	 */
	public static boolean loadImage(IEmoticon emoticon, URI uri) {
		return internalMethods.loadImage(emoticon, uri);
	}

	/**
	 * Loads the input stream into the given emoticon; takes care of .gif animations and all that stuff.
	 * @param emoticon the emoticon to have the image loaded in
	 * @param in the input stream of the image
	 * @return true if success, false if error
	 */
	public static boolean loadImage(IEmoticon emoticon, InputStream in) {
		return internalMethods.loadImage(emoticon, in);
	}

	/**
	 * Loads the input file into the given emoticon; takes care of .gif animations and all that stuff.
	 * @param emoticon the emoticon to have the image loaded in
	 * @param file the input file of the image
	 * @return true if success, false if error
	 */
	public static boolean loadImage(IEmoticon emoticon, File file) {
		return internalMethods.loadImage(emoticon, file);
	}

	/**
	 * Set a custom chat container to determine the correct positioning of emoticons, for mods that completely override the Vanilla chat box.
	 * @param chatContainer chat container to be used for rendering
	 */
	public static void setChatContainer(ChatContainer chatContainer) {
		internalMethods.setChatContainer(chatContainer);
	}
}
