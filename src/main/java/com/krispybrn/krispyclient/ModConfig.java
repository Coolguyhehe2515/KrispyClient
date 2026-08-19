package com.krispybrn.krispyclient;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModConfig {

	public static final Map<String, Boolean> toggles = new LinkedHashMap<>();
	public static Long manualSeed = null;
	public static int armorHudX = 10;
	public static int armorHudY = 60;

	static {
		toggles.put("cps", true);
		toggles.put("coords", true);
		toggles.put("fps", true);
		toggles.put("armor_status", true);
		toggles.put("reach_indicator", true);
		toggles.put("slime_chunks", true);
		toggles.put("no_fade", true);
		toggles.put("own_nametag", true);
	}

	public static boolean isOn(String key) {
		return toggles.getOrDefault(key, false);
	}

	public static void toggle(String key) {
		toggles.put(key, !isOn(key));
	}
}
