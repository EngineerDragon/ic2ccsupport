package net.ayberkbilisim.ic2ccsupport;

import com.google.gson.internal.LinkedHashTreeMap;
import com.mojang.logging.LogUtils;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import ic2.api.network.buffer.IInputBuffer;
import ic2.api.network.buffer.IOutputBuffer;
import ic2.api.tiles.display.IDisplayInfo;
import ic2.core.block.cables.mointor.MonitorBlock;
import ic2.core.block.cables.mointor.MonitorDataManager;
import ic2.core.block.cables.mointor.MonitorTileEntity;
import ic2.core.inventory.handler.InventoryHandler;
import ic2.core.item.tool.infos.ActivityCardItem;
import ic2.core.networking.buffers.InputBuffer;
import ic2.core.networking.buffers.OutputBuffer;
import ic2.core.platform.player.friends.Friend;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.utils.Log;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

class MonitorPeripheral implements IDynamicPeripheral
{
    private final MonitorTileEntity block;
    MonitorPeripheral(MonitorTileEntity block)
    {
        this.block = block;
    }
    @NotNull
    @Override
    public String getType() {
        return "ic2monitor";
    }

    @NotNull
    @Override
    public Set<String> getAdditionalTypes() {
        return IDynamicPeripheral.super.getAdditionalTypes();
    }

    @Override
    public void attach(@NotNull IComputerAccess computer) {
        IDynamicPeripheral.super.attach(computer);
    }

    @Override
    public void detach(@NotNull IComputerAccess computer) {
        IDynamicPeripheral.super.detach(computer);
    }

    @Nullable
    @Override
    public Object getTarget() {
        return IDynamicPeripheral.super.getTarget();
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }

    @NotNull
    @Override
    public String[] getMethodNames(){
        return new String[]{"getScreenData"};
    }

    @NotNull
    @Override
    public MethodResult callMethod(@NotNull IComputerAccess iComputerAccess, @NotNull ILuaContext iLuaContext, int idx, @NotNull IArguments iArguments) throws LuaException {
        switch(idx) { // might add things later currently I am so damn lazy
            case 0: {
                MonitorDataManager mgr = block.dataManager;
                LogUtils.getLogger().debug(((Integer)mgr.size()).toString());
                LinkedHashTreeMap<Object,Object> map = new LinkedHashTreeMap<Object,Object>();

                for (int i = 0; i < mgr.size();i++)
                {
                   // LogUtils.getLogger().debug(mgr.get(i).toString());
                   // LogUtils.getLogger().debug(mgr.get(i).getServerData().toString());
                    Tag tg = mgr.get(i).getServerData();
                  //  LogUtils.getLogger().debug(tg.getType().getName());
                    if (tg instanceof LongArrayTag)
                    {
                        ByteBuf b = Unpooled.buffer(8);
                        b.writeLong( ((LongArrayTag) tg).getAsLongArray()[0]);
                        map.put(i+1,b.readDouble());
                    } else if (tg instanceof StringTag)
                    {
                        map.put(i + 1,tg.toString());
                    }
                    // Veeery lazy to do inventory card support so just use chest peripheral bruh

                }
                return MethodResult.of(new ObjectLuaTable(map));
            }


        }
        return MethodResult.of(null);
    }
}
