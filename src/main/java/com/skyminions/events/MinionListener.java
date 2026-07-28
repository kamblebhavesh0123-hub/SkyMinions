package com.skyminions.events;

import com.skyminions.SkyMinionsPlugin;
import com.skyminions.minions.MinionEntity;
import com.skyminions.models.Minion;
import com.skyminions.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MinionListener implements Listener {

    private final SkyMinionsPlugin plugin;

    public MinionListener(SkyMinionsPlugin plugin) {
        this.plugin = plugin;
    }

    // Prevent players from pulling items off the Minion Armor Stand
    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (event.getRightClicked().getCustomName() != null) {
            String name = PlainTextComponentSerializer.plainText().serialize(event.getRightClicked().customName());
            if (name.contains("Minion")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getType() != Material.PLAYER_HEAD || !item.hasItemMeta()) return;

        String displayName = PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName());
        if (!displayName.contains("Minion")) return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        // Extract Minion Type from Item Display Name (e.g. "DIAMOND Minion I" -> "DIAMOND")
        String type = "COBBLESTONE";
        String cleanName = displayName.replace(" Minion", "").trim();
        String[] parts = cleanName.split(" ");
        if (parts.length > 0) {
            type = parts[0].toUpperCase();
        }

        Location loc = event.getBlockPlaced().getLocation().add(0.5, 0, 0.5);
        loc.setYaw(player.getLocation().getYaw() + 180); // Face player

        Minion minion = new Minion(UUID.randomUUID(), player.getUniqueId(), type, 1, loc);
        plugin.getMinionManager().addMinion(minion);

        MinionEntity.spawnMinionStand(minion);

        // Consume held minion head item
        item.setAmount(item.getAmount() - 1);
        player.sendMessage(Component.text("Placed " + type + " Minion Lv.1!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand stand)) return;
        if (stand.getCustomName() == null) return;

        String name = PlainTextComponentSerializer.plainText().serialize(stand.customName());
        if (!name.contains("Minion")) return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        Minion target = null;
        for (Minion minion : plugin.getMinionManager().getAllMinions()) {
            if (minion.getLocation() != null && stand.getLocation().distanceSquared(minion.getLocation()) < 4) {
                target = minion;
                break;
            }
        }

        if (target != null) {
            plugin.getGuiManager().openMainMenu(player, target);
        }
    }
            }
            
