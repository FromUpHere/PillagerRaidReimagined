package net.fromuphere.raidreimagined.onslaughtevent;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.Objects;

public class Onslaught {
    private int intensity;
    private static final Component ONSLAUGHT_NAME_COMPONENT = Component.literal("Onslaught");
    private final ServerBossEvent onslaughtEvent;
    private final OnslaughtManager onslaughtManager;
    private boolean isActive = false;
    private int ticksActive;
    private boolean startSpawningRaiders;

    public Onslaught(int intensity, ServerPlayer serverPlayer) throws InterruptedException {
        this.intensity = intensity;
        onslaughtEvent = new ServerBossEvent(ONSLAUGHT_NAME_COMPONENT, BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6);
        onslaughtEvent.setVisible(true);
        onslaughtEvent.setProgress(0.0F);
        onslaughtManager = new OnslaughtManager(serverPlayer);
        isActive = true;
        startSpawningRaiders = false;
    }

    public void tick(PlayerTickEvent.Pre event) {
        ++ticksActive;
        Player player = event.getEntity();
        if (player.isLocalPlayer()) {
            return;
        } else {
            List<ServerPlayer> serverPlayer = Objects.requireNonNull(player.getServer(), "Player.getServer() does not exist in OnslaughtListener").getPlayerList().getPlayers();
            if (serverPlayer.contains(player)) {
                // Get player server side and
                ServerPlayer serverPlayer1 = player.getServer().getPlayerList().getPlayer(player.getGameProfile().getId());
                warningStage(serverPlayer1);

                if(startSpawningRaiders) {
                    onslaughtManager.setSpawningRaiders(true);
                }
            }

        }
    }

    public void updatePlayers(Player player) {

    }

    public void warningStage(ServerPlayer serverPlayer) {
        ServerPlayer serverPlayer1 = Objects.requireNonNull(serverPlayer.getServer(), "Line 44: Player.getServer() does not exist in OnslaughtListener").getPlayerList().getPlayer(serverPlayer.getUUID());
        // serverPlayer.sendSystemMessage(Component.literal("Now sending: " + serverPlayer1.getName().getString() + " " + SoundEvents.ELDER_GUARDIAN_CURSE.toString()));
        if((ticksActive % 40) == 0 && ticksActive <= 120) { Objects.requireNonNull(serverPlayer, "serverPlayer 1 is null").playNotifySound(SoundEvents.ELDER_GUARDIAN_CURSE, serverPlayer1.getSoundSource(), 1.0F, 1.0F); }
        if(ticksActive <= 500) {
            onslaughtEvent.setProgress(ticksActive / 500.0F);
        } else {
            onslaughtEvent.setProgress(1.0F);
            startSpawningRaiders = true;
        }
    }

    public void addPlayer(ServerPlayer player) {
        onslaughtEvent.addPlayer(player);
    }

    public void stop() {
        isActive = false;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public OnslaughtManager getOnslaughtManager() {
        return onslaughtManager;
    }
}
