package com.ebicep.warlordspartymanager;

import com.ebicep.warlordspartymanager.commands.PartyCommand;
import com.ebicep.warlordspartymanager.commands.StreamCommand;
import com.ebicep.warlordspartymanager.listeners.PartyListener;
import com.ebicep.warlordspartymanager.party.Party;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WarlordsPartyManager extends JavaPlugin {

    private static WarlordsPartyManager warlordsPartyManager;
    private static final List<Party> parties = new ArrayList<>();

    @Override
    public void onEnable() {
        warlordsPartyManager = this;

        new PartyCommand().register(this);
        new StreamCommand().register(this);
        getServer().getPluginManager().registerEvents(new PartyListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static WarlordsPartyManager getWarlordsPartyManager() {
        return warlordsPartyManager;
    }

    public static List<Party> getParties() {
        return parties;
    }

    public static void disbandParty(Party party) {
        parties.remove(party);
    }

    public static Optional<Party> getPartyFromLeader(UUID uuid) {
        return parties.stream().filter(party -> party.getPartyLeader().getUuid().equals(uuid)).findFirst();
    }

    public static Optional<Party> getPartyFromAny(UUID uuid) {
        return parties.stream().filter(party -> party.getPartyPlayers().stream().anyMatch(partyPlayer -> partyPlayer.getUuid().equals(uuid))).findFirst();
    }

    public static boolean inAParty(UUID uuid) {
        return parties.stream().anyMatch(party -> party.hasUUID(uuid));
    }

    public static boolean inSameParty(UUID uuid1, UUID uuid2) {
        return parties.stream().anyMatch(party -> party.hasUUID(uuid1) && party.hasUUID(uuid2));
    }

}

