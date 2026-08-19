package com.krispybrn.krispyclient;

import net.fabricmc.fabric.api.client.event.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayDeque;
import java.util.Deque;

public class CpsTracker {

	private static final Deque<Long> leftClicks = new ArrayDeque<>();
	private static final Deque<Long> rightClicks = new ArrayDeque<>();
	private static boolean prevAttack = false;
	private static boolean prevUse = false;

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> tick(client));
	}

	private static void tick(MinecraftClient client) {
		long now = System.currentTimeMillis();
		boolean attack = client.options.attackKey.isPressed();
		boolean use = client.options.useKey.isPressed();

		if (attack && !prevAttack) leftClicks.add(now);
		if (use && !prevUse) rightClicks.add(now);
		prevAttack = attack;
		prevUse = use;

		while (!leftClicks.isEmpty() && now - leftClicks.peekFirst() > 1000) leftClicks.pollFirst();
		while (!rightClicks.isEmpty() && now - rightClicks.peekFirst() > 1000) rightClicks.pollFirst();
	}

	public static int getLeftCps() {
		return leftClicks.size();
	}

	public static int getRightCps() {
		return rightClicks.size();
	}
}
