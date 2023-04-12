package net.ayberkbilisim.ic2ccsupport;

import com.mojang.logging.LogUtils;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// I literally have 0 idea how to mod

@Mod(IC2CCSupport.MODID)
public class IC2CCSupport
{

    public static final String MODID = "ic2ccsupport";

    private static final Logger LOGGER = LogUtils.getLogger();


    public IC2CCSupport()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        ForgeComputerCraftAPI.registerPeripheralProvider(new MonitorPeripheralProvider());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
	


}
