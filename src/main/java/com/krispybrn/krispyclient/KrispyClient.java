package com.krispybrn.krispyclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KrispyClient implements ClientModInitializer {

	public static KeyBinding toggleHudKey;
	public static boolean hudEnabled = true;

	@Override
	public void onInitializeClient() {
		toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.krispyclient.togglehud",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_RIGHT_SHIFT,
			"category.krispyclient"
		));

		CpsTracker.register();
		HudRenderCallback.EVENT.register((context, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			while (toggleHudKey.wasPressed()) {
				hudEnabled = !hudEnabled;
			}
			if (hudEnabled && client.player != null) {
				HudRenderer.render(context, client);
			}
		});
	}
}
