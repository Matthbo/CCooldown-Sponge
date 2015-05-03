package matthbo.plugin.ccooldown.event;

import matthbo.plugin.ccooldown.CCooldown;
import matthbo.plugin.ccooldown.config.Config;
import matthbo.plugin.ccooldown.Refs;
import matthbo.plugin.ccooldown.command.CommandCcooldown;
import matthbo.plugin.ccooldown.util.CCAutoOn;
import matthbo.plugin.ccooldown.util.CCManager;
import matthbo.plugin.ccooldown.util.Cooldown;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

public class EventHandler {

    private CCManager manager = new CCManager();
    private CCAutoOn autoOn = new CCAutoOn();
    private Config config = CCooldown.instance.getConfig();

    private long time = System.currentTimeMillis();

    private Text sendMSG(String msg){
        return Texts.builder(Refs.pluginMSG).color(TextColors.DARK_PURPLE)
                .append(Texts.builder(msg).color(TextColors.DARK_GRAY).build()).build();
    }
    private Text sendSpacialMSG(String msg, String time, String msg2){
        return Texts.builder(Refs.pluginMSG).color(TextColors.DARK_PURPLE)
                .append(Texts.builder(msg).color(TextColors.DARK_GRAY).build())
                .append(Texts.builder(time).color(TextColors.GRAY).build())
                .append(Texts.builder(msg2).color(TextColors.DARK_GRAY).build()).build();
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        if(config.isOn){
            if(!bypass(player) && manager.hasCooldown(id)){
                event.setCancelled(true);
                player.sendMessage(sendMSG(config.warning));
                player.sendMessage(sendSpacialMSG("Time between messages: ", String.valueOf(config.time), " seconds"));
            }else {
                this.manager.add(new Cooldown(id, manager.getCooldownTime(),System.currentTimeMillis()));
            }
        }

        if(config.autoOn){
            autoOn.addMSG(event.getMessage());

            if(System.currentTimeMillis() >= time + autoOn.checkTime){
                autoOn.check(player);
                time = System.currentTimeMillis();
            }
        }
    }

    private boolean bypass(Player player){
        return player.hasPermission(CommandCcooldown.PermissionList.bypass) || player.hasPermission(CommandCcooldown.PermissionList.admin);
    }

}
