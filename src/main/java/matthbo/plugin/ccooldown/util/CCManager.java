package matthbo.plugin.ccooldown.util;

import matthbo.plugin.ccooldown.CCooldown;
import matthbo.plugin.ccooldown.config.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CCManager {

    private Config config = CCooldown.instance.getConfig();

    private final int cooldownTime = config.time * 1000;
    private final Map<UUID, Cooldown> cooldowns = new HashMap<>();

    public void add(Cooldown cooldown){
        UUID player = cooldown.getPlayer();
        this.cooldowns.put(player, cooldown);
    }

    public boolean hasCooldown(UUID player){
        Cooldown cooldown = this.cooldowns.get(player);
        if(cooldown == null) return false;
        if(cooldown.isExpired()){
            this.cooldowns.remove(player);
            return false;
        }
        return true;
    }

    public int getCooldownTime(){
        return this.cooldownTime;
    }

}
