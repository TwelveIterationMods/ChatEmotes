package net.blay09.mods.eiramoticons.addon;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class TwitchEmotesAPI {

	public static final int TWITCH_BASE_SIZE = 28;

	private static final int CACHE_LIFETIME_JSON = 86400000;
	private static final int CACHE_LIFETIME_IMAGE = 604800000;
	private static final int TIMEOUT_TIME = 10000;
	private static final String URL_GLOBAL = "https://twitchemotes.com/api_cache/v2/global.json";
	private static final String URL_SUBSCRIBER = "https://twitchemotes.com/api_cache/v2/subscriber.json";

	private static File cacheDir;
	private static File cachedEmotes;
	private static File cachedGlobal;
	private static File cachedSubscriber;

	public static void initialize(File mcDataDir) {
		cacheDir = new File(mcDataDir, "emoticons/cache");
		cachedEmotes = new File(cacheDir, "images/");
		cachedGlobal = new File(cacheDir, "global.json");
		cachedSubscriber = new File(cacheDir, "subscriber.json");
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
