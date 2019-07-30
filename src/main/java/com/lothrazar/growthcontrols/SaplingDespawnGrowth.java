package com.lothrazar.growthcontrols;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SaplingDespawnGrowth {

  public static List<String> oakBiomes = new ArrayList<>();
  public static List<Integer> spruceBiomes = new ArrayList<Integer>();
  public static List<Integer> birchBiomes = new ArrayList<Integer>();
  public static List<Integer> jungleBiomes = new ArrayList<Integer>();
  public static List<Integer> darkoakBiomes = new ArrayList<Integer>();
  public static List<Integer> acaciaBiomes = new ArrayList<Integer>();
  public static boolean drop_on_failed_growth = true;
  public static boolean plantDespawningSaplings;

  public SaplingDespawnGrowth() {
  }



  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    IWorld world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    boolean treeAllowedToGrow = false;
    if (b == Blocks.SPRUCE_SAPLING)// this may not always be true: such as trees
    // added by Mods, so not a vanilla tree, but
    // throwing same event
    {
      Biome biome = world.getBiome(pos);
      String biomeId = biome.getRegistryName().toString();
      treeAllowedToGrow = false;//from biome
      if (treeAllowedToGrow == false) {
        event.setResult(Event.Result.DENY);
        // overwrite the sapling. - we could set to Air first, but dont
        // see much reason to
        world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState(),  3);
        if (drop_on_failed_growth) {
          dropItemStackInWorld((World) world, pos, new ItemStack(b));
        }
      }
    }// else a tree grew that was added by some mod
  }
  //
  //	@SubscribeEvent
  //	public void onItemExpireEvent(ItemExpireEvent event) {
  //		if (plantDespawningSaplings == false) {
  //			return;
  //		}
  //
  //		EntityItem entityItem = event.getEntityItem();
  //		Entity entity = event.getEntity();
  //		ItemStack is = entityItem.getEntityItem();
  //		if (is == null) {
  //			return;
  //		}// has not happened in the wild, yet
  //
  //		Block blockhere = entity.worldObj.getBlockState(entityItem.getPosition()).getBlock();
  //		Block blockdown = entity.worldObj.getBlockState(entityItem.getPosition().down()).getBlock();
  //
  //		if (blockhere == Blocks.air && blockdown == Blocks.dirt || // includes
  //																	// podzol
  //																	// and such
  //		blockdown == Blocks.grass) {
  //			// plant the sapling, replacing the air and on top of dirt/plantable
  //
  //			if (Block.getBlockFromItem(is.getItem()) == Blocks.sapling)
  //				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.sapling.getStateFromMeta(is.getItemDamage()));
  //			else if (Block.getBlockFromItem(is.getItem()) == Blocks.red_mushroom)
  //				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.red_mushroom.getDefaultState());
  //			else if (Block.getBlockFromItem(is.getItem()) == Blocks.brown_mushroom)
  //				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.brown_mushroom.getDefaultState());
  //
  //		}
  //	}

  public static ItemEntity dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack) {
    ItemEntity entityItem = new ItemEntity(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);
    if (worldObj.isRemote == false)// do not spawn a second 'ghost' one on
    {
      worldObj.addEntity(entityItem);
    }
    return entityItem;
  }
}
