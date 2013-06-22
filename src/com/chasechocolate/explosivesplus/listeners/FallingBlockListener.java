package com.chasechocolate.explosivesplus.listeners;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.explosivesplus.ExplosivesPlus;
import com.chasechocolate.explosivesplus.utils.Utilities;

public class FallingBlockListener implements Listener {
	@EventHandler
	public void onFallingBlockLand(final EntityChangeBlockEvent event){
		if(event.getEntity() instanceof FallingBlock){
			FallingBlock fallingBlock = (FallingBlock) event.getEntity();
			UUID id = fallingBlock.getUniqueId();
			
			if(event.getBlock().getType() == Material.AIR){
				if(ExplosivesPlus.getInstance().fallingBlocks.contains(id)){
					new BukkitRunnable(){
						@Override
						public void run(){
							Utilities.regenerateBlock(event.getBlock(), Material.AIR, (byte) 0);
						}
					}.runTaskLater(ExplosivesPlus.getInstance(), 1L);
				}
			}
		}
	}
}