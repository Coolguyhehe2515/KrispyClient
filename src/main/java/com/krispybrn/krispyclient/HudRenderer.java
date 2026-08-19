package com.krispybrn.krispyclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class HudRenderer {

	public static void render(DrawContext context, MinecraftClient client) {
		int x = 4;
		int y = 4;
		int color = 0xFFFFFF;

		context.drawText(client.textRenderer,
			"CPS: " + CpsTracker.getLeftCps() + " / " + CpsTracker.getRightCps(),
			x, y, color, true);
		y += 10;

		context.drawText(client.textRenderer,
			String.format("XYZ: %.1f %.1f %.1f",
				client.player.getX(), client.player.getY(), client.player.getZ()),
			x, y, color, true);
		y += 10;

		int fps = client.getCurrentFps();
		context.drawText(client.textRenderer, "FPS: " + fps, x, y, color, true);
		y += 10;

		for (EquipmentSlot slot : new EquipmentSlot[]{
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
		}) {
			ItemStack stack = client.player.getEquippedStack(slot);
			if (!stack.isEmpty() && stack.isDamageable()) {
				int max = stack.getMaxDamage();
				int dmg = stack.getDamage();
				int durability = max - dmg;
				context.drawText(client.textRenderer,
					slot.getName() + ": " + durability + "/" + max,
					x, y, color, true);
				y += 10;
			}
		}
	}
}
