package com.ebicep.warlordspartymanager.commands;

import com.ebicep.chatutils.ChatUtils;
import com.ebicep.warlordspartymanager.WarlordsPartyManager;
import com.ebicep.warlordspartymanager.party.Party;
import com.ebicep.warlordsqueuemanager.WarlordsBotManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class StreamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("warlords.party.stream")) {
            sender.sendMessage("§cYou do not have permission to do that.");
            return true;
        }

        if (WarlordsPartyManager.inAParty(((Player) sender).getUniqueId())) {
            ChatUtils.sendMessageToPlayer((Player) sender, ChatColor.RED + "You are already in a party", ChatColor.BLUE, true);
            return true;
        }

        if (args.length == 0) {
            Player player = (Player) sender;
            Party party = new Party(player.getUniqueId(), true);
            WarlordsPartyManager.getParties().add(party);
            party.sendMessageToAllPartyPlayers(ChatColor.GREEN + "You created a public party! Players can join with\n" +
                            ChatColor.GOLD + ChatColor.BOLD + "/party join " + sender.getName(),
                    ChatColor.BLUE, true);
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getUniqueId() != player.getUniqueId())
                    .forEach(onlinePlayer -> {
                        ChatUtils.sendCenteredMessage(onlinePlayer, ChatColor.BLUE.toString() + ChatColor.BOLD + "------------------------------------------");
                        ChatUtils.sendCenteredMessage(onlinePlayer, ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " created a public party!");
                        TextComponent message = new TextComponent(ChatColor.GOLD.toString() + ChatColor.BOLD + "Click here to join!");
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + player.getName()));
                        ChatUtils.sendCenteredMessageWithEvents(onlinePlayer, Collections.singletonList(message));
                        ChatUtils.sendCenteredMessage(onlinePlayer, ChatColor.BLUE.toString() + ChatColor.BOLD + "------------------------------------------");
                    });
            WarlordsBotManager.sendMessageToNotificationChannel("[PARTY] **" + player.getName() + "** created a public party! /p join " + player.getName(), true, false);
        }

        return true;
    }

    public void register(WarlordsPartyManager instance) {
        instance.getCommand("stream").setExecutor(this);
    }

}
