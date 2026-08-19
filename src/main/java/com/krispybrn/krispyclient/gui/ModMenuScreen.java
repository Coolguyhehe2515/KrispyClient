package com.krispybrn.krispyclient.gui;

import com.krispybrn.krispyclient.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModMenuScreen extends Screen {

	private static final Map<String, String> LABELS = new LinkedHashMap<>();
	static {
		LABELS.put("cps", "CPS Counter");
		LABELS.put("coords", "Coordinates");
		LABELS.put("fps", "FPS Display");
		LABELS.put("armor_status", "Armor Status");
		LABELS.put("reach_indicator", "Reach Indicator");
		LABELS.put("slime_chunks", "Slime Chunks");
		LABELS.put("no_fade", "No Fade");
	}

	private TextFieldWidget seedField;

	public ModMenuScreen() {
		super(Text.literal("Krispy Client"));
	}

	@Override
	protected void init() {
		int cols = 3;
		int cardW = 150;
		int cardH = 60;
		int gap = 10;
		int startX = (width - (cols * cardW + (cols - 1) * gap)) / 2;
		int startY = 70;

		int i = 0;
		for (String key : LABELS.keySet()) {
			int col = i % cols;
			int row = i / cols;
			int x = startX + col * (cardW + gap);
			int y = startY + row * (cardH + gap);

			addDrawableChild(ButtonWidget.builder(labelFor(key), btn -> {
				ModConfig.toggle(key);
				btn.setMessage(labelFor(key));
			}).dimensions(x, y + cardH - 24, cardW - 16, 20).build());

			i++;
		}

		int rows = (int) Math.ceil(LABELS.size() / (double) cols);
		int seedY = startY + rows * (cardH + gap) + 20;

		addDrawableChild(new ButtonWidget.Builder(Text.literal("Set World Seed"), btn -> {
			try {
				ModConfig.manualSeed = Long.parseLong(seedField.getText().trim());
			} catch (NumberFormatException ignored) {
			}
		}).dimensions(startX + cols * cardW + (cols - 1) * gap - 140, seedY, 140, 20).build());

		seedField = new TextFieldWidget(textRenderer, startX, seedY, 200, 20, Text.literal("World Seed"));
		seedField.setPlaceholder(Text.literal("Manual seed override"));
		if (ModConfig.manualSeed != null) {
			seedField.setText(String.valueOf(ModConfig.manualSeed));
		}
		addDrawableChild(seedField);
	}

	private Text labelFor(String key) {
		String state = ModConfig.isOn(key) ? "ON" : "OFF";
		return Text.literal(LABELS.get(key) + ": " + state);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, "Krispy Client", width / 2, 20, 0xFFFFFF);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
