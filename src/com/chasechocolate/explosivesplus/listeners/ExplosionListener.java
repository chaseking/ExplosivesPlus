package com.chasechocolate.explosivesplus.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.chasechocolate.explosivesplus.ExplosivesPlus;
import com.chasechocolate.explosivesplus.explosive.ExplosionUtils;
import com.chasechocolate.explosivesplus.explosive.ExplosiveType;

public class ExplosionListener implements Listener {		
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		if(event.getEntity() != null){
			UUID id = event.getEntity().getUniqueId();
			
			if(ExplosivesPlus.getInstance().tntIds.containsKey(id)){
				ExplosiveType type = ExplosivesPlus.getInstance().tntIds.get(id);
				
				if(type == ExplosiveType.ROCKET_LAUNCHER){
					ExplosionUtils.performRocketLauncher(event);
				} else if(type == ExplosiveType.SINGULARITY_GRENADE){
					ExplosionUtils.performSingularityGrenade(event);
				} else if(type == ExplosiveType.CLUSTER_BOMB){
					ExplosionUtils.performClusterBomb(event);
				} else if(type == ExplosiveType.BLACK_HOLE){
					ExplosionUtils.performBlackHole(event);
				}
			}
		}
	}
}