package net.mcft.copy.chickennuggies;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ChickenNuggies.MOD_ID)
public class ChickenNuggies
{
    public static final String MOD_ID = "chickennuggies";
    
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> RAW_CHICKEN_NUGGIE = ITEMS.register("raw_chicken_nuggie", () ->
        new Item(new Item.Properties().group(ItemGroup.FOOD).food(
            new Food.Builder().fastToEat().saturation(0.8F).effect(() -> new EffectInstance(Effects.HUNGER, 30 * 20), 0.30F).build())));
    public static final RegistryObject<Item> COOKED_CHICKEN_NUGGIE = ITEMS.register("cooked_chicken_nuggie", () ->
        new Item(new Item.Properties().group(ItemGroup.FOOD).food(
            new Food.Builder().fastToEat().hunger(1).saturation(1.5F).build())));
    public static final RegistryObject<Item> BOX_OF_NUGGIES = ITEMS.register("bowl_of_nuggies", () ->
        new ContainerItem(new Item.Properties().group(ItemGroup.FOOD).containerItem(Items.BOWL).maxStackSize(16).food(
            new Food.Builder().hunger(7).saturation(8.5F).build())));
    
    public ChickenNuggies() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static class ContainerItem extends Item
    {
        public ContainerItem(Properties properties) {
            super(properties);
        }
        
        @Override
        public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
            ItemStack container = stack.getContainerItem();
            super.onItemUseFinish(stack, world, entity);
            
            // If this was the last item in the stack, replace it with the container item.
            if (stack.isEmpty()) return container;
            
            // If entity is a non-creative player, give them back the container item.
            PlayerEntity player = (entity instanceof PlayerEntity) ? (PlayerEntity)entity : null;
            if ((player != null) && !player.abilities.isCreativeMode)
                if (!player.addItemStackToInventory(container))
                    player.dropItem(container, false);
            
            return stack;
        }
    }
}
