package net.pyerter.pootsadditions.block.entity;

import net.minecraft.block.BlockState;
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

    public static final int MAX_STORED_CHARGE = 12100;
    public static final int MAX_CHARGE_TRANSFER_RATE = 50;

    public CaptureChamberEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAPTURE_CHAMBER, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return CaptureChamberEntity.this.storedCharge;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: CaptureChamberEntity.this.storedCharge = value; break;
                }
            }

            @Override
            public int size() {
                return 1;
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
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        storedCharge = nbt.getInt("capture_chamber.storedCharge");
    }

    public static void tick(World world, BlockPos pos, BlockState state, CaptureChamberEntity entity) {
        addCharge(entity, 1);
        tryAddCoreCharges(entity);
    }

    public static int addCharge(CaptureChamberEntity entity, Integer amount) {
        int oldCharge = entity.storedCharge;
        entity.storedCharge = Util.clamp(entity.storedCharge + amount, 0, CaptureChamberEntity.MAX_STORED_CHARGE);
        return  entity.storedCharge - oldCharge;
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
