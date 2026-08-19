package com.krispybrn.krispyclient.gui;

import com.krispybrn.krispyclient.ArmorHudRenderer;
import com.krispybrn.krispyclient.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HudEditScreen extends Screen {

	private boolean dragging = false;
	private int dragOffsetX;
	private int dragOffsetY;

	public HudEditScreen() {
		super(Text.literal("Move Armor HUD"));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null) {
			ArmorHudRenderer.render(context, client, ModConfig.armorHudX, ModConfig.armorHudY);
		}
		context.drawCenteredTextWithShadow(textRenderer,
			"Drag the numbers to move, Escape to save", width / 2, 10, 0xFFFFFF);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int w = ArmorHudRenderer.getWidth(MinecraftClient.getInstance());
		int h = ArmorHudRenderer.getHeight();
		if (mouseX >= ModConfig.armorHudX && mouseX <= ModConfig.armorHudX + w
			&& mouseY >= ModConfig.armorHudY && mouseY <= ModConfig.armorHudY + h) {
			dragging = true;
			dragOffsetX = (int) mouseX - ModConfig.armorHudX;
			dragOffsetY = (int) mouseY - ModConfig.armorHudY;
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (dragging) {
			ModConfig.armorHudX = (int) mouseX - dragOffsetX;
			ModConfig.armorHudY = (int) mouseY - dragOffsetY;
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		dragging = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
