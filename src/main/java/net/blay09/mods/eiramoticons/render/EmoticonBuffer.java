// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.render;

import net.blay09.mods.eiramoticons.emoticon.Emoticon;

public class EmoticonBuffer {

	private static final int BUFFER_SIZE = 16;

	public int count;
	public Emoticon[] emoticons = new Emoticon[BUFFER_SIZE];
	public int[] positionX = new int[BUFFER_SIZE];
	public int[] positionY = new int[BUFFER_SIZE];
	public float[] alpha = new float[BUFFER_SIZE];

	public void addEmoticon(Emoticon emoticon, int x, int y, float a) {
		if(count >= emoticons.length) {
			Emoticon[] emoticons = new Emoticon[this.emoticons.length * 2];
			System.arraycopy(this.emoticons, 0, emoticons, 0, this.emoticons.length);
			this.emoticons = emoticons;
			int[] positionX = new int[this.positionX.length * 2];
			System.arraycopy(this.positionX, 0, positionX, 0, this.positionX.length);
			this.positionX = positionX;
			int[] positionY = new int[this.positionY.length * 2];
			System.arraycopy(this.positionY, 0, positionY, 0, this.positionY.length);
			this.positionY = positionY;
			float[] alpha = new float[this.alpha.length * 2];
			System.arraycopy(this.alpha, 0, alpha, 0, this.alpha.length);
			this.alpha = alpha;
		}
		emoticons[count] = emoticon;
		positionX[count] = x;
		positionY[count] = y;
		alpha[count] = a;
		count++;
	}

	public void freeMemory() {
		if(emoticons.length > BUFFER_SIZE && count < BUFFER_SIZE) {
			emoticons = new Emoticon[BUFFER_SIZE];
			positionX = new int[BUFFER_SIZE];
			positionY = new int[BUFFER_SIZE];
			alpha = new float[BUFFER_SIZE];
		}
		count = 0;
	}

}
