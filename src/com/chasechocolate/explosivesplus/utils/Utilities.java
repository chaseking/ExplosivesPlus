package com.chasechocolate.explosivesplus.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.chasechocolate.explosivesplus.ExplosivesPlus;

public class Utilities {
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
							fallingBlock.setVelocity(getRandomVelocity());
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
	
	public static int randomBetween(int min, int max){
		int range = max - min;
		return min + (int) (Math.random() * (range));
	}
	
	public static Vector getRandomVelocity(){
		Random random = new Random();
		final double power = 0.3D;
		double rix = random.nextBoolean() ? -power : power;
		double riz = random.nextBoolean() ? -power : power;
		double x = random.nextBoolean() ? (rix * (0.25D + (random.nextInt(3) / 5))) : 0.0D;
		double y = 0.6D + (random.nextInt(2) / 4.5D);
		double z = random.nextBoolean() ? (riz * (0.25D + (random.nextInt(3) / 5))) : 0.0D;
		Vector velocity = new Vector(x, y, z);
		
		return velocity;
	}
}