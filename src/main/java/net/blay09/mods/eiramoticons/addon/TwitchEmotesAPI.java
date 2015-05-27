package net.blay09.mods.eiramoticons.addon;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

public class TwitchEmotesAPI {

	private static final int CACHE_LIFETIME = 86400000;
	private static final int TIMEOUT_TIME = 10000;
	private static final String URL_GLOBAL = "https://twitchemotes.com/api_cache/v2/global.json";
	private static final String URL_SUBSCRIBER = "https://twitchemotes.com/api_cache/v2/subscriber.json";

	private static File cachedGlobal;
	private static File cachedSubscriber;

	public static void initialize(File mcDataDir) {
		cachedGlobal = new File(mcDataDir, "emoticons/cache/global.json");
		cachedSubscriber = new File(mcDataDir, "emoticons/cache/subscriber.json");
	}

	private static boolean shouldUseCacheFile(File file) {
		return file.exists() && (System.currentTimeMillis() - file.lastModified()) <= CACHE_LIFETIME;
	}

	public static Reader newGlobalEmotesReader() throws IOException {
		if(!shouldUseCacheFile(cachedGlobal)) {
			FileUtils.copyURLToFile(new URL(URL_GLOBAL), cachedGlobal, TIMEOUT_TIME, TIMEOUT_TIME);
		}
		return new FileReader(cachedGlobal);
	}

	public static Reader newSubscriberEmotesReader() throws IOException {
		if(!shouldUseCacheFile(cachedSubscriber)) {
			FileUtils.copyURLToFile(new URL(URL_SUBSCRIBER), cachedSubscriber, TIMEOUT_TIME, TIMEOUT_TIME);
		}
		return new FileReader(cachedSubscriber);
	}
}
