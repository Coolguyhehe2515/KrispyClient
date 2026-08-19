package com.krispybrn.krispyclient;

import com.krispybrn.krispyclient.gui.ModMenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KrispyClient implements ClientModInitializer {

	public static KeyBinding openMenuKey;
	public static boolean hudEnabled = true;

	@Override
	public void onInitializeClient() {
		openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.krispyclient.openmenu",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_RIGHT_SHIFT,
			"category.krispyclient"
		));

		CpsTracker.register();
		ReachIndicator.register();
		SlimeChunkRenderer.register();

		net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer instanceof net.minecraft.client.render.entity.PlayerEntityRenderer) {
				registrationHelper.register(new CapeFeatureRenderer(
					(net.minecraft.client.render.entity.feature.FeatureRendererContext<
						net.minecraft.client.network.AbstractClientPlayerEntity,
						net.minecraft.client.render.entity.model.PlayerEntityModel<net.minecraft.client.network.AbstractClientPlayerEntity>>) entityRenderer));
				registrationHelper.register(new NametagFeatureRenderer(
					(net.minecraft.client.render.entity.feature.FeatureRendererContext<
						net.minecraft.client.network.AbstractClientPlayerEntity,
						net.minecraft.client.render.entity.model.PlayerEntityModel<net.minecraft.client.network.AbstractClientPlayerEntity>>) entityRenderer));
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openMenuKey.wasPressed()) {
				if (client.currentScreen == null) {
					client.setScreen(new ModMenuScreen());
				}
			}
		});

		HudRenderCallback.EVENT.register((context, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.player != null) {
				HudRenderer.render(context, client);
			}
		});
	}
}
