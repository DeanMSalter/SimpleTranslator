package McEssence.SimpleTranslator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Locale;


public class Commands implements CommandExecutor {
    private final Config config;
    private final Main main;
    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SimpleTranslator");
    public Commands(Config configTemp, Main mainTemp){
        config = configTemp;
        main = mainTemp;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args == null || args[0] == null) {
            return false;
        }
        switch(args[0].toUpperCase()) {
            case "RELOAD":
                reload(commandSender, command, label, args);
                break;
            case "MYLANGUAGE":
                myLanguage(commandSender, command, label, args);
                break;
            default:
                break;
        }
        return true;
    }

    private Boolean reload(CommandSender commandSender, Command command, String s, String[] args){
        plugin.reloadConfig();
        commandSender.sendMessage("Reload Complete");
        return true;
    }
    private Boolean myLanguage(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if (args.length <= 1 || args[1] == null) {
            player.sendMessage("No language provided");
            return true;
        }
        String languageRequest = args[1];
        String languageCode = getLanguageCode(languageRequest);
        if (languageCode == null) {
            player.sendMessage("language was not valid");
            return true;
        }

        File preferencesFile = new File(main.getDataFolder(), "preferences.yml");
        FileConfiguration preferencesConfig = YamlConfiguration.loadConfiguration(preferencesFile);

        ConfigurationSection playerSection = preferencesConfig.getConfigurationSection(String.valueOf(player.getUniqueId()));
        if (playerSection == null) {
            ConfigurationSection newSection = preferencesConfig.createSection(String.valueOf(player.getUniqueId()));
            newSection.set("myLanguage", languageCode);
        } else {
            playerSection.set("myLanguage", languageCode);
        }

        try{
            preferencesConfig.save(preferencesFile);
        }catch(IOException e){

        }
        return true;
    }

    private String getLanguageCode(String lng){
        Locale loc = new Locale("en");
        String[] name = loc.getISOLanguages(); // list of language codes

        for (int i = 0; i < name.length; i++) {
            Locale locale = new Locale(name[i],"US");
            // get the language name in english for comparison
            String langLocal = locale.getDisplayLanguage(loc).toLowerCase();
            if (lng.equals(langLocal)){
                return locale.getISO3Language();
            }
        }
        return null;
    }
}

