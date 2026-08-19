package com.krispybrn.krispyclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class HudRenderer {

	public static void render(DrawContext context, MinecraftClient client) {
		int x = 4;
		int y = 4;
		int color = 0xFFFFFF;

		if (ModConfig.isOn("cps")) {
			context.drawText(client.textRenderer,
				"CPS: " + CpsTracker.getLeftCps() + " / " + CpsTracker.getRightCps(),
				x, y, color, true);
			y += 10;
		}

		if (ModConfig.isOn("coords")) {
			context.drawText(client.textRenderer,
				String.format("XYZ: %.1f %.1f %.1f",
					client.player.getX(), client.player.getY(), client.player.getZ()),
				x, y, color, true);
			y += 10;
		}

		if (ModConfig.isOn("fps")) {
			context.drawText(client.textRenderer, "FPS: " + client.getCurrentFps(), x, y, color, true);
			y += 10;
		}

		if (ModConfig.isOn("armor_status")) {
			for (EquipmentSlot slot : new EquipmentSlot[]{
				EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
			}) {
				ItemStack stack = client.player.getEquippedStack(slot);
				if (!stack.isEmpty() && stack.isDamageable()) {
					int durability = stack.getMaxDamage() - stack.getDamage();
					context.drawText(client.textRenderer,
						slot.getName() + ": " + durability + "/" + stack.getMaxDamage(),
						x, y, color, true);
					y += 10;
				}
			}
		}
	}
}
