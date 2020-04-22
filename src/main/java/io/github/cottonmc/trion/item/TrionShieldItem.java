package io.github.cottonmc.trion.item;

import io.github.cottonmc.trion.Trion;
import io.github.cottonmc.trion.api.TriggerItem;
import io.github.cottonmc.trion.api.TrionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class TrionShieldItem extends Item implements TrionShield, TriggerItem {
	public TrionShieldItem(Settings settings) {
		super(settings);
		this.addPropertyGetter(new Identifier("blocking"), (stack, world, entity) ->
				entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
		);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}

	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (getShieldDamage(user, stack) == getMaxShieldDamage(user, stack)) return TypedActionResult.consume(stack);
		user.setCurrentHand(hand);
		return TypedActionResult.consume(stack);
	}

	@Override
	public int getMaxShieldDamage(PlayerEntity wielder, ItemStack stack) {
		return 128;
	}

	@Override
	public void tickShield(PlayerEntity wielder, ItemStack stack) {
		TrionComponent component = Trion.TRION_COMPONENT.get(wielder);
		int currentDamage = getShieldDamage(wielder, stack);
		if (component.isTriggerActive() && wielder.world.getTime() % 60 == 0 && currentDamage != 0) {
			component.setTrion(component.getTrion() - 4, true);
			setShieldDamage(wielder, stack, currentDamage - 1);
		}
	}

	@Override
	public int getColor(PlayerEntity wielder, ItemStack stack) {
		return 0x5fec94;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (entity instanceof PlayerEntity && !world.isClient) {
			tickShield((PlayerEntity) entity, stack);
		}
	}
}