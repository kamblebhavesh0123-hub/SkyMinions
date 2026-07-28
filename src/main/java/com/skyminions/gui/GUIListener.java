        if (plainTitle.contains("Minion Shop")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            int slot = event.getSlot();

            // Buy Auto-Smelter (Slot 21)
            if (slot == 21) {
                if (player.getInventory().containsAtLeast(new ItemStack(Material.COBBLESTONE), 64)) {
                    player.getInventory().removeItem(new ItemStack(Material.COBBLESTONE, 64));
                    player.getInventory().addItem(new ItemStack(Material.FURNACE, 1));
                    player.sendMessage(Component.text("Purchased Auto-Smelter (Furnace)!", NamedTextColor.GREEN));
                } else {
                    player.sendMessage(Component.text("You need 64x Cobblestone to buy Auto-Smelter!", NamedTextColor.RED));
                }
                return;
            }

            // Buy Coal Fuel (Slot 22)
            if (slot == 22) {
                player.getInventory().addItem(new ItemStack(Material.COAL, 1));
                player.sendMessage(Component.text("Purchased Minion Fuel (Coal)!", NamedTextColor.GREEN));
                return;
            }

            // Buy Auto-Compactor (Slot 23)
            if (slot == 23) {
                if (player.getInventory().containsAtLeast(new ItemStack(Material.REDSTONE), 64)) {
                    player.getInventory().removeItem(new ItemStack(Material.REDSTONE, 64));
                    player.getInventory().addItem(new ItemStack(Material.PISTON, 1));
                    player.sendMessage(Component.text("Purchased Auto-Compactor (Piston)!", NamedTextColor.GREEN));
                } else {
                    player.sendMessage(Component.text("You need 64x Redstone to buy Auto-Compactor!", NamedTextColor.RED));
                }
                return;
            }

            for (MinionConfig config : plugin.getConfigManager().getAllConfigs().values()) {
                if (config.getShopSlot() == slot) {
                    buyMinion(player, config.getType());
                    return;
                }
            }
            return;
        }
