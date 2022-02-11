package McEssence.SimpleTranslator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jsoup.Jsoup;


public class Listeners implements Listener {
    private final Config config;
    private final Main main;
    private final GoogleTranslate translator;

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SimpleTranslator");

    public Listeners(Main mainTemp, Config configTemp){
        main = mainTemp;
        config = configTemp;
        translator = new GoogleTranslate(config.getApiKey());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onChat(AsyncPlayerChatEvent event) {
        Player sendingPlayer = event.getPlayer();
        Set<Player> recipients = event.getRecipients();

        if (config.getPlayerLanguage(sendingPlayer) != null) {
            String rawText = translator.translate(event.getMessage(), config.getPlayerLanguage(sendingPlayer), "eng");
            String textToSend = StringEscapeUtils.unescapeHtml3(rawText);
            event.setMessage(textToSend);
        }

        for(Player player : recipients) {
            if (player == event.getPlayer()) {
                continue;
            }
            if (config.getPlayerLanguage(player) == null) {
                continue;
            }

            String rawText = translator.translate(event.getMessage(), "eng", config.getPlayerLanguage(player));
            String textToSend = StringEscapeUtils.unescapeHtml3(rawText);
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.sendMessage(textToSend);
                }
            }, 1);
        }
    }

}
