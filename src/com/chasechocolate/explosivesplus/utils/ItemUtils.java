package com.chasechocolate.explosivesplus.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.chasechocolate.explosivesplus.explosive.ExplosiveType;

public class ItemUtils {
	public static ItemStack ROCKET_LAUNCHER;
	public static ItemStack SINGULARITY_GRENADE;
	public static ItemStack CLUSTER_BUMB;
	public static ItemStack BLACK_HOLE;
	
	private static final String ROCKET_LAUNCHER_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "Rocket Launcher";
	private static final List<String> ROCKET_LAUNCHER_LORE = Arrays.asList(ChatColor.DARK_PURPLE + "Right click to fire a rocket.");
	
	private static final String SINGULARITY_GRENADE_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "Singularity Grenade";
	private static final List<String> SINGULARITY_GRENADE_LORE = Arrays.asList(ChatColor.DARK_PURPLE + "Right click to throw this,", ChatColor.DARK_PURPLE + "it will suck blocks into the center", ChatColor.DARK_PURPLE + "and then violently shoot them outwards.");
	
	private static final String CLUSTER_BUMB_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "Cluster Bomb";
	private static final List<String> CLUSTER_BUMB_LORE = Arrays.asList(ChatColor.DARK_PURPLE + "Right click to throw this, it will", ChatColor.DARK_PURPLE + "shoot a TNT drop where you are looking", ChatColor.DARK_PURPLE + "and then split into 4 pieces and", ChatColor.DARK_PURPLE + "explode soon after.");
	
	private static final String BLACK_HOLE_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "Black Hole";
	private static final List<String> BLACK_HOLE_LORE = Arrays.asList(ChatColor.DARK_PURPLE + "Right click to throw this,", ChatColor.DARK_PURPLE + "it will suck blocks into the center.");
	
	public static void initItems(){
		ROCKET_LAUNCHER = setNameAndLore(new ItemStack(Material.GOLD_HOE), ROCKET_LAUNCHER_NAME, ROCKET_LAUNCHER_LORE);
		SINGULARITY_GRENADE = setNameAndLore(new ItemStack(Material.INK_SACK, 8), SINGULARITY_GRENADE_NAME, SINGULARITY_GRENADE_LORE);
		CLUSTER_BUMB = setNameAndLore(new ItemStack(Material.TNT, 4), CLUSTER_BUMB_NAME, CLUSTER_BUMB_LORE);
		BLACK_HOLE = setNameAndLore(new ItemStack(Material.INK_SACK, 8), BLACK_HOLE_NAME, BLACK_HOLE_LORE);
	}
	
	public static ItemStack setNameAndLore(ItemStack item, String name, List<String> lore){
		ItemStack newItem = item.clone();
		ItemMeta meta = newItem.getItemMeta();
		
		meta.setDisplayName(name);
		
		if(lore != null){
			meta.setLore(lore);
		}
		
		newItem.setItemMeta(meta);
		
		return newItem;
	}
	
	public static boolean isItem(ItemStack item, ExplosiveType type){
		if(type == ExplosiveType.ROCKET_LAUNCHER){
			return item.isSimilar(ROCKET_LAUNCHER);
		} else if(type == ExplosiveType.SINGULARITY_GRENADE){
			return item.isSimilar(SINGULARITY_GRENADE);
		} else if(type == ExplosiveType.CLUSTER_BOMB){
			return item.isSimilar(CLUSTER_BUMB);
		} else if(type == ExplosiveType.BLACK_HOLE){
			return item.isSimilar(BLACK_HOLE);
		} else {
			return false;
		}
	}
}