package net.blay09.mods.eiramoticons.emoticon;

import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.blay09.mods.eiramoticons.api.ReloadEmoticons;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EmoticonRegistry {

	private static final AtomicInteger idCounter = new AtomicInteger();
	private static final IntHashMap<Emoticon> emoticonMap = new IntHashMap<>();
	private static final Map<String, Emoticon> namedMap = new HashMap<>();
	private static final Map<String, EmoticonGroup> groupMap = new HashMap<>();
	private static final List<Emoticon> disposalList = new ArrayList<>();
	public static boolean isLoading;

	private static final Object loadingLock = new Object();

	public static IEmoticon registerEmoticon(String name, IEmoticonLoader loader) {
		synchronized (loadingLock) {
			Emoticon emoticon = new Emoticon(idCounter.incrementAndGet(), name, loader);
			emoticonMap.addKey(emoticon.id, emoticon);
			namedMap.put(emoticon.name, emoticon);
			return emoticon;
		}
	}

	public static Collection<EmoticonGroup> getGroups() {
		synchronized (loadingLock) {
			return groupMap.values();
		}
	}

	public static EmoticonGroup registerEmoticonGroup(String groupName, ITextComponent listComponent) {
		synchronized (loadingLock) {
			EmoticonGroup group = new EmoticonGroup(groupName, listComponent);
			groupMap.put(groupName, group);
			return group;
		}
	}

	@Nullable
	public static Emoticon fromName(String name) {
		synchronized (loadingLock) {
			return namedMap.get(name);
		}
	}

	@Nullable
	public static Emoticon fromId(int id) {
		synchronized (loadingLock) {
			return emoticonMap.lookup(id);
		}
	}

	public static void reloadEmoticons() {
		synchronized (loadingLock) {
			synchronized (disposalList) {
				disposalList.addAll(namedMap.values());
			}
			idCounter.set(0);
			emoticonMap.clearMap();
			groupMap.clear();
			namedMap.clear();
			MinecraftForge.EVENT_BUS.post(new ReloadEmoticons());
		}
	}

	public static void runDisposal() {
		synchronized (disposalList) {
			if (!disposalList.isEmpty()) {
				for (Emoticon emoticon : disposalList) {
					emoticon.disposeTexture();
				}
				disposalList.clear();
			}
		}
	}
}
