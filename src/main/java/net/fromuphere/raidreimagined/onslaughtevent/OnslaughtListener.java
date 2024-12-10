package net.fromuphere.raidreimagined.onslaughtevent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.fromuphere.raidreimagined.PillagerRaidReimagined;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.*;

import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.core.jmx.Server;


@EventBusSubscriber(modid = PillagerRaidReimagined.MODID, bus = EventBusSubscriber.Bus.GAME)
public class OnslaughtListener {
    @SubscribeEvent
    public static void checkConditions(PlayerTickEvent.Post event) throws InterruptedException {
        if(hasOmenEffect(event) /*&& playerInVillage(event)*/ && occupiedVillagersNearby(event)) {
            if(event.getEntity() instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.literal("Event triggered"));

                Onslaught newOnslaught = new Onslaught(0, serverPlayer);
                newOnslaught.addPlayer(serverPlayer);

                Onslaughts.addOnslaught(newOnslaught);
            }
        }
    }

    /*
    List of conditions:
    - Player has Bad Omen V
    - Player is in a village
    - Player has occupied villagers nearby
     */

    // likely will be changed with an endgame item
    public static boolean hasOmenEffect(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        return player.hasEffect(MobEffects.BAD_OMEN) && Objects.requireNonNull(player.getEffect(MobEffects.BAD_OMEN), "Player does not have bad omen").getAmplifier() == 4;
    }

    public static boolean playerInVillage(PlayerTickEvent.Post event) {
        // Locate if the player is in a village BuiltInStructure
        Map<Structure, LongSet> list;
        Player player = event.getEntity();
        AtomicBoolean playerInVillage = new AtomicBoolean(false);

        // Basically player can be found through client and server
        if (player.isLocalPlayer()) {
            return false;
        } else {
            List<ServerPlayer> serverPlayer = Objects.requireNonNull(player.getServer(), "Player.getServer() does not exist in OnslaughtListener").getPlayerList().getPlayers();
            if (serverPlayer.contains(player)) {
                // Get player server side
                ServerPlayer serverPlayer1 = player.getServer().getPlayerList().getPlayer(player.getGameProfile().getId());

                // Get the class to locate structures and put in a list
                assert serverPlayer1 != null;
                ServerLevel seed = Objects.requireNonNull(serverPlayer1.getServer(), "getServer() does not exist in ServerLevel seed in playerInVillage()").getLevel(serverPlayer1.level().dimension());
                StructureManager structureManager = Objects.requireNonNull((seed), "StructureManager does not exist in ServerLevel seed in OnslaughtListener").structureManager();
                list = structureManager.getAllStructuresAt(player.getOnPos());

                // Checks if one of the structures is a village
                list.forEach((structure, set) -> {
                    if(structure instanceof JigsawStructure) {
                        playerInVillage.set(true);
                    }}
                );
            }
        }

        return playerInVillage.get();
    }

    public static boolean occupiedVillagersNearby(PlayerTickEvent event) {
        Player player = event.getEntity();
        Stream<PoiRecord> poiRecordStream;

        if(player.isLocalPlayer()) {
            return false;
        } else {
            List<ServerPlayer> serverPlayer = Objects.requireNonNull(player.getServer()).getPlayerList().getPlayers();
            if(serverPlayer.contains(player)) {
                ServerPlayer serverPlayer1 = player.getServer().getPlayerList().getPlayer(player.getGameProfile().getId());
                assert serverPlayer1 != null;

                PoiManager poiManager = player.getServer().overworld().getPoiManager();
                poiRecordStream = poiManager.getInRange((PoiType) -> {
                    return PoiType.is(PoiTypeTags.VILLAGE);
                }, player.getOnPos(), 64, PoiManager.Occupancy.IS_OCCUPIED);

                return poiRecordStream.findAny().isPresent();
            }
        }

        return false;
    }
}
