package com.chasechocolate.explosivesplus.listeners;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.chasechocolate.explosivesplus.ExplosivesPlus;
import com.chasechocolate.explosivesplus.explosive.ExplosiveType;
import com.chasechocolate.explosivesplus.utils.ItemUtils;
import com.chasechocolate.explosivesplus.utils.Utilities;

public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		
		if(ItemUtils.isItem(hand, ExplosiveType.ROCKET_LAUNCHER)){
			SmallFireball fireball = player.launchProjectile(SmallFireball.class);
			
			fireball.setBounce(false);
			fireball.setFireTicks(0);
			fireball.setIsIncendiary(false);
			fireball.setYield(0.0F);
			
			UUID id = fireball.getUniqueId();
			Vector newVelocity = player.getLocation().getDirection().multiply(2.0D);
			
			ExplosivesPlus.getInstance().explosiveIds.put(id, ExplosiveType.ROCKET_LAUNCHER);
			fireball.setVelocity(newVelocity);
		} else if(ItemUtils.isItem(hand, ExplosiveType.SINGULARITY_GRENADE)){
			SmallFireball fireball = player.launchProjectile(SmallFireball.class);
			
			fireball.setBounce(false);
			fireball.setFireTicks(0);
			fireball.setIsIncendiary(false);
			fireball.setYield(0.0F);
			
			UUID id = fireball.getUniqueId();
			Vector newVelocity = player.getLocation().getDirection().multiply(2.0D);
			
			ExplosivesPlus.getInstance().explosiveIds.put(id, ExplosiveType.SINGULARITY_GRENADE);
			fireball.setVelocity(newVelocity);
		} else if(ItemUtils.isItem(hand, ExplosiveType.CLUSTER_BOMB)){
			final ItemStack toThrow = new ItemStack(Material.TNT);
			final Vector velocity = player.getLocation().getDirection().multiply(1.4D);
			final Item item = player.getWorld().dropItem(player.getLocation().add(0.0D, 1.5D, 0.0D), toThrow);
			
			item.setPickupDelay(Integer.MAX_VALUE);
			item.setVelocity(velocity);
			
			new BukkitRunnable(){
				@Override
				public void run(){
					Location loc = item.getLocation();
					Block below = loc.getBlock().getRelative(0, -1, 0);
					
					if(below.getType() != Material.AIR){
						loc.getWorld().createExplosion(loc, 0.0F);
						
						for(int i = 0; i < 4; i++){
							final Item splitGrenade = loc.getWorld().dropItem(loc, toThrow);
							
							splitGrenade.setPickupDelay(Integer.MAX_VALUE);
							splitGrenade.setVelocity(Utilities.getRandomVelocity());
							
							new BukkitRunnable(){
								@Override
								public void run(){
									TNTPrimed tnt = splitGrenade.getWorld().spawn(splitGrenade.getLocation(), TNTPrimed.class);
									UUID id = tnt.getUniqueId();
									
									tnt.setFuseTicks(1);
									ExplosivesPlus.getInstance().tntIds.put(id, ExplosiveType.CLUSTER_BOMB);
									splitGrenade.remove();
								}
							}.runTaskLater(ExplosivesPlus.getInstance(), 80L);
						}
						
						item.remove();
						this.cancel();
					}
				}
			}.runTaskTimer(ExplosivesPlus.getInstance(), 0L, 2L);
		} else if(ItemUtils.isItem(hand, ExplosiveType.BLACK_HOLE)){
			SmallFireball fireball = player.launchProjectile(SmallFireball.class);
			
			fireball.setBounce(false);
			fireball.setFireTicks(0);
			fireball.setIsIncendiary(false);
			fireball.setYield(0.0F);
			
			UUID id = fireball.getUniqueId();
			Vector newVelocity = player.getLocation().getDirection().multiply(2.0D);
			
			ExplosivesPlus.getInstance().explosiveIds.put(id, ExplosiveType.BLACK_HOLE);
			fireball.setVelocity(newVelocity);
		}
	}
}