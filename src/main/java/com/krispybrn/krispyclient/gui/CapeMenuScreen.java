package com.krispybrn.krispyclient.gui;

import com.krispybrn.krispyclient.CapeManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CapeMenuScreen extends Screen {

	private final Screen parent;

	public CapeMenuScreen(Screen parent) {
		super(Text.literal("Krispy Client Capes"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int cols = 3;
		int cardW = 140;
		int cardH = 24;
		int gap = 10;
		int startX = (width - (cols * cardW + (cols - 1) * gap)) / 2;
		int startY = 60;

		addDrawableChild(ButtonWidget.builder(labelFor(-1), btn -> {
			CapeManager.selectedCape = -1;
			btn.setMessage(labelFor(-1));
		}).dimensions(startX, startY, cardW, cardH).build());

		for (int i = 0; i < CapeManager.CAPE_NAMES.length; i++) {
			final int index = i;
			int col = (i + 1) % cols;
			int row = (i + 1) / cols;
			int x = startX + col * (cardW + gap);
			int y = startY + row * (cardH + gap);

			addDrawableChild(ButtonWidget.builder(labelFor(index), btn -> {
				CapeManager.selectedCape = index;
				btn.setMessage(labelFor(index));
			}).dimensions(x, y, cardW, cardH).build());
		}

		addDrawableChild(ButtonWidget.builder(Text.literal("Back"), btn -> {
			close();
		}).dimensions(width / 2 - 50, height - 30, 100, 20).build());
	}

	private Text labelFor(int index) {
		String name = index < 0 ? "None" : CapeManager.CAPE_NAMES[index];
		boolean selected = CapeManager.selectedCape == index;
		return Text.literal(name + (selected ? " ✓" : ""));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		context.drawCenteredTextWithShadow(textRenderer, "Select Cape", width / 2, 20, 0xFFFFFF);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void close() {
		if (client != null) client.setScreen(parent);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
