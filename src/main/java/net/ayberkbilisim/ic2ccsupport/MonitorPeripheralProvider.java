package net.ayberkbilisim.ic2ccsupport;

import com.mojang.logging.LogUtils;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import ic2.core.block.cables.mointor.MonitorBlock;
import ic2.core.block.cables.mointor.MonitorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;


public class MonitorPeripheralProvider implements IPeripheralProvider{
    @NotNull
    @Override
    public LazyOptional<IPeripheral> getPeripheral(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        BlockEntity te = level.getBlockEntity(blockPos);
        if (te == null) return LazyOptional.of(null);

        if (te instanceof MonitorTileEntity) {
            //LogUtils.getLogger().debug("Oh my god it works!");
            return LazyOptional.of(() -> new MonitorPeripheral((MonitorTileEntity) te));
        }
            else return LazyOptional.of(null);
    }
}
