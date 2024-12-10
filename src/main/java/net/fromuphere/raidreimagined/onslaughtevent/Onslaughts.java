package net.fromuphere.raidreimagined.onslaughtevent;

import java.util.Map;

import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class Onslaughts extends SavedData {
    private static final Map<Integer, Onslaught> OnslaughtMap = Maps.newHashMap();
    private static int onslaughtCounter = 0;

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return null;
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Pre event) {
        OnslaughtMap.forEach((id, onslaught) -> {
            if(OnslaughtMap.get(id).getIsActive() && event.getEntity() instanceof ServerPlayer serverPlayer) {
                onslaught.tick(event);
                onslaught.getOnslaughtManager().tick(event, serverPlayer);
            }
        });
    }

    public static void addOnslaught(Onslaught onslaught) {
        OnslaughtMap.put(onslaughtCounter, onslaught);
        onslaughtCounter++;
    }

    public static Map<Integer, Onslaught> getOnslaughts(int id) {
        return OnslaughtMap;
    }
}
