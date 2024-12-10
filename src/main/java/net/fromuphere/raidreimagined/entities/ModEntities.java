package net.fromuphere.raidreimagined.entities;

import net.fromuphere.raidreimagined.PillagerRaidReimagined;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            BuiltInRegistries.ENTITY_TYPE, PillagerRaidReimagined.MODID
    );

    public void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
