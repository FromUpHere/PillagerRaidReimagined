package net.fromuphere.raidreimagined.items;

import net.fromuphere.raidreimagined.PillagerRaidReimagined;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            BuiltInRegistries.ITEM, PillagerRaidReimagined.MODID
    );


    public void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
