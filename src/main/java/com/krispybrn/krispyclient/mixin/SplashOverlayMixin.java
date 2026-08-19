package com.krispybrn.krispyclient.mixin;

import com.krispybrn.krispyclient.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashOverlay.class)
public class SplashOverlayMixin {

	@Shadow
	private long reloadStartTime;

	@Shadow
	private long reloadCompleteTime;

	@Inject(method = "render", at = @At("HEAD"))
	private void krispyclient$skipFade(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (!ModConfig.isOn("no_fade")) return;

		long past = Util.getMeasuringTimeMs() - 100000L;
		reloadStartTime = past;
		if (reloadCompleteTime != 0L) {
			reloadCompleteTime = past;
		}
	}
}
