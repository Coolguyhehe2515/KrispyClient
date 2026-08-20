package com.krispybrn.krispyclient;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.TntEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.List;

public class TntTimerRenderer {

	private static final double RANGE = 48.0;

	public static void register() {
		WorldRenderEvents.AFTER_TRANSLUCENT.register(TntTimerRenderer::render);
	}

	private static void render(WorldRenderContext context) {
		if (!ModConfig.isOn("tnt_timer")) return;

		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null || client.world == null) return;

		Box area = client.player.getBoundingBox().expand(RANGE);
		List<TntEntity> tntList = client.world.getEntitiesByClass(TntEntity.class, area, tnt -> true);
		if (tntList.isEmpty()) return;

		MatrixStack matrices = context.matrixStack();
		Vec3d camPos = context.camera().getPos();
		VertexConsumerProvider.Immediate vcp = client.getBufferBuilders().getEntityVertexConsumers();
		TextRenderer textRenderer = client.textRenderer;
		int light = LightmapTextureManager.MAX_LIGHT_COORDINATE;

		for (TntEntity tnt : tntList) {
			int fuse = tnt.getFuse();
			float seconds = fuse / 20f;

			double x = tnt.getX() - camPos.x;
			double y = tnt.getY() + tnt.getHeight() + 0.6 - camPos.y;
			double z = tnt.getZ() - camPos.z;

			matrices.push();
			matrices.translate(x, y, z);
			matrices.multiply(client.getEntityRenderDispatcher().getRotation());
			matrices.scale(-0.025F, -0.025F, 0.025F);

			Text text = Text.literal(String.format("%.1fs", seconds));
			int color = fuse <= 20 ? 0xFF4444 : (fuse <= 40 ? 0xFFD34D : 0x34D399);
			float tx = -textRenderer.getWidth(text) / 2f;
			int bgColor = (int) (client.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
			Matrix4f matrix = matrices.peek().getPositionMatrix();

			textRenderer.draw(text, tx, 0, color, false, matrix, vcp,
				TextRenderer.TextLayerType.NORMAL, bgColor, light);

			matrices.pop();
		}

		vcp.draw();
	}
}
