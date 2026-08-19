package com.krispybrn.krispyclient;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.PlayerModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CapeFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	private final ModelPart cape;

	public CapeFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
		super(context);
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();
		root.addChild("cape", ModelPartBuilder.create().uv(0, 0)
			.cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F),
			ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		TexturedModelData textured = TexturedModelData.of(modelData, 64, 32);
		this.cape = textured.createModel().getChild("cape");
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
						AbstractClientPlayerEntity entity, float limbAngle, float limbDistance,
						float tickDelta, float animationProgress, float headYaw, float headPitch) {

		if (CapeManager.selectedCape < 0) return;
		if (entity.isInvisible()) return;
		if (!entity.isPartVisible(PlayerModelPart.CAPE)) return;

		Identifier texture = CapeManager.getTexture(CapeManager.selectedCape);

		matrices.push();
		matrices.translate(0.0D, 0.0D, 0.125D);

		double dy = (entity.getY() - entity.prevY);
		float lean = (float) dy * 10.0F;
		float lean2 = MathHelper.clamp(lean, -6.0F, 32.0F);

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + lean2 / 2.0F));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

		cape.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(texture)), light, OverlayTexture.DEFAULT_UV);

		matrices.pop();
	}
}
