package commonnetwork;

import commonnetwork.networking.PaperNetworkHandler;
import commonnetwork.networking.data.Side;
import org.bukkit.plugin.java.JavaPlugin;

public class CommonNetworkPaper extends JavaPlugin
{
    private PaperNetworkHandler handler;

    @Override
    public void onEnable()
    {
        this.handler = new PaperNetworkHandler(this, Side.SERVER);
        new CommonNetworkMod(this.handler);
    }

    @Override
    public void onDisable()
    {
        if (this.handler != null)
        {
            this.handler.shutdown();
        }
    }
}
