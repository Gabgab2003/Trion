package io.github.cottonmc.trion.registry;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.Trigger;
import io.github.cottonmc.trion.trigger.BailOutTrigger;
import io.github.cottonmc.trion.trigger.ChameleonTrigger;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TrionTriggers {
	public static final Trigger BAIL_OUT = register(new BailOutTrigger(), "bail_out");
	public static final Trigger CHAMELEON = register(new ChameleonTrigger(), "chameleon");

	public static void init() { }

	private static Trigger register(Trigger trigger, String name) {
		return Registry.register(Trion.TRIGGERS, new Identifier(Trion.MODID, name), trigger);
	}
}