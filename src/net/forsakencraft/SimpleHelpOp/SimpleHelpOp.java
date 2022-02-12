package net.forsakencraft.SimpleHelpOp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleHelpOp extends JavaPlugin {
	private static ArrayList<String> playerName = new ArrayList<>();
	private static ArrayList<String> playerMsg = new ArrayList<>();
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
	}
	
	/* To-Do
	 * Add /helpop ? help menu
	 * Re-program to be object-oriented
	 */

	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args ) {
		if( sender instanceof Player ) {
	        if( cmd.getName().equalsIgnoreCase( "helpop" ) ) {
	        	if( sender.hasPermission( "simple.helpop.send" ) ) {
		        	Player player = (Player) sender;
		        	if( args.length == 0 )
		        		player.sendMessage( ChatColor.RED + "Error: Proper usage /helpop <Message>" );
		        	// PLAYER COMMAND
		        	else {
		        		StringBuilder str = new StringBuilder();
		    			for( int i = 0; i < args.length; i++ )
		    				str.append(args[i] + " " );
		    			
		    			player.sendMessage( ChatColor.translateAlternateColorCodes('&', getConfig().getString("SuccessfullySentToAdminMsg") ));
		    			playerName.add(player.getName());
		    			playerMsg.add(str.toString().trim());
			        	for( Player target : Bukkit.getOnlinePlayers() ) {
			        		if( target.hasPermission( "simple.helpop.admin" ) )
			        			target.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("FormatMsgToAdmins").replace("%player%", player.getName()).replace("%msg%", str.toString().trim() )));
			        	}
		        	}
	        	}
	        	else
	        		sender.sendMessage( ChatColor.translateAlternateColorCodes('&', getConfig().getString("NoPermissions") ));
	        }
	        if( cmd.getName().equalsIgnoreCase("helpopadmin") ) {
	        	if( sender.hasPermission("simple.helpop.admin")) {
		        	Player player = (Player) sender;
		        	if( args.length == 0 ) {
		        		player.sendMessage( ChatColor.RED + "Error: Proper usage /hoa <Player> <Message>" );
		        		return true;
		        	}
		        	// RELOAD
	        		if( args[0].equalsIgnoreCase("reload") ) {
	    				if(!player.hasPermission("simple.helpop.reload")) {
	    					player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NoPermission")));
	    					return true;
	    				}
	    				player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("ConfigReloaded")));
	    				saveConfig();
	    				reloadConfig();
	    				return true;
	        		}
	        		// HISTORY
	        		if( args[0].equalsIgnoreCase("history") ) {
	        			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("HelpOpHistoryHeader")));
	        			for( int i = 0; i < getConfig().getInt("NumberOfHelpOpRequest") && i < playerMsg.size(); i++ ) {
	        				player.sendMessage( ChatColor.GOLD + "[From] " + playerName.get(playerName.size() - (i+1) ) +
	        									ChatColor.GREEN + " [Msg]: " + ChatColor.RED + playerMsg.get(playerName.size() - (i+1) ));
	        			}
	        			return true;
	        		}
	        		// TARGET
		        	Player target = Bukkit.getServer().getPlayer(args[0]);
	        		if( target == null ) {
	        			player.sendMessage( ChatColor.RED + "Error: Player is offline!" );
	        			return true;
	        		}
		        	else {
		        		StringBuilder str = new StringBuilder();
		    			for( int i = 1; i < args.length; i++ )
		    				str.append(args[i] + " " );
		    			
		    			player.sendMessage( ChatColor.translateAlternateColorCodes('&', getConfig().getString("ConfirmMsgSentToAdmin")).replace("%player%", target.getName()) );
		    			target.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("AdminToPlayerPrefix"))
		    					+ ChatColor.translateAlternateColorCodes('&', getConfig().getString("AdminToPlayerMsgColor") ) + str.toString().trim());
		        	}
	        	}
	        	else
	        		sender.sendMessage( ChatColor.translateAlternateColorCodes('&', getConfig().getString("NoPermissions")) );
	        }
		}
		else {
			sender.sendMessage( ChatColor.RED + "Error: Cannot send this command from the console!" );
		}
		return false;
	}
}
