package net.ayberkbilisim.ic2ccsupport;

import com.google.gson.internal.LinkedHashTreeMap;
import com.mojang.logging.LogUtils;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import ic2.core.block.cables.mointor.MonitorDataManager;
import ic2.core.block.cables.mointor.MonitorTileEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.*;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
                        map.put(i + 1,tg.getAsString());
                    }
                    // Veeery lazy to do inventory card support so just use chest peripheral bruh

                }
                return MethodResult.of(new ObjectLuaTable(map));
            }


        }
        return MethodResult.of();
    }
}
