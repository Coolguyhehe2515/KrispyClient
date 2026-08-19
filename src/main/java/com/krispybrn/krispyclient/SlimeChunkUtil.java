package com.krispybrn.krispyclient;

import java.util.Random;

public class SlimeChunkUtil {

	public static boolean isSlimeChunk(long seed, int chunkX, int chunkZ) {
		long chunkSeed = seed
			+ (long) (chunkX * chunkX * 4987142)
			+ (long) (chunkX * 5947611)
			+ (long) (chunkZ * chunkZ) * 4392871L
			+ (long) (chunkZ * 389711)
			^ 987234911L;
		Random random = new Random(chunkSeed);
		return random.nextInt(10) == 0;
	}
}
