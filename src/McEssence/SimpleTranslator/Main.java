package McEssence.SimpleTranslator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    Config config;

    @Override
    public void onEnable() {
        File f = new File(this.getDataFolder() + "/");
        if(!f.exists()) {
            f.mkdir();
        }
        getConfig().options().copyDefaults(true);
        saveConfig();

        File preferenceFile = new File(this.getDataFolder() + File.separator + "preferences.yml");
        if(!preferenceFile.exists()){
            try {
                preferenceFile.createNewFile();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = new Config(this);
        if (!config.getEnabled()){
            Bukkit.getLogger().info(ChatColor.RED + " Disabled" + this.getName() + " As not enabled in config");
            return;
        }

        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled" + this.getName());
        this.getCommand("SimpleTranslator").setExecutor(new Commands(config, this));
        getServer().getPluginManager().registerEvents(new Listeners(this, config), this);

//        ProtocolLibrary.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
//                new PacketAdapter(this, ConnectionSide.CLIENT_SIDE, Packets.Client.CHAT) {
//                    @Override
//                    public void onPacketReceiving(final PacketEvent event) {
//                        event.getPlayer().sendMessage("test");
//                    }
//
//
//
////                .addPacketListener(
////                new PacketAdapter(this, ConnectionSide.SERVER_SIDE, Packets.Server.CHAT) {
////                    @Override
////                    public void onPacketSending(PacketEvent event) {
////                        Player player = event.getPlayer();
////                        if (config.getPlayerLanguage(player) != null) {
////                            String message = event.getPacket().getStrings().read(0);
////                            GoogleTranslate translator = new GoogleTranslate(config.getApiKey());
////                            String text = translator.translate(message, "en", config.getPlayerLanguage(player));
////                            String textToSend = StringEscapeUtils.unescapeHtml3(text);
////                            event.getPlayer().sendMessage(textToSend);
////                        }
////
////
////                    }
//                });
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Disabled " + this.getName());
    }

}
