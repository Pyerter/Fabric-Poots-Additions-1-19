package net.pyerter.pootsadditions.item.custom;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.block.ModBlockTags;
import net.pyerter.pootsadditions.item.StackDependentAttributeModifierItem;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractEngineeredTool extends Item implements Vanishable, StackDependentAttributeModifierItem {
    public static final String ATTRIBUTES_UPDATED_NBT_ID = "pootsadditions.attributesUpdated";
    public static final String PREVIOUSLY_WORKING_NBT_ID = "pootsadditions.previouslyWorking";

    public static final Map<net.minecraft.util.Pair<ToolMaterial, ToolType>, AbstractEngineeredTool> materialToToolDictionary = new HashMap<>();
    public static final List<net.minecraft.util.Pair<ToolMaterial, ToolType>> toolTypePairs = new ArrayList<>();

    public enum ToolType {
        AXE,
        HOE,
        PICKAXE,
        SHOVEL,
        SWORD,
        NADA
    }

    public static final int DAMAGE_INCREMENT = 1;
    protected List<TagKey<Block>> effectiveBlocksList;

    /**
     * Notice that these instance variables are not final. That implies that these can change
     * over the lifespan of this item.
     * In most instances, these should not be changed. The exception is when a multi-tool is desired,
     * and upgrading this multi-tool's tier and stats may be useful.
     */
    protected Settings settings;

    protected ToolMaterial material;

    protected float miningSpeed;
    protected float attackDamage;
    protected float attackSpeed;
    protected Multimap<EntityAttribute, EntityAttributeModifier> unsuitableAttributeModifiers;
    protected Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    protected boolean attributesUpdated = false;
    /* Instance variables */

    Map<Block, Block> STRIPPED_BLOCKS = (new ImmutableMap.Builder()).put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD).put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG).build();
    Map<Block, BlockState> PATH_STATES = Maps.newHashMap((new ImmutableMap.Builder()).put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.DIRT, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.PODZOL, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.MYCELIUM, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.getDefaultState()).build());
    Map<Block, com.mojang.datafixers.util.Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of((itemUsageContext) -> {
        return true;
    }, createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));

    protected AbstractEngineeredTool(float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        this(attackDamage, attackSpeed, material, List.of(effectiveBlocks), settings);
    }

    protected AbstractEngineeredTool(float attackDamage, float attackSpeed, ToolMaterial material, List<TagKey<Block>> effectiveBlocks, Settings settings) {
        super(settings.maxDamageIfAbsent(material.getDurability()));

        this.settings = settings;
        this.material = material;
        effectiveBlocksList = effectiveBlocks;
        this.miningSpeed = material.getMiningSpeedMultiplier();
        this.attackDamage = attackDamage + material.getAttackDamage();
        this.attackSpeed = attackSpeed;

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", (double)this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", (double)attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> unsuitableBuilder = ImmutableMultimap.builder();
        unsuitableBuilder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", (double)-attackDamage, EntityAttributeModifier.Operation.ADDITION));
        unsuitableBuilder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", (double)5, EntityAttributeModifier.Operation.ADDITION));
        this.unsuitableAttributeModifiers = unsuitableBuilder.build();
    }

    protected AbstractEngineeredTool(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
        this(attackDamage, attackSpeed, material, List.of(), settings);
    }

    protected AbstractEngineeredTool(AbstractEngineeredTool tool) {
        this(tool.attackDamage, tool.attackSpeed, tool.material, tool.effectiveBlocksList, tool.settings);
    }

    protected static void registerTool(AbstractEngineeredTool tool, ToolMaterial mat, ToolType toolType) {
        Optional<net.minecraft.util.Pair<ToolMaterial, ToolType>> typePair = toolTypePairs.stream().filter(p -> { return p.getLeft().equals(mat) && p.getRight().equals(toolType); }).findFirst();
        if (typePair.isPresent() && !materialToToolDictionary.containsKey(typePair))
            materialToToolDictionary.put(typePair.get(), tool);
        else {
            net.minecraft.util.Pair<ToolMaterial, ToolType> newTypePair = new net.minecraft.util.Pair<>(mat, toolType);
            toolTypePairs.add(newTypePair);
            materialToToolDictionary.put(newTypePair, tool);
        }
    }

    public static ToolType getToolType(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty())
            return ToolType.NADA;

        Item item = itemStack.getItem();
        if (item instanceof AxeItem)
            return ToolType.AXE;
        else if (item instanceof ShovelItem)
            return ToolType.SHOVEL;
        else if (item instanceof  SwordItem)
            return ToolType.SWORD;
        else if (item instanceof HoeItem)
            return ToolType.HOE;
        else if (item instanceof PickaxeItem)
            return ToolType.PICKAXE;

        return ToolType.NADA;
    }

    public static AbstractEngineeredTool getRegisteredTool(ToolMaterial mat, ToolType toolType) {
        Optional<net.minecraft.util.Pair<ToolMaterial, ToolType>> typePair = toolTypePairs.stream().filter(p -> { return p.getLeft() == mat && p.getRight() == toolType; }).findFirst();
        if (typePair.isPresent())
            return materialToToolDictionary.containsKey(typePair.get()) ? materialToToolDictionary.get(typePair.get()) : null;
        else
            return null;
    }

    public void resetAttributeModifiers() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", (double)this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", (double)attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    /** ToolItem methods **/
    public ToolMaterial getMaterial() {
        return this.material;
    }

    public int getEnchantability() {
        return this.material.getEnchantability();
    }

    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }
    /* ToolItem methods */

    /** Mining Tool methods **/
    public List<TagKey<Block>> getEffectiveBlocksList() {
        return effectiveBlocksList;
    }

    private boolean stateInEffectiveBlocks(BlockState state) {
        for (TagKey<Block> tagKey: effectiveBlocksList) {
            if (state.isIn(tagKey)) {
                return true;
            }
        }
        return false;
    }

    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return stateInEffectiveBlocks(state) && willNotBreak(stack) ? this.miningSpeed : 1.0F;
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return tryDamageItem(stack, attacker);
    }

    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
            tryDamageItem(stack, miner);
        }

        return true;
    }

    private boolean tryDamageItem(ItemStack stack, LivingEntity miner) {
        if (willNotBreak(stack)) {
            stack.damage(DAMAGE_INCREMENT, miner, (e) -> { e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND); });
            setPreviouslyWorking(stack, true);
            if (!willNotBreak(stack)) {
                setAttributesUpdated(stack, true);
                setPreviouslyWorking(stack, false);
                onItemNoLongerSuitable(stack, miner);
            }
            return true;
        }
        return false;
    }

    public boolean tryRepairItem(ItemStack stack) {
        stack.setDamage(0);
        setAttributesUpdated(stack, true);
        setPreviouslyWorking(stack, true);
        onItemNowSuitable(stack);
        return true;
    }

    public void onItemNoLongerSuitable(ItemStack stack, LivingEntity miner) {
        PootsAdditions.logInfo("No longer suitable!");
    }

    public void onItemNowSuitable(ItemStack stack) {
        PootsAdditions.logInfo("Item now suitable!");
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        // Reset the value of attributes updated, since the most recent update was applied
        setAttributesUpdated(stack, false);
        if (willNotBreak(stack))
            return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
        else
            return slot == EquipmentSlot.MAINHAND ? this.unsuitableAttributeModifiers : super.getAttributeModifiers(slot);
    }

    @Override
    public boolean getNeedsAttributeRefresh(EquipmentSlot slot, ItemStack stack) {
        return getAttributesUpdated(stack);
    }

    @Override
    public void setToRefreshAttributes(EquipmentSlot slot, ItemStack stack) {
        setAttributesUpdated(stack, true);
    }

    public boolean getBooleanNbt(ItemStack stack, String id, boolean defaultBoolean) {
        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();
            if (nbt.contains(id)) {
                return nbt.getBoolean(id);
            } else {
                nbt.putBoolean(id, defaultBoolean);
                return defaultBoolean;
            }
        } else {
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean(id, defaultBoolean);
            stack.setNbt(nbt);
            return defaultBoolean;
        }
    }

    public boolean setBooleanNbt(ItemStack stack, String id, boolean value) {
        if (stack.hasNbt()) {
            stack.getNbt().putBoolean(id, value);
        } else {
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean(id, value);
            stack.setNbt(nbt);
        }
        return value;
    }

    public boolean getAttributesUpdated(ItemStack stack) {
        return getBooleanNbt(stack, ATTRIBUTES_UPDATED_NBT_ID, false);
    }

    public boolean setAttributesUpdated(ItemStack stack, boolean updated) {
        return setBooleanNbt(stack, ATTRIBUTES_UPDATED_NBT_ID, updated);
    }

    public boolean getPreviouslyWorking(ItemStack stack) {
        return getBooleanNbt(stack, PREVIOUSLY_WORKING_NBT_ID, true);
    }

    public boolean setPreviouslyWorking(ItemStack stack, boolean working) {
        return setBooleanNbt(stack, PREVIOUSLY_WORKING_NBT_ID, working);
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public boolean isSuitableFor(BlockState state) {
        int i = this.getMaterial().getMiningLevel();
        if (i < 4 && state.isIn(ModBlockTags.NEEDS_SAPPHIRE_TOOL)) {
            return false;
        } else if (i < 3 && state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return i < 1 && state.isIn(BlockTags.NEEDS_STONE_TOOL) ? false : stateInEffectiveBlocks(state);
        }
    }

    public boolean willNotBreak(ItemStack stack) {
        boolean wontBreak = stack.getDamage() + DAMAGE_INCREMENT < stack.getMaxDamage();
        return wontBreak;
    }
    /* Mining Tool Methods */

    /** Extended Item Tool **/

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return anitcipatedUseOnBlockResult(context) == ActionResult.SUCCESS ? ActionResult.PASS : ActionResult.PASS;
    }

    public ActionResult anitcipatedUseOnBlockResult(ItemUsageContext context) {
        if (willNotBreak(context.getStack()))
            return ActionResult.SUCCESS;
        else
            return ActionResult.FAIL;
    }
    /* Extended Item Tool */

    /** Axe Item Methods **/
    public ActionResult useAxeActionOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        Optional<BlockState> optional = this.getStrippedState(blockState);
        Optional<BlockState> optional2 = Oxidizable.getDecreasedOxidationState(blockState);
        Optional<BlockState> optional3 = Optional.ofNullable((Block)((BiMap)HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()).get(blockState.getBlock())).map((block) -> {
            return block.getStateWithProperties(blockState);
        });
        ItemStack itemStack = context.getStack();
        Optional<BlockState> optional4 = Optional.empty();
        if (optional.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            optional4 = optional;
        } else if (optional2.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
            optional4 = optional2;
        } else if (optional3.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, 3004, blockPos, 0);
            optional4 = optional3;
        }

        if (optional4.isPresent()) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            world.setBlockState(blockPos, (BlockState)optional4.get(), 11);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, (BlockState)optional4.get()));
            if (playerEntity != null) {
                tryDamageItem(itemStack, playerEntity);
            }

            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    private Optional<BlockState> getStrippedState(BlockState state) {
        return Optional.ofNullable((Block)STRIPPED_BLOCKS.get(state.getBlock())).map((block) -> {
            return (BlockState)block.getDefaultState().with(PillarBlock.AXIS, (Direction.Axis)state.get(PillarBlock.AXIS));
        });
    }
    /* Axe Item Methods */

    /** Shovel Item Methods **/
    public ActionResult useShovelActionOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (context.getSide() == Direction.DOWN) {
            return ActionResult.PASS;
        } else {
            PlayerEntity playerEntity = context.getPlayer();
            BlockState blockState2 = (BlockState)PATH_STATES.get(blockState.getBlock());
            BlockState blockState3 = null;
            if (blockState2 != null && world.getBlockState(blockPos.up()).isAir()) {
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                blockState3 = blockState2;
            } else if (blockState.getBlock() instanceof CampfireBlock && (Boolean)blockState.get(CampfireBlock.LIT)) {
                if (!world.isClient()) {
                    world.syncWorldEvent((PlayerEntity)null, 1009, blockPos, 0);
                }

                CampfireBlock.extinguish(context.getPlayer(), world, blockPos, blockState);
                blockState3 = (BlockState)blockState.with(CampfireBlock.LIT, false);
            }

            if (blockState3 != null) {
                if (!world.isClient) {
                    world.setBlockState(blockPos, blockState3, 11);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, blockState3));
                    if (playerEntity != null) {
                        tryDamageItem(context.getStack(), playerEntity);
                    }
                }

                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.PASS;
            }
        }
    }
    /* Shovel Item Methods */

    /** Hoe Item Methods **/
    public ActionResult useHoeActionOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = (Pair)TILLING_ACTIONS.get(world.getBlockState(blockPos).getBlock());
        if (pair == null) {
            return ActionResult.PASS;
        } else {
            Predicate<ItemUsageContext> predicate = (Predicate)pair.getFirst();
            Consumer<ItemUsageContext> consumer = (Consumer)pair.getSecond();
            if (predicate.test(context)) {
                PlayerEntity playerEntity = context.getPlayer();
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClient) {
                    consumer.accept(context);
                    if (playerEntity != null) {
                        tryDamageItem(context.getStack(), playerEntity);
                    }
                }

                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.PASS;
            }
        }
    }

    public static Consumer<ItemUsageContext> createTillAction(BlockState result) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), result));
        };
    }

    public static Consumer<ItemUsageContext> createTillAndDropAction(BlockState result, ItemConvertible droppedItem) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), result));
            Block.dropStack(context.getWorld(), context.getBlockPos(), context.getSide(), new ItemStack(droppedItem));
        };
    }
    /* Hoe Item Methods */

    /** Damaging Item Methods **/



    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    /* Damaging Item Methods */
}
