package net.fromuphere.raidreimagined.onslaughtevent;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fromuphere.raidreimagined.PillagerRaidReimagined;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;


import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;

import java.util.Objects;

public class OnslaughtCommands {
    public void startOnslaught(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
    }

    public void stop() {

    }
}
