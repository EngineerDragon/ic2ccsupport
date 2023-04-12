package net.ayberkbilisim.ic2ccsupport;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import ic2.core.block.cables.mointor.MonitorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;


public class MonitorPeripheralProvider implements IPeripheralProvider{
    @NotNull
    @Override
    public LazyOptional<IPeripheral> getPeripheral(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        BlockEntity te = level.getBlockEntity(blockPos);
        if (te instanceof MonitorTileEntity) {
            //LogUtils.getLogger().debug("Oh my god it works!");
            return LazyOptional.of(() -> new MonitorPeripheral((MonitorTileEntity) te));
        }
            else return LazyOptional.empty();
    }
}
