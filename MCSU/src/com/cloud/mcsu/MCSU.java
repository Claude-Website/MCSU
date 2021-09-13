package com.cloud.mcsu;
import com.cloud.mcsu.commands.Commands;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.event.Event;
import com.cloud.mcsu.events.Events;
import com.cloud.mcsu.gui.GUI;
import com.cloud.mcsu.items.Items;
import com.cloud.mcsu.minigames.*;
import com.cloud.mcsu.worldreset.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MCSU extends JavaPlugin implements CommandExecutor {

    public static double spawnx = -73.5;
    public static double spawny = 4;
    public static double spawnz = 200.5;
    public static int bluepoints;
    public static int redpoints;
    public static int greenpoints;
    public static int yellowpoints;
    public static int whitepoints;
    public static int greypoints;
    public static int pinkpoints;
    public static int aquapoints;
    public static ScoreboardManager manager;
    public static Scoreboard board;
    public static Team blueteam;
    public static Team redteam;
    public static Team greenteam;
    public static Team yellowteam;
    public static Team aquateam;
    public static Team pinkteam;
    public static Team whiteteam;
    public static Team greyteam;
    public static Team tunarank;
    private static MCSU instance;
    public static String currentgame;
    public static int gameround;
    public static String timeleft;
    public static HashMap<Integer,String> placement = new HashMap<Integer,String>();
    public static HashMap<String, Integer> placements = new HashMap<String, Integer>();
    public static Player teamplayer;

    @Override
    public void onEnable() {
        getCommand("spawn").setExecutor(new Commands());
        getCommand("trophy").setExecutor(new Commands());
        getCommand("beeriding").setExecutor(new Commands());
        getCommand("cosmetic").setExecutor(new Commands());
        getCommand("heal").setExecutor(new Commands());
        getCommand("hub").setExecutor(new Commands());
        getCommand("gui").setExecutor(new Commands());
        getCommand("sg").setExecutor(new SG());
        getCommand("stopsg").setExecutor(new SG());
        getCommand("skybattle").setExecutor(new Skybattle());
        getCommand("stopskybattle").setExecutor(new Skybattle());
        getCommand("resetworld").setExecutor(new Commands());
        getCommand("mcsuteam").setExecutor(new Commands());
        getCommand("horserace").setExecutor(new HorseRace());
        getCommand("v").setExecutor(new Commands());
        getCommand("roll").setExecutor(new Commands());
        getCommand("buildingmap").setExecutor(new Commands());
        getCommand("ctf").setExecutor(new CaptureTheFlag());
        getCommand("slimekour").setExecutor(new Slimekour());
        getCommand("stopslimekour").setExecutor(new Slimekour());
        getCommand("hideandseek").setExecutor(new HideAndSeek());
        getCommand("stophideandseek").setExecutor(new HideAndSeek());
        getCommand("timer").setExecutor(new HideAndSeek());
        getCommand("taco").setExecutor(new Commands());
        getCommand("ace").setExecutor(new Commands());
        getCommand("gangbeasts").setExecutor(new GangBeasts());
        getCommand("stopgangbeasts").setExecutor(new GangBeasts());
        getCommand("stopctf").setExecutor(new CaptureTheFlag());
        getCommand("event").setExecutor(new Event());
        getCommand("resetpoints").setExecutor(new Commands());
        getCommand("nick").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new SG(), this);
        getServer().getPluginManager().registerEvents(new WorldManager(), this);
        getServer().getPluginManager().registerEvents(new CaptureTheFlag(), this);
        getServer().getPluginManager().registerEvents(new Slimekour(), this);
        getServer().getPluginManager().registerEvents(new HorseRace(), this);
        getServer().getPluginManager().registerEvents(new Skybattle(), this);
        getServer().getPluginManager().registerEvents(new GangBeasts(), this);
        getServer().getPluginManager().registerEvents(new HideAndSeek(),this);
        getServer().getPluginManager().registerEvents(new GUI(),this);
        // getServer().getPluginManager().registerEvents(new MiningMayhem(), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[MCSU]: Plugin is enabled!");
        Items.init();

        Config.setup();

        // Config
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("MCSU").getDataFolder(), "config.yml");

        if(file.exists()) {
            Config.setup();
            Config.get().options().copyDefaults(true);
            Config.save();
            bluepoints = Integer.parseInt(Config.get().getString("Points.BluePoints"));
            greenpoints = Integer.parseInt(Config.get().getString("Points.GreenPoints"));
            yellowpoints = Integer.parseInt(Config.get().getString("Points.YellowPoints"));
            redpoints = Integer.parseInt(Config.get().getString("Points.RedPoints"));
            aquapoints = Integer.parseInt(Config.get().getString("Points.AquaPoints"));
            pinkpoints = Integer.parseInt(Config.get().getString("Points.PinkPoints"));
            greypoints = Integer.parseInt(Config.get().getString("Points.GreyPoints"));
            whitepoints = Integer.parseInt(Config.get().getString("Points.WhitePoints"));
        }
        setInstance(this);
    }

    public static MCSU getInstance() {
        return instance;
    }

    private void setInstance(MCSU instance) {
        this.instance = instance;
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[MCSU]: Plugin is disabled!");
        Bukkit.getScheduler().cancelTasks(this);
    }

    public static void createAceyRank(Player player) {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("Ace", "dummy", "Ace+++");
        obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
        player.setScoreboard(board);
    }

    public static void getPoints() {
        Config.save();
        Config.get().options().copyDefaults(true);
        Config.reload();
        bluepoints = Integer.parseInt(Config.get().getString("Points.BluePoints"));
        redpoints = Integer.parseInt(Config.get().getString("Points.RedPoints"));
        greenpoints = Integer.parseInt(Config.get().getString("Points.GreenPoints"));
        yellowpoints = Integer.parseInt(Config.get().getString("Points.YellowPoints"));
        aquapoints = Integer.parseInt(Config.get().getString("Points.AquaPoints"));
        pinkpoints = Integer.parseInt(Config.get().getString("Points.PinkPoints"));
        greypoints = Integer.parseInt(Config.get().getString("Points.GreyPoints"));
        whitepoints = Integer.parseInt(Config.get().getString("Points.WhitePoints"));
        /*
        placements.put(ChatColor.GREEN+"Green Geckos: ",greenpoints);
        placements.put(ChatColor.YELLOW+"Yellow Yetis: ",yellowpoints);
        placements.put(ChatColor.RED+"Red Reindeers: ",redpoints);
        placements.put(ChatColor.BLUE+"Blue Bitches: ",bluepoints);
        placements.put(ChatColor.AQUA+"Aqua Axolotols: ",aquapoints);
        placements.put(ChatColor.LIGHT_PURPLE+"Pink Pandas: ",pinkpoints);
        placements.put(ChatColor.GRAY+"Grey Gorillas: ",greypoints);
        placements.put(ChatColor.WHITE+"White Walruses: ",whitepoints);

         */
        for(Player players : Bukkit.getOnlinePlayers()) {
            MCSU.createBoard(players);
        }
    }

    static class TeamPlace implements Comparator<TeamPlace>, Comparable<TeamPlace> {
        private String teamname;
        private int points;
        TeamPlace() {
        }

        TeamPlace(String n, int a) {
            teamname = n;
            points = a;
        }

        public String getTeamName() {
            return teamname;
        }

        public int getTeamPoints() {
            return points;
        }

        // Overriding the compareTo method
        public int compareTo(TeamPlace d) {
            return (this.teamname).compareTo(d.teamname);
        }

        // Overriding the compare method to sort the age
        public int compare(TeamPlace d, TeamPlace d1) {
            return d.points - d1.points;
        }
    }

    public static void createBoard(Player player) {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("MCSU","dummy","§a§lMinecraft Sundays");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score online = obj.getScore(ChatColor.AQUA+" Online: "+ChatColor.WHITE+Bukkit.getOnlinePlayers().size());
        online.setScore(13);
        Score game = obj.getScore(ChatColor.AQUA+" Game: "+ChatColor.WHITE+currentgame);
        game.setScore(12);
        Score round = obj.getScore(ChatColor.AQUA+" Round: "+ChatColor.WHITE+gameround);
        round.setScore(11);
        timeleft = "06:09";
        Score timer = obj.getScore(ChatColor.AQUA+" Time Left: "+ChatColor.WHITE+timeleft);
        timer.setScore(10);
        Score line9 = obj.getScore("  ");
        line9.setScore(9);
        Score topblank = obj.getScore(" ");
        topblank.setScore(14);
        Score bottomblank = obj.getScore("");
        bottomblank.setScore(0);
        player.setScoreboard(board);
        // Teams
        MCSU.blueteam = MCSU.board.registerNewTeam("Blue");
        MCSU.blueteam.setDisplayName(ChatColor.BLUE+"Blue Bears");
        MCSU.blueteam.setColor(ChatColor.BLUE);
        MCSU.blueteam.setPrefix(ChatColor.BLUE+"[Blue] ");
        MCSU.redteam = MCSU.board.registerNewTeam("Red");
        MCSU.redteam.setDisplayName(ChatColor.RED+"Red Reindeers");
        MCSU.redteam.setColor(ChatColor.RED);
        MCSU.redteam.setPrefix(ChatColor.RED+"[Red] ");
        MCSU.greenteam = MCSU.board.registerNewTeam("Green");
        MCSU.greenteam.setDisplayName(ChatColor.GREEN+"Green Gorillas");
        MCSU.greenteam.setColor(ChatColor.GREEN);
        MCSU.greenteam.setPrefix(ChatColor.GREEN+"[Green] ");
        MCSU.yellowteam = MCSU.board.registerNewTeam("Yellow");
        MCSU.yellowteam.setDisplayName(ChatColor.YELLOW+"Yellow Yetis");
        MCSU.yellowteam.setColor(ChatColor.YELLOW);
        MCSU.yellowteam.setPrefix(ChatColor.YELLOW+"[Yellow] ");
        MCSU.aquateam = MCSU.board.registerNewTeam("Aqua");
        MCSU.aquateam.setDisplayName(ChatColor.AQUA+"Aqua Axolotols");
        MCSU.aquateam.setColor(ChatColor.AQUA);
        MCSU.aquateam.setPrefix(ChatColor.AQUA+"[Aqua] ");
        MCSU.pinkteam = MCSU.board.registerNewTeam("Pink");
        MCSU.pinkteam.setDisplayName(ChatColor.LIGHT_PURPLE+"Pink Pandas");
        MCSU.pinkteam.setColor(ChatColor.LIGHT_PURPLE);
        MCSU.pinkteam.setPrefix(ChatColor.LIGHT_PURPLE+"[Pink] ");
        MCSU.greyteam = MCSU.board.registerNewTeam("Grey");
        MCSU.greyteam.setDisplayName(ChatColor.GRAY+"Grey Gorillas");
        MCSU.greyteam.setColor(ChatColor.GRAY);
        MCSU.greyteam.setPrefix(ChatColor.GRAY+"[Grey] ");
        MCSU.whiteteam = MCSU.board.registerNewTeam("White");
        MCSU.whiteteam.setDisplayName(ChatColor.WHITE+"White Walruses");
        MCSU.whiteteam.setColor(ChatColor.WHITE);
        MCSU.whiteteam.setPrefix(ChatColor.WHITE+"[White] ");

        bluepoints = Integer.parseInt(Config.get().getString("Points.BluePoints"));
        redpoints = Integer.parseInt(Config.get().getString("Points.RedPoints"));
        greenpoints = Integer.parseInt(Config.get().getString("Points.GreenPoints"));
        yellowpoints = Integer.parseInt(Config.get().getString("Points.YellowPoints"));
        aquapoints = Integer.parseInt(Config.get().getString("Points.AquaPoints"));
        pinkpoints = Integer.parseInt(Config.get().getString("Points.PinkPoints"));
        greypoints = Integer.parseInt(Config.get().getString("Points.GreyPoints"));
        whitepoints = Integer.parseInt(Config.get().getString("Points.WhitePoints"));

        placements = null;
        placements = new HashMap<String,Integer>();
        placements.put(greenteam.getName(),greenpoints);
        placements.put(yellowteam.getName(),yellowpoints);
        placements.put(redteam.getName(),redpoints);
        placements.put(blueteam.getName(),bluepoints);
        placements.put(aquateam.getName(),aquapoints);
        placements.put(pinkteam.getName(),pinkpoints);
        placements.put(greyteam.getName(),greypoints);
        placements.put(whiteteam.getName(),whitepoints);

        List<TeamPlace> teamplace = new ArrayList<TeamPlace>();
        teamplace.add(new TeamPlace(greenteam.getName(),greenpoints));
        teamplace.add(new TeamPlace(yellowteam.getName(),yellowpoints));
        teamplace.add(new TeamPlace(redteam.getName(),redpoints));
        teamplace.add(new TeamPlace(blueteam.getName(),bluepoints));
        teamplace.add(new TeamPlace(aquateam.getName(),aquapoints));
        teamplace.add(new TeamPlace(pinkteam.getName(),pinkpoints));
        teamplace.add(new TeamPlace(greyteam.getName(),greypoints));
        teamplace.add(new TeamPlace(whiteteam.getName(),whitepoints));
        Collections.sort(teamplace, new TeamPlace());
        int i = 0;
        int place = 9;
        for(TeamPlace a: teamplace) { // printing the sorted list of ages
            player.sendMessage(a.getTeamName() +"  : "+ a.getTeamPoints() + ", ");
            i++;
            place--;
            /*
            int place = 0;
            switch (i) {
                case 1:
                    place = 8;
                case 2:
                    place = 7;
                case 3:
                    place = 6;
                case 4:
                    place = 5;
                case 5:
                    place = 4;
                case 6:
                    place = 3;
                case 7:
                    place = 2;
                case 8:
                    place = 1;
            }

             */
            obj.getScore(ChatColor.WHITE+" "+place+". "+player.getScoreboard().getTeam(a.getTeamName()).getColor()+player.getScoreboard().getTeam(a.getTeamName()).getDisplayName()+": "+ChatColor.WHITE+a.getTeamPoints()).setScore(i);
        }

        /*
        Score point1 = obj.getScore(ChatColor.WHITE+" 1. "+ChatColor.WHITE+greenpoints+" ");
        point1.setScore(8);
        Score point2 = obj.getScore(ChatColor.WHITE+" 2. "+ChatColor.YELLOW+order.get(1).toString()+ChatColor.WHITE+yellowpoints+" ");
        point2.setScore(7);
        Score point3 = obj.getScore(ChatColor.WHITE+" 3. "+ChatColor.RED+order.get(2).toString()+ChatColor.WHITE+redpoints+" ");
        point3.setScore(6);
        Score point4 = obj.getScore(ChatColor.WHITE+" 4. "+ChatColor.BLUE+order.get(3).toString()+ChatColor.WHITE+bluepoints+" ");
        point4.setScore(5);
        Score point5 = obj.getScore(ChatColor.WHITE+" 5. "+ChatColor.AQUA+order.get(4).toString()+ChatColor.WHITE+aquapoints+" ");
        point5.setScore(4);
        Score point6 = obj.getScore(ChatColor.WHITE+" 6. "+ChatColor.LIGHT_PURPLE+order.get(5).toString()+ChatColor.WHITE+pinkpoints+" ");
        point6.setScore(3);
        Score point7 = obj.getScore(ChatColor.WHITE+" 7. "+ChatColor.GRAY+order.get(6).toString()+ChatColor.WHITE+greypoints+" ");
        point7.setScore(2);
        Score point8 = obj.getScore(ChatColor.WHITE+" 8. "+ChatColor.WHITE+order.get(7).toString()+ChatColor.WHITE+whitepoints+" ");
        point8.setScore(1);

         */

        // Adding Players
        MCSU.greenteam.addEntry("WaitWhosCandice");
        MCSU.aquateam.addEntry("axob");
        MCSU.blueteam.addEntry("AceyXS");
        MCSU.yellowteam.addEntry("robux_");
        MCSU.aquateam.addEntry("PogGamer");
        MCSU.greyteam.addEntry("Anonymous1252");
        MCSU.blueteam.addEntry("tunae");
        MCSU.whiteteam.addEntry("stanowar");
        MCSU.greenteam.addEntry("leaef");
        MCSU.redteam.addEntry("JackyWackers");
        MCSU.aquateam.addEntry("happygamer1977");
        MCSU.pinkteam.addEntry("CakeIsTasty");

        if(HideAndSeek.hideandseekStarted) {
            if(HideAndSeek.round == 1) {
                MCSU.blueteam.setSuffix(ChatColor.DARK_RED+" [Tagger]");
                MCSU.redteam.setSuffix(ChatColor.DARK_RED+" [Tagger]");
                MCSU.greenteam.setSuffix(ChatColor.DARK_RED+" [Tagger]");
                MCSU.yellowteam.setSuffix(ChatColor.DARK_RED+" [Tagger]");
                MCSU.aquateam.setSuffix(ChatColor.GREEN+" [Hider]");
                MCSU.pinkteam.setSuffix(ChatColor.GREEN+" [Hider]");
                MCSU.greyteam.setSuffix(ChatColor.GREEN+" [Hider]");
                MCSU.whiteteam.setSuffix(ChatColor.GREEN+" [Hider]");
            } else if(HideAndSeek.round == 2) {
                MCSU.blueteam.setSuffix(ChatColor.DARK_RED+" [Hider]");
                MCSU.redteam.setSuffix(ChatColor.DARK_RED+" [Hider]");
                MCSU.greenteam.setSuffix(ChatColor.DARK_RED+" [Hider]");
                MCSU.yellowteam.setSuffix(ChatColor.DARK_RED+" [Hider]");
                MCSU.aquateam.setSuffix(ChatColor.GREEN+" [Tagger]");
                MCSU.pinkteam.setSuffix(ChatColor.GREEN+" [Tagger]");
                MCSU.greyteam.setSuffix(ChatColor.GREEN+" [Tagger]");
                MCSU.whiteteam.setSuffix(ChatColor.GREEN+" [Tagger]");
            }
        }
    }

    public static Map<String, Integer> sortByValueDesc(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }   

    /*
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        Player player = (Player) sender;
        if (label.equalsIgnoreCase("joinred")) {
            Team.addPlayer(teame, player);
            player.sendMessage("You just joined the RED team!");
        }
        if (label.equalsIgnoreCase("leaveteam")) {
            if (Team.hasTeam(player)) {
                Team.removePlayer(player);
                player.sendMessage("You just left the team!");
            }
        }
        return true;
    }

     */
}