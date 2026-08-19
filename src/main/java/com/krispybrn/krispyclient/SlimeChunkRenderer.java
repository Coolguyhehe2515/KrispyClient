package com.krispybrn.krispyclient;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class SlimeChunkRenderer {

	private static final int RADIUS = 6;

	public static void register() {
		WorldRenderEvents.AFTER_TRANSLUCENT.register(SlimeChunkRenderer::render);
	}

	private static void render(WorldRenderContext context) {
		if (!ModConfig.isOn("slime_chunks")) return;

		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		if (player == null || client.world == null) return;

		if (ModConfig.manualSeed == null) return;
		long seed = ModConfig.manualSeed;

		MatrixStack matrices = context.matrixStack();
		Vec3d camPos = context.camera().getPos();
		VertexConsumerProvider.Immediate vcp = client.getBufferBuilders().getEntityVertexConsumers();

		ChunkPos playerChunk = player.getChunkPos();
		double y = player.getY() - 1;

		for (int dx = -RADIUS; dx <= RADIUS; dx++) {
			for (int dz = -RADIUS; dz <= RADIUS; dz++) {
				int cx = playerChunk.x + dx;
				int cz = playerChunk.z + dz;
				if (!SlimeChunkUtil.isSlimeChunk(seed, cx, cz)) continue;

				double minX = (cx << 4) - camPos.x;
				double minZ = (cz << 4) - camPos.z;
				Box box = new Box(minX, y - camPos.y, minZ, minX + 16, y - camPos.y + 0.05, minZ + 16);

				WorldRenderer.drawBox(matrices, vcp.getBuffer(RenderLayer.getLines()),
					box, 0.2f, 1f, 0.3f, 1f);
			}
		}

		vcp.draw();
	}
}
