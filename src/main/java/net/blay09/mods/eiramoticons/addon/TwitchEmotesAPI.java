package net.blay09.mods.eiramoticons.addon;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class TwitchEmotesAPI {

	public static final int TWITCH_BASE_SIZE = 28;
	public static final String URL_SMALL = "https://static-cdn.jtvnw.net/emoticons/v1/{image_id}/1.0";

	private static final int CACHE_LIFETIME_JSON = 86400000;
	private static final int CACHE_LIFETIME_IMAGE = 604800000;
	private static final int TIMEOUT_TIME = 10000;
	private static final String URL_GLOBAL = "https://twitchemotes.com/api_cache/v3/global.json";
	private static final String URL_SUBSCRIBER = "https://twitchemotes.com/api_cache/v3/subscriber.json";
	private static final String URL_PRIME = "https://api.twitch.tv/kraken/chat/emoticon_images?client_id=gdhi94otnk7c7746syjv7gkr6bizq4w&emotesets=19194";

	private static File cacheDir;
	private static File cachedEmotes;
	private static File cachedGlobal;
	private static File cachedSubscriber;
	private static File cachedPrime;

	public static void initialize(File mcDataDir) {
		cacheDir = new File(mcDataDir, "emoticons/cache");
		cachedEmotes = new File(cacheDir, "images/");
		cachedGlobal = new File(cacheDir, "global.json");
		cachedSubscriber = new File(cacheDir, "subscriber.json");
		cachedPrime = new File(cacheDir, "prime.json");
	}

	private static boolean shouldUseCacheFileJson(File file) {
		return file.exists() && (System.currentTimeMillis() - file.lastModified()) <= CACHE_LIFETIME_JSON;
	}

	private static boolean shouldUseCacheFileImage(File file) {
		return file.exists() && (System.currentTimeMillis() - file.lastModified()) <= CACHE_LIFETIME_IMAGE;
	}

	public static Reader newGlobalEmotesReader(boolean forceRemote) throws IOException {
		if(forceRemote || !shouldUseCacheFileJson(cachedGlobal)) {
			FileUtils.copyURLToFile(new URL(URL_GLOBAL), cachedGlobal, TIMEOUT_TIME, TIMEOUT_TIME);
		}
		return new FileReader(cachedGlobal);
	}

	public static Reader newSubscriberEmotesReader(boolean forceRemote) throws IOException {
		if(forceRemote || !shouldUseCacheFileJson(cachedSubscriber)) {
			FileUtils.copyURLToFile(new URL(URL_SUBSCRIBER), cachedSubscriber, TIMEOUT_TIME, TIMEOUT_TIME);
		}
		return new FileReader(cachedSubscriber);
	}

	public static Reader newPrimeEmotesReader(boolean forceRemote) throws IOException {
		if(forceRemote || !shouldUseCacheFileJson(cachedPrime)) {
			FileUtils.copyURLToFile(new URL(URL_PRIME), cachedPrime, TIMEOUT_TIME, TIMEOUT_TIME);
		}
		return new FileReader(cachedPrime);
	}

	public static BufferedImage readTwitchEmoteImage(String template, int imageId, String cachePrefix) {
		File cachedImageFile = new File(cachedEmotes, cachePrefix + "-" + imageId + ".png");
		if(shouldUseCacheFileImage(cachedImageFile)) {
			try {
				BufferedImage image = ImageIO.read(cachedImageFile);
				if(image != null) {
					return image;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedImage image = ImageIO.read(new URL(template.replace("{image_id}", String.valueOf(imageId))));
			if(image != null) {
				try {
					if(cachedImageFile.mkdirs()) {
						ImageIO.write(image, "PNG", cachedImageFile);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return image;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void clearCache() {
		try {
			FileUtils.deleteDirectory(cacheDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
