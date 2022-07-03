package net.pyerter.pootsadditions.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.ModItems;
import net.pyerter.pootsadditions.item.ModToolMaterials;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractEngineeredTool;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractEngineersItem;
import net.pyerter.pootsadditions.item.custom.engineering.EngineersTrustyHammer;
import net.pyerter.pootsadditions.item.inventory.ImplementedInventory;
import net.pyerter.pootsadditions.recipe.EngineeringStationRefineRecipe;
import net.pyerter.pootsadditions.screen.handlers.EngineeringStationScreenHandler;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EngineeringStationEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private static final String DISPLAY_NAME = "Engineering Station";
    private static List<ToolMaterial> materials = null;
    public static List<ToolMaterial> getMaterials() { if(materials == null) materials = updateToolMaterials(); return materials; }
    private static final List<Item> ENGINEERS_ITEMS = List.of(ModItems.ENGINEERS_BLUPRINT);
    public static final List<Item> acceptedRefiningMaterials = new ArrayList<>();
    public static final int ENGINEERING_STATION_INVENTORY_SIZE = 5;
    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(ENGINEERING_STATION_INVENTORY_SIZE, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int hammered = 0;
    private int successfulBuild = 0;
    private ItemStack resultingStack = ItemStack.EMPTY;

    public static final int HAMMERS_PER_CRAFT = 2;

    public EngineeringStationEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENGINEERING_STATION, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return EngineeringStationEntity.this.hammered;
                    case 1: return EngineeringStationEntity.this.successfulBuild;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: EngineeringStationEntity.this.hammered = value; break;
                    case 1: EngineeringStationEntity.this.successfulBuild = value; break;
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
        return new EngineeringStationScreenHandler(syncId, inv, this, this.propertyDelegate, this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("engineering_station.hammered", hammered);
        nbt.putInt("engineering_station.successfulBuild", successfulBuild);
        nbt.put("engineering_station.resulting_stack", resultingStack.writeNbt(new NbtCompound()));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        hammered = nbt.getInt("engineering_station.hammered");
        successfulBuild = nbt.getInt("engineering_station.successfulBuild");
        resultingStack = ItemStack.fromNbt(nbt.getCompound("engineering_station.resulting_stack"));
    }

    public Pair<Boolean, Boolean> hammerIt() {
        return hammerIt(false);
    }

    // Left boolean: if can hammer
    // Right boolean: if result was made
    // Left boolean implies right boolean
    public Pair<Boolean, Boolean> hammerIt(boolean stackCraft) {
        PootsAdditions.logInfo("Trying to hammer!");
        Pair<Optional<ItemStack>, Boolean[]> craftingResult = getCraftingResult();
        if (!craftingResult.getLeft().isPresent()) {
            resetHammeredProgress();
            return new Pair<>(false, false);
        }

        boolean shouldStackCraft = stackCraft && !craftingResult.getRight()[0] &&
                !craftingResult.getRight()[1] && !craftingResult.getRight()[2] &&
                craftingResult.getRight()[3];

        hammered++;
        PootsAdditions.logInfo("Hammering!");
        if (hammered >= HAMMERS_PER_CRAFT) {
            if (shouldStackCraft) {
                craftResultMax(craftingResult.getLeft().get(), craftingResult.getRight());
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1f, 1f, 0);
            } else {
                craftResult(craftingResult.getLeft().get(), craftingResult.getRight());
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1f, 1f, 0);
            }
            resetHammeredProgress();
            successfulBuild = 1;
            // world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1f, 1f, 0);
            return new Pair<>(true, true);
        } else {
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1f, 1f, 0);
        }
        return new Pair<>(true, false);
    }

    public boolean craftResult(ItemStack stack, Boolean[] usedSlots) {
        if (stack == null || stack.isEmpty() || usedSlots.length != 4)
            return false;

        for (int i = 0; i < 4; i++) {
            if (usedSlots[i])
                inventory.get(i).decrement(1);
        }

        if (inventory.get(2).isEmpty())
            inventory.set(2, stack);
        else if (inventory.get(2).isItemEqual(stack))
            inventory.get(2).increment(stack.getCount());
        return true;
    }

    public boolean craftResultMax(ItemStack stack, Boolean[] usedSlots) {
        if (stack == null || stack.isEmpty() || usedSlots.length != 4)
            return false;

        Optional<EngineeringStationRefineRecipe> recipe = hasRefineRecipe(this);
        if (!recipe.isPresent() || !recipe.get().getOutput().isItemEqual(stack))
            return false;

        ItemStack recipeResult = recipe.get().getOutput();
        int maxStackCount = recipeResult.getMaxCount();
        int openSpace = (maxStackCount - (!inventory.get(2).isEmpty() ? inventory.get(2).getCount() : 0));
        maxStackCount = openSpace;
        int repeatTimes = maxStackCount / recipeResult.getCount();
        for (int i = 0; i < 4; i++) {
            if (usedSlots[i])
                repeatTimes = Math.min(repeatTimes, inventory.get(i).getCount());
        }
        stack.setCount(stack.getCount() * repeatTimes);

        for (int i = 0; i < 4; i++) {
            if (usedSlots[i])
                inventory.get(i).decrement(repeatTimes);
        }

        if (inventory.get(2).isEmpty())
            inventory.set(2, stack);
        else if (inventory.get(2).isItemEqual(stack))
            inventory.get(2).increment(stack.getCount());

        return true;
    }

    public void resetHammeredProgress() {
        hammered = 0;
        successfulBuild--;
    }

    public static void tick(World world, BlockPos pos, BlockState state, EngineeringStationEntity entity) {
        // nada
    }

    public Pair<Optional<ItemStack>, Boolean[]> getCraftingResult() {
        // TODO: check if the tool in the tool slot is craftable with the engineers item
        // TODO: check if the engineers tool in the middle is augmentable with mats or augments
        Optional<ItemStack> resultingStack;

        resultingStack = canEngineerTool(getToolSlot(), getEngineeringSlot());
        if (resultingStack.isPresent())
            return new Pair(resultingStack, new Boolean[]{true, false, true, false});

        resultingStack = canUpgradeEngineerTool(getMaterialSlot(), getEngineeringSlot());
        if (resultingStack.isPresent())
            return new Pair(resultingStack, new Boolean[]{false, false, true, true});

        Optional<EngineeringStationRefineRecipe> refineRecipe = hasRefineRecipe(this);
        if (refineRecipe.isPresent())
            return new Pair(Optional.of(refineRecipe.get().getOutput()), new Boolean[]{false, false, false, true});

        return new Pair<>(Optional.empty(), null);
    }

    public static Optional<ItemStack> canEngineerTool(ItemStack toolStack, ItemStack engineerStack) {
        PootsAdditions.logInfo("Checking if can engineer tool.");
        ToolMaterial mat = getToolMaterial(toolStack);
        AbstractEngineeredTool.ToolType toolType = AbstractEngineeredTool.getToolType(toolStack);
        if (mat == null || toolType == AbstractEngineeredTool.ToolType.NADA)
            return Optional.empty();

        AbstractEngineersItem.ENGINEERS_CRAFT_TYPE craftType = AbstractEngineersItem.tryGetEngineersCraftType(engineerStack);
        switch (craftType) {
            case ENGINEERIFY:
                AbstractEngineeredTool resultTool = AbstractEngineeredTool.getRegisteredTool(mat, toolType);
                return resultTool != null ? Optional.of(new ItemStack(resultTool)) : Optional.empty();
            default: return Optional.empty();
        }
    }

    public static Optional<ItemStack> canUpgradeEngineerTool(ItemStack materialStack, ItemStack engineerStack) {
        ToolMaterial mat = AbstractEngineeredTool.getToolMaterialFromIngredient(materialStack);
        if (mat == null)
            return Optional.empty();

        Pair<ToolMaterial, AbstractEngineeredTool.ToolType> typePair = AbstractEngineeredTool.tryGetToolInfo(engineerStack);
        if (typePair == null || typePair.getRight() == AbstractEngineeredTool.ToolType.NADA || typePair.getLeft() == mat)
            return Optional.empty();

        if (!(mat.getMiningLevel() == typePair.getLeft().getMiningLevel() + 1 || mat.getMiningLevel() == typePair.getLeft().getMiningLevel()))
            return Optional.empty();

        AbstractEngineeredTool newTool = AbstractEngineeredTool.getRegisteredTool(mat, typePair.getRight());
        ItemStack newStack = new ItemStack(newTool);
        boolean transferSuccess = AbstractEngineeredTool.transferToolData (engineerStack, newStack);
        if (!transferSuccess)
            return Optional.empty();

        Optional<ItemStack> resultStack = Optional.of(newStack);
        return resultStack;
    }

    private static Optional<EngineeringStationRefineRecipe> hasRefineRecipe(EngineeringStationEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < inventory.size(); i++) { inventory.setStack(i, entity.getStack(i)); }

        Optional<EngineeringStationRefineRecipe> match = world.getRecipeManager()
                .getFirstMatch(EngineeringStationRefineRecipe.Type.INSTANCE, inventory, world);

        if (match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput())) {
            return match;
        }
        return Optional.empty();
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, ItemStack output) {
        return inventory.getStack(2).getItem() == output.getItem() || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }

    public static boolean acceptsQuickTransfer(ItemStack itemStack) {
        return false;
    }

    /** Slot 0: Tool slot is the left slot **/
    public static boolean acceptsQuickTransferToolSlot(ItemStack itemStack) {
        return itemStack.getItem() instanceof ToolItem;
    }
    /** Slot 0: Tool slot is the left slot **/
    public ItemStack getToolSlot() {
        return inventory.get(0);
    }

    /** Slot 1: Augment slot is the right slot **/
    public static boolean acceptsQuickTransferAugmentSlot(ItemStack itemStack) {
        return false;
    }
    /** Slot 1: Augment slot is the right slot **/
    public ItemStack getAugmentSlot() {
        return inventory.get(1);
    }

    /** Slot 2: Engineering Slot is the bottom slot **/
    public static boolean acceptsQuickTransferEngineeringSlot(ItemStack itemStack) {
        return itemStack.getItem() instanceof AbstractEngineeredTool || ENGINEERS_ITEMS.contains(itemStack.getItem());
    }
    /** Slot 2: Engineering Slot is the bottom slot **/
    public ItemStack getEngineeringSlot() {
        return inventory.get(2);
    }

    /** Slot 3: Materials slot is the top slot **/
    public static boolean acceptsQuickTransferMaterialSlot(ItemStack itemStack) {
        return acceptedRefiningMaterials.contains(itemStack.getItem()) || AbstractEngineeredTool.isAcceptedToolIngredient(itemStack);
    }
    /** Slot 3: Materials slot is the top slot **/
    public ItemStack getMaterialSlot() {
        return inventory.get(3);
    }

    /** Slot 4: Hammer slot is the far left slot **/
    public static boolean acceptsQuickTransferHammerSlot(ItemStack itemStack) {
        return itemStack.getItem() instanceof EngineersTrustyHammer;
    }
    /** Slot 4: Hammer slot is the far left slot **/
    public ItemStack getHammerSlot() {
        return inventory.get(4);
    }

    public static boolean acceptsQuickTransfer(ItemStack itemStack, Integer i) {
        switch (i) {
            case 0: return acceptsQuickTransferToolSlot(itemStack);
            case 1: return acceptsQuickTransferAugmentSlot(itemStack);
            case 2: return acceptsQuickTransferEngineeringSlot(itemStack);
            case 3: return acceptsQuickTransferMaterialSlot(itemStack);
            case 4: return acceptsQuickTransferHammerSlot(itemStack);
            default: return false;
        }
    }

    public static ToolMaterial getToolMaterial(ItemStack itemStack) {
        if (itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ToolItem) {
            ToolMaterial mat = ((ToolItem)itemStack.getItem()).getMaterial();
            return mat;
        }
        return null;
    }

    public static List<ToolMaterial> updateToolMaterials() {
        List<ToolMaterial> mats = List.of(ToolMaterials.values());

        // TODO: figure out how to implement a tool material finder to be compatible with other mods
        // Loop through Modded tool material instances provided in a JSON and add?
        List<ToolMaterial> modMats = List.of(ModToolMaterials.values());
        mats.addAll(modMats);

        mats.sort((a, b) -> { return a.getMiningLevel() - b.getMiningLevel(); });
        return mats;
    }
}
