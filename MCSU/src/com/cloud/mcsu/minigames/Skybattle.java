package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.worldreset.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.io.IOException;

public class Skybattle implements CommandExecutor, Listener {

    public static Player player;
    public static World world;
    public static int time;
    public static int taskID;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static double borderY;
    public static boolean skybattleStarted;
    public static boolean borderStarted = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("skybattle") && player.isOp()) {
            skybattleCommand(player);
        }
        if(cmd.getName().equalsIgnoreCase("stopskybattle") && player.isOp()) {
            stopSkybattle();
        }
        return true;
    }

    public static void skybattleCommand(Player p) {
        player = p;
        world = player.getWorld();
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            stopTimer();
        }
        skybattleStarted = false;
        world.getWorldBorder().setSize(10000,1);
        try {
            WorldManager.resetSkybattle(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderY = 120;
        setTimer(15);
        startTimer();
    }

    public static void stopSkybattle() {
        stopTimer();
        borderY = 120;
        skybattleStarted = false;
        world.getWorldBorder().setSize(10000,1);
    }

    public static void setTimer(int amount) {
        time = amount;
    }

    public static void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                if(time == 0) {
                    if(!skybattleStarted) {
                        borderStarted = false;
                        skybattleStart();
                        setTimer(150);
                        world.getWorldBorder().setCenter(1347,1525);
                        world.getWorldBorder().setSize(160,1);
                        startTimer();
                    } else {
                        if(!borderStarted) {
                            borderStarted = true;
                            Bukkit.broadcastMessage(ChatColor.RED+"Border shrinking!");
                            world.getWorldBorder().setSize(10,90);
                        }
                        if(borderStarted) {
                            borderY = borderY - 0.3;
                            player.sendMessage(""+borderY);
                            for(Player players : Bukkit.getOnlinePlayers()) {
                                if(players.getLocation().getY() >= borderY) {
                                    players.damage(1);
                                }
                                Location corner1 = new Location(world,1243,borderY,1424);
                                Location corner2 = new Location(world,1473,borderY,1643);
                                int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
                                int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
                                int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
                                int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
                                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.ORANGE, 8);
                                for (int x = minX; x <= maxX; x = x + 6) {
                                    for (int z = minZ; z <= maxZ; z = z + 6) {
                                        world.spawnParticle(Particle.REDSTONE, new Location(world, x, borderY, z),1, dustOptions);
                                    }
                                }
                            }
                        }
                    }
                    return;
                }
                if(time % 1 == 0 && !skybattleStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time == 15 && !skybattleStarted) {
                    world.setPVP(false);
                    MCSU.currentgame = "Skybattle";
                    MCSU.gameround = 1;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Skybattle starting in "+time+" seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA+"Skybattle Starting in "+time+" seconds!");
                        players.teleport(new Location(world,1347,162,1525));
                        players.playSound(players.getLocation(), Sound.BLOCK_ANVIL_PLACE,1,1);
                        MCSU.createBoard(players);
                    }
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        world.setPVP(false);
                        players.setGameMode(GameMode.ADVENTURE);
                        players.getInventory().clear();
                        players.setFoodLevel(20);
                        players.setHealth(20);
                        players.setFireTicks(1);
                        players.setLevel(0);
                        players.setExp(0);
                        for (PotionEffect effect : players.getActivePotionEffects()) {
                            players.removePotionEffect(effect.getType());
                        }
                    }
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public static void skybattleStart() {
        world.setPVP(true);
        stopTimer();
        skybattleStarted = true;
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendTitle("Go!", "", 1, 20, 1);
            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,5);
            players.setGameMode(GameMode.SURVIVAL);
            players.getInventory().clear();
            players.setFoodLevel(20);
            players.setHealth(20);
            players.setFireTicks(1);
            players.setLevel(0);
            players.setExp(0);
            for (PotionEffect effect : players.getActivePotionEffects()) {
                players.removePotionEffect(effect.getType());
            }
            Team team = players.getScoreboard().getPlayerTeam(players);
            String teamname = team.getName();
            if(teamname.equals("Blue")) {
                players.teleport(new Location(world,1347.5,67,1594.5));
                players.getInventory().setItemInOffHand(new ItemStack(Material.BLUE_CONCRETE,64));
            }
            if(teamname.equals("Pink")) {
                players.teleport(new Location(world,1300.5,66,1573.5));
                players.getInventory().setItemInOffHand(new ItemStack(Material.PINK_CONCRETE,64));
            }
            if(teamname.equals("Yellow")) {
                players.teleport(new Location(world,1280.5,67,1525.5));
                players.getInventory().setItemInOffHand(new ItemStack(Material.YELLOW_CONCRETE,64));
            }
            if(teamname.equals("Green")) {
                players.teleport(new Location(world,1347.5,67,1458.5));
                players.getInventory().setItemInOffHand(new ItemStack(Material.GREEN_CONCRETE,64));
            }
            if(teamname.equals("Red")) {
                players.teleport(new Location(world,1415.5,67,1526.5));
                players.getInventory().setItemInOffHand(new ItemStack(Material.RED_CONCRETE,64));
            }
            if(teamname.equals("Grey")) {
                players.teleport(new Location(world,1394.5,66,1573.5));
                players.getInventory().setItemInOffHand(new ItemStack(Material.GRAY_CONCRETE,64));
            }
            if(teamname.equals("Aqua")) {
                players.teleport(new Location(world,1301.5,66,1478.5));
                players.getInventory().setItemInOffHand(new ItemStack(Material.LIGHT_BLUE_CONCRETE,64));
            }
            if(teamname.equals("White")) {
                players.teleport(new Location(world,1393.5,66,1479.5));
                players.getInventory().setItemInOffHand(new ItemStack(Material.WHITE_CONCRETE,64));
            }
            players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
            ItemStack ironpick = new ItemStack(Material.IRON_PICKAXE,1);
            ironpick.addEnchantment(Enchantment.DIG_SPEED,2);
            players.getInventory().setItem(1,ironpick);
            players.getInventory().setItem(2,new ItemStack(Material.COOKED_BEEF,8));
            players.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE,1));
        }
    }

    @EventHandler
    public static void onKillPoint(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        Team team = killer.getScoreboard().getPlayerTeam(killer);
        String teamname = team.getName();
        if(skybattleStarted) {
            if(teamname.equals("Blue")) {
                int points = MCSU.bluepoints + 30;
                Config.get().set("Points.BluePoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"30"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Pink")) {
                int points = MCSU.pinkpoints + 30;
                Config.get().set("Points.PinkPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"30"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Yellow")) {
                int points = MCSU.yellowpoints + 30;
                Config.get().set("Points.YellowPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"30"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Green")) {
                int points = MCSU.greenpoints + 30;
                Config.get().set("Points.GreenPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"30"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Red")) {
                int points = MCSU.redpoints + 30;
                Config.get().set("Points.RedPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"30"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Grey")) {
                int points = MCSU.greypoints + 30;
                Config.get().set("Points.GreyPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"30"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Aqua")) {
                int points = MCSU.aquapoints + 30;
                Config.get().set("Points.AquaPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"30"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("White")) {
                int points = MCSU.whitepoints + 30;
                Config.get().set("Points.WhitePoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"30"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
        }
    }

}