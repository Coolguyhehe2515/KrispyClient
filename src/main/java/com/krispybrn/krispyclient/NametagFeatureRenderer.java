package com.krispybrn.krispyclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

public class NametagFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public NametagFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
						AbstractClientPlayerEntity entity, float limbAngle, float limbDistance,
						float tickDelta, float animationProgress, float headYaw, float headPitch) {

		if (!ModConfig.isOn("own_nametag")) return;

		MinecraftClient client = MinecraftClient.getInstance();
		if (entity != client.player) return;
		if (client.options.getPerspective().isFirstPerson()) return;

		matrices.push();
		matrices.translate(0.0D, entity.getHeight() + 0.5D, 0.0D);
		matrices.multiply(client.getEntityRenderDispatcher().getRotation());
		matrices.scale(-0.025F, -0.025F, 0.025F);

		Matrix4f matrix = matrices.peek().getPositionMatrix();
		Text text = Text.literal(entity.getGameProfile().getName());
		TextRenderer textRenderer = client.textRenderer;
		float x = -textRenderer.getWidth(text) / 2f;
		int bgColor = (int) (client.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;

		textRenderer.draw(text, x, 0, 0xFFFFFF, false, matrix, vertexConsumers,
			TextRenderer.TextLayerType.NORMAL, bgColor, light);

		matrices.pop();
	}
}
