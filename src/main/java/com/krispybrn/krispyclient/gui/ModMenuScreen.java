package com.krispybrn.krispyclient.gui;

import com.krispybrn.krispyclient.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModMenuScreen extends Screen {

	private static final Identifier CARD_OFF = Identifier.of("krispyclient", "textures/gui/card_off.png");
	private static final Identifier CARD_ON = Identifier.of("krispyclient", "textures/gui/card_on.png");
	private static final Identifier CARD_HOVER = Identifier.of("krispyclient", "textures/gui/card_hover.png");
	private static final Identifier BTN = Identifier.of("krispyclient", "textures/gui/button_small.png");
	private static final Identifier BTN_HOVER = Identifier.of("krispyclient", "textures/gui/button_small_hover.png");
	private static final Identifier AURORA = Identifier.of("krispyclient", "textures/gui/aurora_band.png");

	private static final Map<String, String> LABELS = new LinkedHashMap<>();
	static {
		LABELS.put("cps", "CPS Counter");
		LABELS.put("coords", "Coordinates");
		LABELS.put("fps", "FPS Display");
		LABELS.put("armor_status", "Armor Status");
		LABELS.put("reach_indicator", "Reach Indicator");
		LABELS.put("slime_chunks", "Slime Chunks");
		LABELS.put("no_fade", "No Fade");
		LABELS.put("own_nametag", "F5 Nametag");
		LABELS.put("tnt_timer", "TNT Timer");
	}

	private static class Card {
		int x, y, w, h;
		String key;
	}

	private static class SmallButton {
		int x, y, w, h;
		String label;
		Runnable action;
	}

	private final List<Card> cards = new ArrayList<>();
	private final List<SmallButton> buttons = new ArrayList<>();
	private TextFieldWidget seedField;

	private final int cols = 3;
	private final int cardW = 150;
	private final int cardH = 60;
	private final int gap = 10;
	private int startX;
	private int startY;

	private final int contentTop = 55;
	private final int bottomMargin = 12;
	private int seedFieldBaseY;
	private double scrollAmount = 0;
	private int maxScroll = 0;

	public ModMenuScreen() {
		super(Text.literal("Krispy Client"));
	}

	@Override
	protected void init() {
		cards.clear();
		buttons.clear();
		scrollAmount = 0;

		startX = (width - (cols * cardW + (cols - 1) * gap)) / 2;
		startY = 70;

		int i = 0;
		for (String key : LABELS.keySet()) {
			int col = i % cols;
			int row = i / cols;
			Card card = new Card();
			card.x = startX + col * (cardW + gap);
			card.y = startY + row * (cardH + gap);
			card.w = cardW;
			card.h = cardH;
			card.key = key;
			cards.add(card);
			i++;
		}

		int rows = (int) Math.ceil(LABELS.size() / (double) cols);
		int rowBelow = startY + rows * (cardH + gap) + 20;

		SmallButton capes = new SmallButton();
		capes.x = startX;
		capes.y = rowBelow + 30;
		capes.w = 140;
		capes.h = 24;
		capes.label = "Capes";
		capes.action = () -> { if (client != null) client.setScreen(new CapeMenuScreen(this)); };
		buttons.add(capes);

		SmallButton moveHud = new SmallButton();
		moveHud.x = startX + 150;
		moveHud.y = rowBelow + 30;
		moveHud.w = 140;
		moveHud.h = 24;
		moveHud.label = "Move Armor HUD";
		moveHud.action = () -> { if (client != null) client.setScreen(new HudEditScreen()); };
		buttons.add(moveHud);

		seedFieldBaseY = rowBelow;
		seedField = new TextFieldWidget(textRenderer, startX, seedFieldBaseY, 200, 20, Text.literal("World Seed"));
		seedField.setPlaceholder(Text.literal("Manual seed override"));
		if (ModConfig.manualSeed != null) {
			seedField.setText(String.valueOf(ModConfig.manualSeed));
		}
		addDrawableChild(seedField);

		SmallButton setSeed = new SmallButton();
		setSeed.x = startX + 210;
		setSeed.y = rowBelow;
		setSeed.w = 140;
		setSeed.h = 20;
		setSeed.label = "Set World Seed";
		setSeed.action = () -> {
			try {
				ModConfig.manualSeed = Long.parseLong(seedField.getText().trim());
			} catch (NumberFormatException ignored) {
			}
		};
		buttons.add(setSeed);

		int contentBottom = rowBelow + 30 + 24 + bottomMargin;
		int viewportHeight = (height - bottomMargin) - contentTop;
		maxScroll = Math.max(0, contentBottom - contentTop - viewportHeight);
	}

	private int viewportBottom() {
		return height - bottomMargin;
	}

	private void applyScroll(double amount) {
		scrollAmount = Math.max(0, Math.min(maxScroll, scrollAmount + amount));
		seedField.setY(seedFieldBaseY - (int) scrollAmount);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fillGradient(0, 0, width, height, 0xE60A0D12, 0xE60F131A);

		long time = System.currentTimeMillis();
		float phase = (time % 14000L) / 14000f;
		float driftX = (float) Math.sin(phase * Math.PI * 2) * (width * 0.04f);
		float driftY = (float) Math.sin(phase * Math.PI * 2 + 1.5f) * 6f;
		context.drawTexture(AURORA, (int) driftX, (int) driftY, 0, 0, width, 90, 512, 140);

		context.drawCenteredTextWithShadow(textRenderer, "Krispy Client", width / 2, 20, 0xFFFFFF);

		int viewBottom = viewportBottom();
		context.enableScissor(0, contentTop, width, viewBottom);
		context.getMatrices().push();
		context.getMatrices().translate(0, -scrollAmount, 0);

		for (Card card : cards) {
			boolean on = ModConfig.isOn(card.key);
			double screenY = card.y - scrollAmount;
			boolean hovered = mouseX >= card.x && mouseX <= card.x + card.w
				&& mouseY >= screenY && mouseY <= screenY + card.h
				&& mouseY >= contentTop && mouseY <= viewBottom;

			Identifier tex = hovered ? CARD_HOVER : (on ? CARD_ON : CARD_OFF);
			context.drawTexture(tex, card.x, card.y, 0, 0, card.w, card.h, card.w, card.h);

			String label = LABELS.get(card.key);
			int labelW = textRenderer.getWidth(label);
			context.drawText(textRenderer, label, card.x + (card.w - labelW) / 2, card.y + 14, 0xFFFFFF, true);

			String state = on ? "ON" : "OFF";
			int stateColor = on ? 0x34D399 : 0x8A8F98;
			int stateW = textRenderer.getWidth(state);
			context.drawText(textRenderer, state, card.x + (card.w - stateW) / 2, card.y + 34, stateColor, true);
		}

		for (SmallButton btn : buttons) {
			double screenY = btn.y - scrollAmount;
			boolean hovered = mouseX >= btn.x && mouseX <= btn.x + btn.w
				&& mouseY >= screenY && mouseY <= screenY + btn.h
				&& mouseY >= contentTop && mouseY <= viewBottom;
			Identifier tex = hovered ? BTN_HOVER : BTN;
			context.drawTexture(tex, btn.x, btn.y, 0, 0, btn.w, btn.h, btn.w, btn.h);
			int labelW = textRenderer.getWidth(btn.label);
			context.drawText(textRenderer, btn.label, btn.x + (btn.w - labelW) / 2, btn.y + (btn.h - 8) / 2, 0xFFFFFF, true);
		}

		super.render(context, mouseX, mouseY, delta);

		context.getMatrices().pop();
		context.disableScissor();

		if (maxScroll > 0) {
			int trackX = width - 8;
			int trackH = viewBottom - contentTop;
			context.fill(trackX, contentTop, trackX + 4, viewBottom, 0x40FFFFFF);
			int thumbH = Math.max(20, (int) (trackH * ((double) trackH / (trackH + maxScroll))));
			int thumbY = contentTop + (int) ((trackH - thumbH) * (scrollAmount / maxScroll));
			context.fill(trackX, thumbY, trackX + 4, thumbY + thumbH, 0xA0FFFFFF);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (maxScroll > 0 && mouseY >= contentTop && mouseY <= viewportBottom()) {
			applyScroll(-amount * 16);
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (mouseY < contentTop || mouseY > viewportBottom()) {
			return super.mouseClicked(mouseX, mouseY, button);
		}
		double adjY = mouseY + scrollAmount;
		for (Card card : cards) {
			if (mouseX >= card.x && mouseX <= card.x + card.w
				&& adjY >= card.y && adjY <= card.y + card.h) {
				ModConfig.toggle(card.key);
				return true;
			}
		}
		for (SmallButton btn : buttons) {
			if (mouseX >= btn.x && mouseX <= btn.x + btn.w
				&& adjY >= btn.y && adjY <= btn.y + btn.h) {
				btn.action.run();
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
