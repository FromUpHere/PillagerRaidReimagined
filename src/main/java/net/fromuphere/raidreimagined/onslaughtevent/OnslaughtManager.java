package net.fromuphere.raidreimagined.onslaughtevent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

import net.minecraft.world.entity.raid.Raider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


// Manages raiders in an event spawn conditions based on difficulty, intensity, and number of players
@EventBusSubscriber
public class OnslaughtManager {
    private final Map<Integer, Set<Raider>> raiders = new HashMap<Integer, Set<Raider>>();
    private BlockPos villageCenter;

    private int randomNumber;
    private double raiderDifficulty;
    private boolean spawningRaiders;
    private double uniqueRaider;
    private int intensity;
    private long tick;


    public OnslaughtManager(ServerPlayer player) {
        //villageCenter = getVillageCenter(player);
        this.randomNumber = (int) ((Math.random() * 5) + 1) * 40;
        this.uniqueRaider = 0;
        this.intensity = 0;
        this.spawningRaiders = false;
    }

    public void tick(PlayerTickEvent.Pre event, ServerPlayer serverPlayer) {
        if (spawningRaiders) {
            spawnClock(serverPlayer);
        }
    }

    public void spawnClock(ServerPlayer player) {
        ++tick;

        if ((tick % randomNumber) == 0) {
            randomNumber = (int) ((Math.random() * 5) + 1) * 40;
            spawnRaider(player);
        }

    }

    @SubscribeEvent
    public static void increaseIntensity(AttackEntityEvent event) {
        // KilledTrigger.TriggerInstance.playerKilledEntity().trigger().addPlayerListener();
        if (event.getTarget() instanceof Raider raider) {
            if (raider.isAlive()) {
                event.getEntity().sendSystemMessage(Component.literal("health of pillager: " + raider.getHealth()));
                event.getEntity().sendSystemMessage(Component.literal("WEEE!"));
            };
        }
    }

    public double calculateDifficulty(int intensity, double currentRaiderDifficulty, int numberOfRaiders, int numberOfPlayers) {

        // Number of players
        return 0;
    }

    public BlockPos getRaidSpawn(Vec3i direction) {
        return null;
    }

    public void spawnRaider(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("Raider spawns, next in " + randomNumber));
        EntityType.PILLAGER.spawn(Objects.requireNonNull(
                Objects.requireNonNull(
                        player.getServer()).getLevel(player.level().dimension()
                ),
                "Level does not exist in spawnRaider()"),
                new BlockPos(1, 56, 1), MobSpawnType.MOB_SUMMONED);
    }

    public void addRaiders(Raider raider) {}

    public void setRaiderProperties(Raider raider) {return;}

    public void setSpawningRaiders(boolean spawningRaiders) {this.spawningRaiders = spawningRaiders;}

    public Map<Integer, Set<Raider>> getRaiders() {return raiders;}
}
