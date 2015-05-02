package matthbo.plugin.ccooldown;

import com.google.inject.Inject;
import matthbo.plugin.ccooldown.command.CommandCcooldown;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.command.CommandService;

import java.io.File;

@Plugin(id = Refs.NAME, name = Refs.NAME, version = Refs.VERSION)
public class CCooldown {

    public static CCooldown instance;

    @Inject
    private Logger logger;

    @Inject
    private Game game;

    private File dataFolder = new File("mods/",Refs.NAME);

    @Subscribe
    public void onEnabled(ServerStartingEvent event){
        instance = this;

        CommandService cmdService = getGame().getCommandDispatcher();
        cmdService.register(instance, new CommandCcooldown(), "ccooldown");

        getLogger().info("CCooldown has been Activated");
    }

    @Subscribe
    public void onDisabled(ServerStoppingEvent event){
        getLogger().info("CCooldown is Disconnected from the Internet!");
    }

    public Logger getLogger() {
        return logger;
    }

    public Game getGame(){
        return game;
    }

    public File getDataFolder(){
        return dataFolder;
    }

}
