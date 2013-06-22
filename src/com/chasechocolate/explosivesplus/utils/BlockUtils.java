package com.chasechocolate.explosivesplus.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.explosivesplus.ExplosivesPlus;

public class BlockUtils {
	public static void regenerateBlocks(Collection<Block> blocks, final Material type, final byte data, final int blocksPerTime, final long delay, Comparator<Block> comparator){
		final List<Block> orderedBlocks = new ArrayList<Block>();
		
		orderedBlocks.addAll(blocks);
		
		if(comparator != null){
			Collections.sort(orderedBlocks, comparator);
		}
		
		final int size = orderedBlocks.size();
		
		if(size > 0){
			new BukkitRunnable(){
				int index = size - 1;
				
				@Override
				public void run(){
					for(int i = 0; i < blocksPerTime; i++){
						if(index >= 0){
							final Block block = orderedBlocks.get(index);
							
							regenerateBlock(block, type, data);
							
							index -= 1;
						} else {
							this.cancel();
							return;
						}						
					}
				}
			}.runTaskTimer(ExplosivesPlus.getInstance(), 0L, delay);
		}
	}
	
	public static void regenerateBlocks(Collection<BlockState> blocks, final int blocksPerTime, final long delay, Comparator<BlockState> comparator){
		final List<BlockState> orderedBlocks = new ArrayList<BlockState>();
		
		orderedBlocks.addAll(blocks);
		
		if(comparator != null){
			Collections.sort(orderedBlocks, comparator);
		}
		
		final int size = orderedBlocks.size();
		
		if(size > 0){
			new BukkitRunnable(){
				int index = size - 1;
				
				@Override
				public void run(){
					for(int i = 0; i < blocksPerTime; i++){
						if(index >= 0){
							final BlockState state = orderedBlocks.get(index);
							
							regenerateBlock(state.getBlock(), state.getType(), state.getData().getData());
							
							index -= 1;
						} else {
							this.cancel();
							return;
						}						
					}
				}
			}.runTaskTimer(ExplosivesPlus.getInstance(), 0L, delay);
		}
	}
	
	public static void spawnFallingBlocks(Collection<Block> blocks, final long delay, Comparator<Block> comparator){
		final List<Block> orderedBlocks = new ArrayList<Block>();
		
		orderedBlocks.addAll(blocks);
		
		if(comparator != null){
			Collections.sort(orderedBlocks, comparator);
		}
		
		final int size = orderedBlocks.size();
		
		if(size > 0){
			new BukkitRunnable(){
				int index = 0;
				int yOff = getLowestBlockOnYAxis(orderedBlocks);
				
				@Override
				public void run(){
					for(int i = 0; i < getBlocksOnYAxis(orderedBlocks, yOff); i++){
						if(index < size){
							final Block block = orderedBlocks.get(index);
							final Location loc = block.getLocation();
							final Material type = block.getType();
							final byte data = block.getData();
							
							regenerateBlock(block, Material.AIR, (byte) 0);
							
							FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(loc, type, data);
							UUID id = fallingBlock.getUniqueId();
							
							fallingBlock.setDropItem(false);
							fallingBlock.setFireTicks(0);
							fallingBlock.setVelocity(Utilities.getRandomVelocity());
							ExplosivesPlus.getInstance().fallingBlocks.add(id);
							
							index += 1;
						} else {
							this.cancel();
							return;
						}						
					}
					
					yOff += 1;
				}
			}.runTaskTimer(ExplosivesPlus.getInstance(), 0L, delay);
		}
	}
	
	public static void regenerateBlock(Block block, final Material type, final byte data){
		final Location loc = block.getLocation();
		
		loc.getWorld().playEffect(loc, Effect.STEP_SOUND, (type == Material.AIR ? block.getType().getId() : type.getId()));
		block.setType(type);
		block.setData(data);
	}
	
	public static List<Location> circle(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY){
		List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        
        for(int x = cx - radius; x <= cx + radius; x++){
        	for (int z = cz - radius; z <= cz + radius; z++){
        		for(int y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++){
        			double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
        			
        			if(dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))){
        				Location l = new Location(loc.getWorld(), x, y + plusY, z);
        				circleblocks.add(l);
        			}
        		}
        	}
        }
 
        return circleblocks;
	}
	
	public static List<Block> getRelativeBlocks(Location center, int radius, Collection<Block> startBlocks, Set<Material> types){
		List<Block> relative = new ArrayList<Block>();
		
		for(int x = -radius; x < radius; x++){
			for(int y = -radius; y < radius; y++){
				for(int z = -radius; z < radius; z++){
					Block newBlock = center.getBlock().getRelative(x, y, z);
					
					if(types.contains(newBlock.getType())){
						if(newBlock.getLocation().distance(center) <= radius){
							relative.add(newBlock);
						}
					}
				}
			}
		}
		
		return relative;
	}
	
	public static List<Block> getNearbyBlocks(Location center, int radius){
		List<Location> locs = circle(center, radius, radius, true, true, 0);
		List<Block> blocks = new ArrayList<Block>();
		
		for(Location loc : locs){
			blocks.add(loc.getBlock());
		}
		
		return blocks;
	}
	
	private static int getBlocksOnYAxis(Collection<Block> blocks, int y){
		int num = 0;
		
		for(Block block : blocks){
			if(block.getY() == y){
				num += 1;
			}
		}
		
		return num;
	}
	
	private static int getLowestBlockOnYAxis(Collection<Block> blocks){
		int lowestY = 360;
		
		for(Block block : blocks){
			int y = block.getY();
			
			if(y < lowestY){
				lowestY = y;
			}
		}
		
		return lowestY;
	}
}