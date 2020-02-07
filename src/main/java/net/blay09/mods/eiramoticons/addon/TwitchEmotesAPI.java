package net.blay09.mods.eiramoticons.addon;

import net.blay09.mods.eiramoticons.addon.pack.AbstractEmotePack;
import net.blay09.mods.eiramoticons.api.EmoteLoaderException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class TwitchEmotesAPI {

	public static final int TWITCH_BASE_SIZE = 28;
	public static final String URL_SMALL = "https://static-cdn.jtvnw.net/emoticons/v1/{image_id}/1.0";

	private static final int CACHE_LIFETIME_JSON = 86400000;
	private static final int CACHE_LIFETIME_IMAGE = 604800000;
	private static final int TIMEOUT_TIME = 10000;

	private static final String URL_SETS = "https://api.twitchemotes.com/api/v4/sets?id=";
	private static final String URL_CHANNEL = "https://api.twitchemotes.com/api/v4/channels/";
	private static final int SETID_PRIME = 19194;
	private static final int SETID_GLOBAL = 0;
	
	private static final String URL_SUBSCRIBER = "https://twitchemotes.com/api_cache/v3/images.json";

	private static File cacheDir;
	private static File cacheSets;
	private static File cacheChannel;
	private static File cachedEmotes;
	private static File cachedGlobal;
	private static File cachedSubscriber;

	public static void initialize(File mcDataDir) {
		cacheDir = new File(mcDataDir, "emoticons/cache");
		cacheSets = new File(cacheDir, "set/");
		cacheChannel = new File(cacheDir, "channel/");
		cachedEmotes = new File(cacheDir, "images/");
		cachedGlobal = new File(cacheDir, "global.json");
		cachedSubscriber = new File(cacheDir, "images.json");
	}
	
	private static Reader getSet(int setId, boolean forceRemote) throws IOException {
		File cachedFile = new File(cacheSets, setId + ".json");
		if(forceRemote || !shouldUseCacheFileJson(cachedFile)) {
			FileUtils.copyURLToFile(new URL(URL_SETS + setId), cachedFile, TIMEOUT_TIME, TIMEOUT_TIME);
		}
		return new FileReader(cachedFile);
	}

	private static Reader getChannel(int channelId, boolean forceRemote) throws IOException {
		File cachedFile = new File (cacheChannel, channelId + ".json");
		if (forceRemote || !shouldUseCacheFileJson(cachedFile)) {
			FileUtils.copyURLToFile(new URL(URL_CHANNEL + channelId), cachedFile, TIMEOUT_TIME, TIMEOUT_TIME);
		}
		return new FileReader(cachedFile);
	}

	private static Reader getChannelFromSetId(int setId, boolean forceRemote) throws IOException {
		Reader reader = TwitchEmotesAPI.getSet(setId, false);
		Gson gson = new Gson();

		JsonArray setInfos;
		try {
			setInfos = gson.fromJson(reader, JsonArray.class);
		} catch (Exception e) {
			reader = TwitchEmotesAPI.getSet(setId, true);
			try {
				setInfos = gson.fromJson(reader, JsonArray.class);
			} catch (Exception e2) {
				throw new EmoteLoaderException(e2);
			}
		}
		
		if (setInfos != null && setInfos.size() > 0) {
			JsonObject setInfo = setInfos.get(0).getAsJsonObject();
			String channelIdStr = AbstractEmotePack.getJsonString(setInfo, "channel_id");

			int channelId = Integer.parseInt(channelIdStr);	
			
			reader.close();
			return TwitchEmotesAPI.getChannel(channelId, forceRemote);
		}
		reader.close();
		return null;
	}

	private static boolean shouldUseCacheFileJson(File file) {
		return file.exists() && (System.currentTimeMillis() - file.lastModified()) <= CACHE_LIFETIME_JSON;
	}

	private static boolean shouldUseCacheFileImage(File file) {
		return file.exists() && (System.currentTimeMillis() - file.lastModified()) <= CACHE_LIFETIME_IMAGE;
	}

	public static Reader newGlobalEmotesReader(boolean forceRemote) throws IOException {
		return TwitchEmotesAPI.getChannelFromSetId(SETID_GLOBAL, forceRemote);
	}

	public static Reader newSubscriberEmotesReader(boolean forceRemote) throws IOException {
		if(forceRemote || !shouldUseCacheFileJson(cachedSubscriber)) {
			FileUtils.copyURLToFile(new URL(URL_SUBSCRIBER), cachedSubscriber, TIMEOUT_TIME, TIMEOUT_TIME);
		}
		return new FileReader(cachedSubscriber);
	}

    public static Reader newPrimeEmotesReader(boolean forceRemote) throws IOException {
		return TwitchEmotesAPI.getChannelFromSetId(SETID_PRIME, forceRemote);
    }

	@Nullable
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
