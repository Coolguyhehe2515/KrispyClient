package com.krispybrn.krispyclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ArmorHudRenderer {

	private static final EquipmentSlot[] SLOTS = {
		EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};

	private static final float SCALE = 2.0f;
	private static final int ROW_HEIGHT = 34;
	private static final int ICON_SIZE = 16;

	public static void render(DrawContext context, MinecraftClient client, int x, int y) {
		if (client.player == null) return;

		int row = 0;
		for (EquipmentSlot slot : SLOTS) {
			ItemStack stack = client.player.getEquippedStack(slot);
			if (stack.isEmpty() || !stack.isDamageable()) {
				row++;
				continue;
			}
			int durability = stack.getMaxDamage() - stack.getDamage();
			int rowY = y + row * ROW_HEIGHT;
			String text = String.valueOf(durability);
			int textWidth = client.textRenderer.getWidth(text);

			context.getMatrices().push();
			context.getMatrices().translate(x, rowY, 0);
			context.getMatrices().scale(SCALE, SCALE, 1f);
			context.drawText(client.textRenderer, text, 0, 0, 0xFFFFFF, true);
			context.getMatrices().pop();

			int iconX = x + (int) (textWidth * SCALE) + 10;
			context.getMatrices().push();
			context.getMatrices().translate(iconX, rowY - 2, 0);
			context.getMatrices().scale(SCALE, SCALE, 1f);
			context.drawItem(stack, 0, 0);
			context.getMatrices().pop();

			row++;
		}
	}

	public static int getWidth(MinecraftClient client) {
		if (client.player == null) return 0;
		int maxTextWidth = 0;
		for (EquipmentSlot slot : SLOTS) {
			ItemStack stack = client.player.getEquippedStack(slot);
			if (stack.isEmpty() || !stack.isDamageable()) continue;
			int durability = stack.getMaxDamage() - stack.getDamage();
			maxTextWidth = Math.max(maxTextWidth, client.textRenderer.getWidth(String.valueOf(durability)));
		}
		return (int) (maxTextWidth * SCALE) + 10 + (int) (ICON_SIZE * SCALE);
	}

	public static int getHeight() {
		return ROW_HEIGHT * SLOTS.length;
	}
}
