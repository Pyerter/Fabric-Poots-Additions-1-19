package net.pyerter.pootsadditions.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
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
import net.pyerter.pootsadditions.recipe.TridiRecipe;
import net.pyerter.pootsadditions.screen.handlers.TridiScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

// Tridiminiumobulator!
public class TridiBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private static final String DISPLAY_NAME = "Tridiminiumobulator";
    private static final List<Item> acceptedQuickTransfers = List.of(ModItems.STARMETAL_ALLOY_INGOT, ModItems.SAPPHIRE_STAR);
    private static final List<Item> acceptedQuickTransfersFuel = List.of(ModItems.MAKESHIFT_CORE);
    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(7, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public TridiBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRIDI, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return TridiBlockEntity.this.progress;
                    case 1: return TridiBlockEntity.this.maxProgress;
                    case 2: return TridiBlockEntity.this.fuelTime;
                    case 3: return TridiBlockEntity.this.maxFuelTime;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: TridiBlockEntity.this.progress = value; break;
                    case 1: TridiBlockEntity.this.maxProgress = value; break;
                    case 2: TridiBlockEntity.this.fuelTime = value; break;
                    case 3: TridiBlockEntity.this.maxFuelTime = value; break;
                }
            }

            @Override
            public int size() {
                return 4;
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
        return new TridiScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("tridi.progress", progress);
        nbt.putInt("tridi.fuelTime", fuelTime);
        nbt.putInt("tridi.maxFuelTime", maxFuelTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("tridi.progress");
        fuelTime = nbt.getInt("tridi.fuelTime");
        maxFuelTime = nbt.getInt("tridi.maxFuelTime");
    }

    public static void tick(World world, BlockPos pos, BlockState state, TridiBlockEntity entity) {
        Optional<TridiRecipe> recipe = hasRecipe(entity);
        if (recipe.isPresent()) {
            entity.progress++;
            if (entity.progress > entity.maxProgress) {
                craftItem(entity, recipe.get());
            }
        } else {
            entity.resetProgress();
        }
    }

    private static void consumeFuel(TridiBlockEntity entity, Integer cost) {
        if (entity.inventory.get(5).getItem() instanceof AbstractPowerCore) {
            AbstractPowerCore core = (AbstractPowerCore) entity.inventory.get(5).getItem();
            core.addCharge(entity.inventory.get(5), -cost);
        }
    }

    private static boolean hasAvailablePowerInSlot(TridiBlockEntity entity, TridiRecipe recipe) {
        if (entity.inventory.get(5).isEmpty())
            return false;

        if(acceptsQuickTransferFuel(entity.inventory.get(5)) && entity.inventory.get(5).getItem() instanceof AbstractPowerCore) {
            AbstractPowerCore core = (AbstractPowerCore) entity.inventory.get(5).getItem();
            return core.getCharge(entity.inventory.get(5)) >= recipe.getEnergyCost();
        }

        return false;
    }



    private static boolean isConsumingFuel(TridiBlockEntity entity) {
        return entity.fuelTime > 0;
    }

    private static void craftItem(TridiBlockEntity entity, TridiRecipe recipe) {
        entity.removeStack(0, 1);
        entity.removeStack(1,1);
        entity.removeStack(2,1);
        entity.removeStack(3, 1);
        entity.removeStack(4, 1);

        consumeFuel(entity, recipe.getEnergyCost());

        entity.setStack(6, new ItemStack(recipe.getOutput().getItem(),
                entity.getStack(6).getCount() + recipe.getOutput().getCount()));
        entity.resetProgress();
    }

    private static Optional<TridiRecipe> hasRecipe(TridiBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < inventory.size(); i++) { inventory.setStack(i, entity.getStack(i)); }

        Optional<TridiRecipe> match = world.getRecipeManager()
                .getFirstMatch(TridiRecipe.Type.INSTANCE, inventory, world);

        if (match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput())
                && hasAvailablePowerInSlot(entity, match.get())) {
            return match;
        }
        return Optional.empty();
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, ItemStack output) {
        return inventory.getStack(6).getItem() == output.getItem() || inventory.getStack(6).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(6).getMaxCount() > inventory.getStack(6).getCount();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean checkSlotsForItem(TridiBlockEntity entity, Item item) {
        for (int i = 0; i < 5; i++) {
            if (entity.getStack(i).getItem() == item) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkSlotsExclusiveToList(TridiBlockEntity entity, List<Item> items) {
        for (int i = 0; i < 5; i++) {
            ItemStack itemStack = entity.getStack(i);
            // if slot is not empty and filled with something outside of the provided list
            if (itemStack != ItemStack.EMPTY && !items.contains(itemStack.getItem())) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasNotReachedStackLimit(TridiBlockEntity entity) {
        return entity.getStack(6).getCount() < entity.getStack(6).getMaxCount();
    }

    public static boolean acceptsQuickTransfer(ItemStack itemStack) {
        return acceptedQuickTransfers.contains(itemStack.getItem());
    }

    public static boolean acceptsQuickTransferFuel(ItemStack itemStack) {
        return acceptedQuickTransfersFuel.contains(itemStack.getItem());
    }
}
