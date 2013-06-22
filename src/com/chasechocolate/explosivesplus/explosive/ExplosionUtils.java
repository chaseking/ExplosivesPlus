package com.chasechocolate.explosivesplus.explosive;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.explosivesplus.ExplosivesPlus;
import com.chasechocolate.explosivesplus.utils.BlockUtils;
import com.chasechocolate.explosivesplus.utils.EntityUtils;
import com.chasechocolate.explosivesplus.utils.Utilities;

public class ExplosionUtils {
	private static final int ROCKET_LAUNCHER_RADIUS = 12; 
	private static final int SINGULARITY_GRENADE_RADIUS = 8;
	private static final int BLACK_HOLE_RADIUS = 8;
	
	private static final long REGEN_DELAY = 120L;
	
	public static void performRocketLauncher(EntityExplodeEvent event){
		event.setYield(0.0F);
		
		final Location center = event.getLocation();
		final Set<Material> typesToDestroy = new HashSet<Material>();
		final List<BlockState> oldBlocks = new ArrayList<BlockState>();
		final List<Block> toRemove = new ArrayList<Block>();
		
		for(Block block : event.blockList()){
			typesToDestroy.add(block.getType());
			toRemove.add(block);
		}
		
		typesToDestroy.remove(Material.AIR);
		typesToDestroy.remove(Material.GRASS);
		typesToDestroy.remove(Material.DIRT);
		toRemove.addAll(BlockUtils.getRelativeBlocks(center, ROCKET_LAUNCHER_RADIUS, event.blockList(), typesToDestroy));
		event.blockList().clear();
		
		for(Block block : toRemove){
			oldBlocks.add(block.getState());
		}
		
		Utilities.spawnFallingBlocks(toRemove, 5L, new Comparator<Block>(){
			@Override
			public int compare(Block block1, Block block2){
				return Double.compare(block1.getY(), block2.getY());
			}
		});
		
		performWorldRegen(oldBlocks, center, 12, REGEN_DELAY);
	}
	
	public static void performSingularityGrenade(EntityExplodeEvent event){
		event.setYield(0.0F);
		event.blockList().clear();
		
		final Location center = event.getLocation();
		final double speed = 0.6D;
		final long changeVelocityDelay = 2L;
		final long moveBlocksDelay = 30L;
		
		final List<BlockState> oldBlocks = new ArrayList<BlockState>();
		final List<FallingBlock> fallingBlocks = new ArrayList<FallingBlock>();
		
		new BukkitRunnable(){
			int radius = 1;
			
			@Override
			public void run(){
				if(radius <= SINGULARITY_GRENADE_RADIUS){
					for(Block block : BlockUtils.getNearbyBlocks(center, radius)){
						if(block.getType() != Material.AIR){
							final Location loc = block.getLocation();
							final Material type = block.getType();
							final byte data = block.getData();
							
							oldBlocks.add(block.getState());
							block.setType(Material.AIR);
							
							FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(loc, type, data);
							
							fallingBlock.setDropItem(false);
							fallingBlock.setFireTicks(0);
							
							ExplosivesPlus.getInstance().fallingBlocks.add(fallingBlock.getUniqueId());
							fallingBlocks.add(fallingBlock);
							EntityUtils.moveToward(fallingBlock, center, speed);
						}
					}
					
					radius += 1;
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(ExplosivesPlus.getInstance(), 20L, moveBlocksDelay);
		
		new BukkitRunnable(){
			int timesRun = 0;
			
			@Override
			public void run(){
				if(timesRun < SINGULARITY_GRENADE_RADIUS * moveBlocksDelay * 108){
					for(FallingBlock fallingBlock : fallingBlocks){
						EntityUtils.moveToward(fallingBlock, center, speed);
						
						timesRun += 1;
					}
				} else {
					for(FallingBlock fallingBlock : fallingBlocks){
						fallingBlock.setVelocity(Utilities.getRandomVelocity());
					}
					
					performWorldRegen(oldBlocks, center, 12, REGEN_DELAY);
					this.cancel();
				}
			}
		}.runTaskTimer(ExplosivesPlus.getInstance(), 20L, changeVelocityDelay);
	}
	
	public static void performClusterBomb(EntityExplodeEvent event){
		event.setYield(0.0F);
		
		final Location center = event.getLocation();
		final List<BlockState> oldBlocks = new ArrayList<BlockState>();
		final List<Block> toRemove = new ArrayList<Block>();
		
		for(Block block : event.blockList()){
			toRemove.add(block);
		}
		
		event.blockList().clear();
		
		for(Block block : toRemove){
			oldBlocks.add(block.getState());
		}
		
		Utilities.spawnFallingBlocks(toRemove, 5L, new Comparator<Block>(){
			@Override
			public int compare(Block block1, Block block2){
				return Double.compare(block1.getY(), block2.getY());
			}
		});
		
		performWorldRegen(oldBlocks, center, 12, REGEN_DELAY);
	}
	
	public static void performBlackHole(EntityExplodeEvent event){
		event.setYield(0.0F);
		event.blockList().clear();
		
		final Location center = event.getLocation();
		final long moveBlocksDelay = 30L;
		
		final List<BlockState> oldBlocks = new ArrayList<BlockState>();
		
		new BukkitRunnable(){
			int radius = 1;
			
			@Override
			public void run(){
				if(radius <= BLACK_HOLE_RADIUS){
					for(Block block : BlockUtils.getNearbyBlocks(center, radius)){
						if(block.getType() != Material.AIR){							
							oldBlocks.add(block.getState());
							block.setType(Material.AIR);
						}
					}
					
					radius += 1;
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(ExplosivesPlus.getInstance(), 20L, moveBlocksDelay);
		
		performWorldRegen(oldBlocks, center, 12, REGEN_DELAY * (BLACK_HOLE_RADIUS / 2));
	}
	
	public static void performWorldRegen(final List<BlockState> blocks, final Location center, final int blocksPerTime, long delay){
		new BukkitRunnable(){
			@Override
			public void run(){
				BlockUtils.regenerateBlocks(blocks, blocksPerTime, 12, new Comparator<BlockState>(){
					@Override
					public int compare(BlockState state1, BlockState state2){
						return Double.compare(state1.getLocation().distance(center), state2.getLocation().distance(center));
					}
				});
			}
		}.runTaskLater(ExplosivesPlus.getInstance(), delay);
	}
}