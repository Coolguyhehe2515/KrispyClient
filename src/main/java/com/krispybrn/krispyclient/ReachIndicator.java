package com.krispybrn.krispyclient;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ReachIndicator {

	private static final double REACH = 3.0;

	public static void register() {
		WorldRenderEvents.AFTER_TRANSLUCENT.register(ReachIndicator::render);
	}

	private static void render(WorldRenderContext context) {
		if (!ModConfig.isOn("reach_indicator")) return;

		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		if (player == null || client.world == null) return;

		LivingEntity target = getTarget(client, player);
		if (target == null) return;

		MatrixStack matrices = context.matrixStack();
		Vec3d camPos = context.camera().getPos();
		VertexConsumerProvider.Immediate vcp = client.getBufferBuilders().getEntityVertexConsumers();

		float distFactor = (float) (1.0 - (player.distanceTo(target) / REACH));
		float r = 1f;
		float g = distFactor * 0.3f;
		float b = distFactor * 0.3f;

		Box box = target.getBoundingBox().offset(-camPos.x, -camPos.y, -camPos.z);
		WorldRenderer.drawBox(matrices, vcp.getBuffer(RenderLayer.getLines()), box, r, g, b, 1f);

		vcp.draw();
	}

	private static LivingEntity getTarget(MinecraftClient client, PlayerEntity player) {
		HitResult hit = client.crosshairTarget;
		if (!(hit instanceof EntityHitResult entityHit)) return null;

		Entity entity = entityHit.getEntity();
		if (!(entity instanceof LivingEntity living)) return null;
		if (living == player) return null;
		if (player.distanceTo(living) > REACH) return null;

		return living;
	}
}
