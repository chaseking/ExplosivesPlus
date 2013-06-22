package com.chasechocolate.explosivesplus.listeners;

import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.chasechocolate.explosivesplus.ExplosivesPlus;
import com.chasechocolate.explosivesplus.explosive.ExplosiveType;

public class ProjectileHitListener implements Listener {
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		if(event.getEntity() instanceof SmallFireball){
			SmallFireball fireball = (SmallFireball) event.getEntity();
			UUID fireballId = fireball.getUniqueId();
			
			if(ExplosivesPlus.getInstance().explosiveIds.containsKey(fireballId)){
				ExplosiveType rocketType = ExplosivesPlus.getInstance().explosiveIds.get(fireballId);
				
				TNTPrimed tnt = (TNTPrimed) fireball.getWorld().spawnEntity(fireball.getLocation(), EntityType.PRIMED_TNT);
				UUID tntId = tnt.getUniqueId();
				
				fireball.remove();
				tnt.setFuseTicks(1);
				ExplosivesPlus.getInstance().tntIds.put(tntId, rocketType);
			}
		}
	}
}