package matthbo.plugin.ccooldown.util;

import matthbo.plugin.ccooldown.CCooldown;
import matthbo.plugin.ccooldown.Refs;
import matthbo.plugin.ccooldown.command.CommandCcooldown;
import matthbo.plugin.ccooldown.config.Config;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class CCAutoOn {

    private Config config = CCooldown.instance.getConfig();

    public final int checkTime = config.messages * 1000;
    private final List<Text> messages = new ArrayList<>();

    public void addMSG(Text msg){
        this.messages.add(msg);
    }

    public void check(Player player){
        if(this.messages.size() >= config.messages && !config.isOn) {config.isOn = true; config.set(config.isOn_LANG, true); messageToAll("SlowMode is on!");}
        if(this.messages.size() < (config.messages / 2) && config.isOn){config.isOn = false; config.set(config.isOn_LANG, false); messageToAll("SlowMode is off!");}
        messages.removeAll(messages);
    }

    private Text sendToAll(String msg){
        return Texts.builder(Refs.pluginMSG).color(TextColors.DARK_PURPLE)
                .append(Texts.builder(msg).color(TextColors.GOLD).build()).build();
    }

    private void messageToAll(String msg) {
        Object[] allPlayers = CCooldown.instance.getGame().getServer().getOnlinePlayers().toArray();

        for (int j = 0; j < allPlayers.length; j++) {
            Player target = (Player)allPlayers[j];
            target.sendMessage(sendToAll(msg));
            CCooldown.instance.getLogger().info(msg);
        }
    }

}
