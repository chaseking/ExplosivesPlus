package com.chasechocolate.explosivesplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.chasechocolate.explosivesplus.explosive.ExplosiveType;
import com.chasechocolate.explosivesplus.listeners.ExplosionListener;
import com.chasechocolate.explosivesplus.listeners.FallingBlockListener;
import com.chasechocolate.explosivesplus.listeners.PlayerInteractListener;
import com.chasechocolate.explosivesplus.listeners.ProjectileHitListener;
import com.chasechocolate.explosivesplus.utils.ItemUtils;

public class ExplosivesPlus extends JavaPlugin {
	private static ExplosivesPlus instance;

	public List<UUID> fallingBlocks = new ArrayList<UUID>();
	
	public HashMap<UUID, ExplosiveType> explosiveIds = new HashMap<UUID, ExplosiveType>();
	public HashMap<UUID, ExplosiveType> tntIds = new HashMap<UUID, ExplosiveType>();
	
	public void log(String msg){		
		this.getLogger().info(msg);
	}
	
	@Override
	public void onEnable(){
		instance = this;
		
		ItemUtils.initItems();
		
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new ProjectileHitListener(), this);
		pm.registerEvents(new ExplosionListener(), this);
		pm.registerEvents(new FallingBlockListener(), this);
		
		ShapedRecipe rocketLauncherRecipe = new ShapedRecipe(ItemUtils.ROCKET_LAUNCHER);
		ShapedRecipe singularityGrenadeRecipe = new ShapedRecipe(ItemUtils.SINGULARITY_GRENADE);
		ShapedRecipe clusterBombRecipe = new ShapedRecipe(ItemUtils.CLUSTER_BUMB);
		
		rocketLauncherRecipe.shape("tgg", "ts ", " s ");
		rocketLauncherRecipe.setIngredient('t', Material.TNT);
		rocketLauncherRecipe.setIngredient('g', Material.GOLD_INGOT);
		rocketLauncherRecipe.setIngredient('s', Material.STICK);
				
		singularityGrenadeRecipe.shape("rtr", "trt", "rtr");
		singularityGrenadeRecipe.setIngredient('r', Material.REDSTONE_TORCH_ON);
		singularityGrenadeRecipe.setIngredient('t', Material.TNT);
				
		clusterBombRecipe.shape("tit", "iti", "tit");
		clusterBombRecipe.setIngredient('t', Material.TNT);
		clusterBombRecipe.setIngredient('i', Material.IRON_INGOT);
		
		this.getServer().addRecipe(rocketLauncherRecipe);
		this.getServer().addRecipe(singularityGrenadeRecipe);
		this.getServer().addRecipe(clusterBombRecipe);
		
		log("Enabled!");
	}
	
	public static ExplosivesPlus getInstance(){
		return instance;
	}
}