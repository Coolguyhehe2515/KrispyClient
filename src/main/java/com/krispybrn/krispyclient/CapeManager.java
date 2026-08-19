package com.krispybrn.krispyclient;

public class CapeManager {

	public static int selectedCape = -1;

	public static final String[] CAPE_NAMES = {
		"Cape 1", "Cape 2", "Cape 3", "Cape 4", "Cape 5",
		"Cape 6", "Cape 7", "Cape 8", "Cape 9", "Cape 10"
	};

	public static net.minecraft.util.Identifier getTexture(int index) {
		return net.minecraft.util.Identifier.of("krispyclient", "textures/capes/cape_" + (index + 1) + ".png");
	}
}
