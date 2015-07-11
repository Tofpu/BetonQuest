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
package pl.betoncraft.betonquest.conditions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.api.Condition;

/**
 * Checks block at specified location against specified Material
 * 
 * @author Jakub Sapalski
 */
public class TestForBlockCondition extends Condition {

    private final Block block;
    private final Material material;
    
    public TestForBlockCondition(String packName, String instructions)
            throws InstructionParseException {
        super(packName, instructions);
        String[] parts = instructions.split(" ");
        if (parts.length < 3) {
            throw new InstructionParseException("Not enough arguments");
        }
        String[] location = parts[1].split(";");
        if (location.length != 4) {
            throw new InstructionParseException("Wrong location format");
        }
        double y = 0, x = 0, z = 0;
        try {
            x = Double.parseDouble(location[0]);
            y = Double.parseDouble(location[1]);
            z = Double.parseDouble(location[2]);
        } catch (NumberFormatException e) {
            throw new InstructionParseException("Cannot parse coordinates");
        }
        World world = Bukkit.getWorld(location[3]);
        if (world == null) {
            throw new InstructionParseException("World does not exist");
        }
        block = new Location(world, x, y, z).getBlock();
        if (block == null) {
            throw new InstructionParseException("Error with the block");
        }
        material = Material.matchMaterial(parts[2]);
        if (material == null) {
            throw new InstructionParseException("Undefined material type");
        }
    }

    @Override
    public boolean check(String playerID) {
        return block.getType().equals(material);
    }

}
