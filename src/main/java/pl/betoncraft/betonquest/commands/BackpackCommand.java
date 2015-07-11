/**
 * BetonQuest - advanced quests for Bukkit
 * Copyright (C) 2015  Jakub "Co0sh" Sapalski
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.betoncraft.betonquest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.betoncraft.betonquest.BackpackDisplay;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.utils.Debug;
import pl.betoncraft.betonquest.utils.PlayerConverter;

/**
 * The journal command. It gives the player his journal book.
 * 
 * @author Jakub Sapalski
 */
public class BackpackCommand implements CommandExecutor {
    
    /**
     * Registers a new executor of the /backpack command
     */
    public BackpackCommand() {
        BetonQuest.getInstance().getCommand("backpack").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("backpack")) {
            // command sender must be a player, console can't have journal
            if (sender instanceof Player) {
                Debug.info("Executing /backpack command for " + sender.getName());
                new BackpackDisplay(PlayerConverter.getID((Player) sender));
            }
            return true;
        }
        return false;
    }
}
