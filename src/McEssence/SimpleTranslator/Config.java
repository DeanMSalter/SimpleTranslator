package McEssence.SimpleTranslator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Locale;

public class Config{
    private final Main main;
    public Config(Main mainTemp){
        main = mainTemp;
    }

    public Boolean getEnabled(){
        return main.getConfig().getBoolean("general.enabled");
    }
    public String getApiKey(){
        return main.getConfig().getString("general.apiKey");
    }
    public String getDefaultLanguage(){
        return main.getConfig().getString("general.defaultLanguage");
    }
    public String getPlayerLanguage(Player player){
        File preferencesFile = new File(main.getDataFolder(), "preferences.yml");
        FileConfiguration preferencesConfig = YamlConfiguration.loadConfiguration(preferencesFile);

        ConfigurationSection playerSection = preferencesConfig.getConfigurationSection(String.valueOf(player.getUniqueId()));
        if (playerSection == null) {
            return null;
        } else {
            return (String) playerSection.get("myLanguage");
        }
    }
}
