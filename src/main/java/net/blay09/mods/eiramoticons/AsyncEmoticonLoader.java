// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons;

import net.blay09.mods.eiramoticons.api.IEmoticon;

import java.util.LinkedList;

public class AsyncEmoticonLoader implements Runnable {

	public static final AsyncEmoticonLoader instance = new AsyncEmoticonLoader();

	private boolean running;
	private final LinkedList<IEmoticon> loadQueue = new LinkedList<IEmoticon>();

	public AsyncEmoticonLoader() {
		running = true;
		Thread thread = new Thread(this, "EiraMoticonLoader");
		thread.start();
	}

	public void loadAsync(IEmoticon emoticon) {
		synchronized(loadQueue) {
			loadQueue.push(emoticon);
		}
	}

	@Override
	public void run() {
		while(running) {
			try {
				synchronized (loadQueue) {
					while (!loadQueue.isEmpty()) {
						IEmoticon emoticon = loadQueue.pop();
						emoticon.getLoader().loadEmoticonImage(emoticon);
					}
				}
				Thread.sleep(100);
			} catch (InterruptedException ignored) {}
		}
	}
}
