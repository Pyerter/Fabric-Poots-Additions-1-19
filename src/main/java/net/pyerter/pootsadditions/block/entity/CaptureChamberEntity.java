package net.pyerter.pootsadditions.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.block.custom.CaptureChamberProviderBlock;
import net.pyerter.pootsadditions.item.ModItems;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractPowerCore;
import net.pyerter.pootsadditions.item.custom.engineering.MakeshiftCore;
import net.pyerter.pootsadditions.item.inventory.ImplementedInventory;
import net.pyerter.pootsadditions.screen.handlers.CaptureChamberScreenHandler;
import net.pyerter.pootsadditions.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CaptureChamberEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private static final String DISPLAY_NAME = "Capture Chamber";
    private static final List<Item> acceptedQuickTransfers = List.of(ModItems.MAKESHIFT_CORE);
    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(3, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int storedCharge;
    private int passiveChargeTicks;
    private BlockPos provideTo = BlockPos.ORIGIN;
    private int receivingPriority = -1;

    public static final int MAX_STORED_CHARGE = 12100;
    public static final int MAX_CHARGE_TRANSFER_RATE = 50;
    public static final int TICKS_PER_PASSIVE_CHARGE = 20;
    public static final int TICKS_PER_PASSIVE_TRANSFER = 2;
    public static final int DEFAULT_PRIORITY = -1;

    public CaptureChamberEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAPTURE_CHAMBER, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return CaptureChamberEntity.this.storedCharge;
                    case 1: return CaptureChamberEntity.this.passiveChargeTicks;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: CaptureChamberEntity.this.storedCharge = value; break;
                    case 1: CaptureChamberEntity.this.passiveChargeTicks = value; break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return MutableText.of(new LiteralTextContent(DISPLAY_NAME));
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CaptureChamberScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("capture_chamber.storedCharge", storedCharge);
        nbt.putInt("capture_chamber.passiveChargeTicks", passiveChargeTicks);
        nbt.putLong("capture_chamber.provideTo", provideTo.asLong());
        nbt.putInt("capture_chamber.receivingPriority", receivingPriority);

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        storedCharge = nbt.getInt("capture_chamber.storedCharge");
        passiveChargeTicks = nbt.getInt("capture_chamber.passiveChargeTicks");
        provideTo = BlockPos.fromLong(nbt.getLong("capture_chamber.provideTo"));
        receivingPriority = nbt.getInt("capture_chamber.receivingPriority");
    }

    public static void tick(World world, BlockPos pos, BlockState state, CaptureChamberEntity entity) {
        if (world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock() == Blocks.LIGHTNING_ROD)
            entity.passiveChargeTicks++;
        if (entity.passiveChargeTicks % TICKS_PER_PASSIVE_CHARGE == 0) {
            addCharge(entity, entity.passiveChargeTicks / TICKS_PER_PASSIVE_CHARGE);
            entity.passiveChargeTicks = 0;
            if (entity.receivingPriority != DEFAULT_PRIORITY)
                assertStillHavePriority(world, pos, entity);
        }
        if (entity.passiveChargeTicks % TICKS_PER_PASSIVE_TRANSFER == 0)
            tryProvideTo(world, entity);

        tryAddCoreCharges(entity);
        entity.markDirty();
    }

    public static boolean tryProvideTo(World world, CaptureChamberEntity entity) {
        if (Util.blockPosEqual(entity.provideTo, BlockPos.ORIGIN))
            return false;

        if (Util.blockPosEqual(entity.provideTo, entity.getPos())) {
            entity.provideTo = BlockPos.ORIGIN;
            return false;
        }

        BlockEntity targetEntity = world.getBlockEntity(entity.provideTo);
        if (targetEntity == null || !(targetEntity instanceof CaptureChamberEntity)) {
            entity.provideTo = BlockPos.ORIGIN;
            return false;
        }

        CaptureChamberEntity targetChamber = (CaptureChamberEntity) targetEntity;
        if (targetChamber.receivingPriority == DEFAULT_PRIORITY || targetChamber.receivingPriority <= entity.receivingPriority) {
            entity.provideTo = BlockPos.ORIGIN;
            return false;
        }

        int canTransfer = Math.min(MAX_CHARGE_TRANSFER_RATE, entity.storedCharge);
        int transferred = addCharge(targetChamber, canTransfer);
        if (transferred <= 0) {
            return false;
        } else {
            entity.storedCharge -= transferred;
            return true;
        }
    }

    public static void assertStillHavePriority(World world, BlockPos pos, CaptureChamberEntity entity) {
        BlockPos upPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        BlockPos downPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        if (!(world.getBlockEntity(upPos) instanceof CaptureChamberProviderEntity) && !(world.getBlockEntity(downPos) instanceof CaptureChamberProviderEntity)) {
            entity.resetPriority();
        }
    }

    public static int addCharge(CaptureChamberEntity entity, Integer amount) {
        int oldCharge = entity.storedCharge;
        entity.storedCharge = Util.clamp(entity.storedCharge + amount, 0, CaptureChamberEntity.MAX_STORED_CHARGE);
        return entity.storedCharge - oldCharge;
    }

    private static int addCoreCharge(CaptureChamberEntity entity, Integer amount, int index) {
        ItemStack core = entity.inventory.get(index);
        if (core.isEmpty() || !(core.getItem() instanceof AbstractPowerCore))
            return 0;

        AbstractPowerCore coreItem = (AbstractPowerCore) core.getItem();
        int originalAmount = coreItem.getCharge(core);
        int newAmount = coreItem.addCharge(core, amount);
        return newAmount - originalAmount;
    }

    private static void tryAddCoreCharges(CaptureChamberEntity entity) {
        int i = 0;
        while (entity.storedCharge > 0 && i < 3) {
            if (hasAvailableCoreInSlot(entity,  i)) {
                addCharge(entity, -addCoreCharge(entity, Math.min(entity.storedCharge, MAX_CHARGE_TRANSFER_RATE), i));
            }
            i++;
        }
    }

    public int getReceivingPriority() {
        return receivingPriority;
    }

    public void setReceivingPriority(int val) {
        receivingPriority = val;
        markDirty();
    }

    public void resetPriority() {
        receivingPriority = DEFAULT_PRIORITY;
        markDirty();
    }

    public void setProvideTarget(BlockPos target) {
        this.provideTo = target;
        markDirty();
    }

    public BlockPos getProvideTarget() { return provideTo; }

    private static boolean hasAvailableCoreInSlot(CaptureChamberEntity entity, int index) {
        if (entity.inventory.get(index).isEmpty())
            return false;

        return true;
    }

    public static boolean acceptsQuickTransfer(ItemStack itemStack) {
        return acceptedQuickTransfers.contains(itemStack.getItem());
    }

    public static boolean acceptsQuickTransferFuel(ItemStack itemStack) {
        return false;
    }
}
