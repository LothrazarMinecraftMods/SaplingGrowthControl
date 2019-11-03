package com.lothrazar.growthcontrols.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
//import com.lothrazar.examplemod.ExampleMod;
import com.lothrazar.growthcontrols.ModGrowthCtrl;
import com.lothrazar.growthcontrols.UtilString;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class ConfigHandler {

  public ConfigHandler(ForgeConfigSpec spec, Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    spec.setConfig(configData);
  }

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  public static final String DELIM = "->";
  private static ForgeConfigSpec.BooleanValue dropFailedGrowth;
  public static ForgeConfigSpec.ConfigValue<List<String>> SAPLING_BIOMES;
  public static ForgeConfigSpec.ConfigValue<List<String>> CROP_BIOMES;
  public static ForgeConfigSpec COMMON_CONFIG;

  public boolean getdropFailedGrowth() {
    return dropFailedGrowth.get();
  }
  //  Beets (Beta vulgaris) are a cool-season, root vegetable, which means they grow in the cool weather of spring and fall.
  //EMPTY!!!
  //the following are empty
  //
  //??cactus?? 

  // desert lakes maybe what could be here
  //
  //
  // 
  //reeds? as well as other places? 
  //  
  //
  //  nothing? cactus?
  //
  //
  //"minecraft:ice_spikes"
  static {
    initConfig();
  }

  public void getEmptyBiomes() {
    PlayerEntity p = Minecraft.getInstance().player;// ModGrowthCtrl.proxy.getClientWorld();
    List<String> valid = new ArrayList<>();
    for (Iterator<Biome> iter = ForgeRegistries.BIOMES.iterator(); iter.hasNext();) {
      Biome b = iter.next();
      //find any biomes with NOTHING
      List<String> has = this.getGrowthsForBiome(b);
      if (has == null || has.isEmpty()) {
        valid.add(b.getRegistryName().toString());
      }
      else {
        //        System.out.println(b.getRegistryName() + "  " + has);
      }
    }
    String lst = String.join(", ", valid);
    if (valid.size() == 0) {
      lst = "none";
    }
    UtilString.chatMessage(p, "Barren: " + lst);
  }

  private static void initConfig() {
    //TODO: reverse allcapas and finalstatic
    COMMON_BUILDER.comment("General settings").push(ModGrowthCtrl.MODID);
    //
    dropFailedGrowth = COMMON_BUILDER.comment("Drop sapling item on failed growth").define("dropOnFailedGrow", true);
    //
    List<String> configstuff = new ArrayList<>();
    configstuff.add(Blocks.ACACIA_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:savanna", "minecraft:shattered_savanna", "minecraft:shattered_savanna_plateau", "minecraft:savanna_plateau", "minecraft:modified_wooded_badlands_plateau",
        "minecraft:wooded_badlands_plateau"
    }));
    configstuff.add(Blocks.BIRCH_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:birch_forest", "minecraft:forest", "minecraft:birch_forest_hills", "minecraft:tall_birch_forest", "minecraft:tall_birch_hills" }));
    configstuff.add(Blocks.SPRUCE_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:taiga", "minecraft:giant_tree_taiga", "minecraft:snowy_tundra", "minecraft:taiga_hills", "minecraft:snowy_taiga", "minecraft:snowy_taiga_hills",
        "minecraft:giant_tree_taiga_hills" }));
    configstuff.add(Blocks.OAK_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:forest", "minecraft:dark_forest", "minecraft:wooded_mountains", "minecraft:wooded_hills", "minecraft:swamp", "minecraft:swamp_hills", "minecraft:flower_forest"
    }));
    configstuff.add(Blocks.DARK_OAK_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:dark_forest", "minecraft:dark_forest_hills", "minecraft:flower_forest"
    }));
    configstuff.add(Blocks.JUNGLE_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:jungle_edge", "minecraft:jungle", "minecraft:jungle_hills", "minecraft:modified_jungle", "minecraft:bamboo_jungle", "minecraft:bamboo_jungle_hills",
        "minecraft:modified_jungle_edge", "minecraft:modified_jungle" }));
    //unsupported type: map
    SAPLING_BIOMES = COMMON_BUILDER.comment(
        "Map growable block to CSV list of biomes no spaces, -> in between.  It SHOULD be fine to add modded saplings. An empty list means the sapling can gro nowhere.  Delete the key-entry for a sapling to let it grow everywhere.")
        .define("SaplingBlockToBiome", configstuff);
    //
    configstuff = new ArrayList<>();
    configstuff.add(Blocks.WHEAT.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:plains", "minecraft:swamp", "minecraft:beach"
    }));
    configstuff.add(Blocks.CARROTS.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:taiga", "minecraft:savanna", "minecraft:savanna*", "minecraft:*savanna", "minecraft:shattered_savanna_plateau", "minecraft:sunflower_plains",
        "minecraft:giant_tree_taiga_hills" }));
    configstuff.add(Blocks.POTATOES.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:taiga", "minecraft:snowy*", "minecraft:taiga*", "minecraft:*forest", "minecraft:dark_forest_hills", "minecraft:*taiga", "minecraft:*mountains",
        "minecraft:giant_tree_taiga_hills", "minecraft:mountain_edge"
    }));
    configstuff.add(Blocks.BEETROOTS.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:taiga", "minecraft:forest", "minecraft:swamp", "minecraft:flower_forest", "minecraft:birch_forest", "minecraft:birch*", "minecraft:tall_birch_forest", "minecraft:tall_birch_hills",
        "minecraft:dark_forest", "minecraft:dark_forest_hills"
    }));
    final String[] cactua = new String[] {
        "minecraft:desert", "minecraft:desert_hills", "minecraft:badlands", "minecraft:badlands*", "minecraft:*badlands"
    };
    configstuff.add(Blocks.CACTUS.getRegistryName().toString() + DELIM + String.join(",", cactua));
    final String[] reeds = new String[] {
        "minecraft:stone_shore", "minecraft:desert_lakes"
    };
    configstuff.add(Blocks.SUGAR_CANE.getRegistryName().toString() + DELIM + String.join(",", reeds));
    final String[] purpur = new String[] {
        "minecraft:small_end_islands", "minecraft:end*", "minecraft:the_end",
        "minecraft:the_void"
    };
    configstuff.add(Blocks.CHORUS_FLOWER.getRegistryName().toString() + DELIM + String.join(",", purpur));
    configstuff.add(Blocks.CHORUS_PLANT.getRegistryName().toString() + DELIM + String.join(",", purpur));
    final String[] mushrooms = new String[] {
        "minecraft:mushroom*", "minecraft:mushroom_field_shore", "minecraft:nether", "minecraft:mesa*", "minecraft:mesa", "minecraft:small_end_islands", "minecraft:end*", "minecraft:the_end",
        "minecraft:the_void"
    };
    configstuff.add(Blocks.BROWN_MUSHROOM.getRegistryName().toString() + DELIM + String.join(",", mushrooms));
    configstuff.add(Blocks.RED_MUSHROOM.getRegistryName().toString() + DELIM + String.join(",", mushrooms));
    configstuff.add(Blocks.COCOA.getRegistryName().toString() + DELIM + String.join(",",
        new String[] {
            "minecraft:jungle_edge", "minecraft:jungle", "minecraft:jungle_hills", "minecraft:modified_jungle", "minecraft:bamboo_jungle", "minecraft:bamboo_jungle_hills",
            "minecraft:modified_jungle_edge", "minecraft:modified_jungle" }));
    configstuff.add(Blocks.MELON_STEM.getRegistryName().toString() + DELIM + String.join(",",
        new String[] {
            "minecraft:ocean", "minecraft:mesa*", "minecraft:mesa", "minecraft:*ocean", "minecraft:river", "minecraft:frozen_ocean", "minecraft:frozen_river", "minecraft:beach",
            "minecraft:deep_ocean", "minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:cold_ocean", "minecraft:deep_warm_ocean", "minecraft:deep_lukewarm_ocean",
            "minecraft:deep_cold_ocean", "minecraft:deep_frozen_ocean", "minecraft:jungle", "minecraft:jungle*", "minecraft:modified_jungle", "minecraft:bamboo_jungle",
            "minecraft:bamboo_jungle_hills", "minecraft:modified_jungle_edge", "minecraft:modified_jungle" }));
    configstuff.add(Blocks.PUMPKIN_STEM.getRegistryName().toString() + DELIM + String.join(",",
        new String[] {
            "minecraft:extreme*"
            // // and "mesa"
            , "minecraft:taiga", "minecraft:snowy*", "minecraft:taiga*", "minecraft:dark_forest_hills", "minecraft:*taiga", "minecraft:giant_tree_taiga_hills" }));
    CROP_BIOMES = COMMON_BUILDER.comment("Map growable block to CSV list of biomes no spaces, -> in between.  It SHOULD be fine to add modded blocks. An empty list means the crop"
        + " can gro nowhere.  Delete the key-entry for a crop to let it grow everywhere.")
        .define("CropBlockToBiome", configstuff);
    //YES: it is here actually
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public Map<String, List<String>> getMapBiome(ForgeConfigSpec.ConfigValue<List<String>> conf) {
    final Map<String, List<String>> mapInit = new HashMap<>();
    for (String splitme : conf.get()) {
      try {
        final String[] split = splitme.split(DELIM);
        final String blockId = split[0];
        final String[] biomes = split[1].split(",");
        mapInit.put(blockId, Arrays.asList(biomes));
      }
      catch (Exception e) {
        //bad config sucks to be you
        ModGrowthCtrl.LOGGER.error("Error reading bad config value :" + splitme, e);
      }
    } //TODO: DONT BE LAZY
    return mapInit;
  }

  public List<String> getBiomesCombined(Block block) {
    List<String> found = getBiomesForCrop(block);
    List<String> saplings = this.getBiomesForSapling(block);
    if (found == null) {
      found = new ArrayList<>();
    }
    if (saplings == null) {
      saplings = new ArrayList<>();
    }
    found.addAll(saplings);
    return found;
  }

  public List<String> getBiomesForCrop(Block block) {
    Map<String, List<String>> mapInit = this.getMapBiome(CROP_BIOMES);
    String key = block.getRegistryName().toString();
    if (mapInit.containsKey(key) == false) {
      //null means no list set, so everything allowed
      return null;
    }
    //my list is allowed
    return mapInit.get(key);
  }

  public List<String> getBiomesForSapling(Block block) {
    Map<String, List<String>> mapInit = this.getMapBiome(SAPLING_BIOMES);
    String key = block.getRegistryName().toString();
    if (mapInit.containsKey(key) == false) {
      //null means no list set, so everything allowed
      return null;
    }
    //my list is allowed
    return mapInit.get(key);
  }

  public List<String> getGrowthsForBiome(Biome biome) {
    List<String> result = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : this.getMapBiome(ConfigHandler.CROP_BIOMES).entrySet()) {
      String block = entry.getKey();
      List<String> biomes = entry.getValue();
      if (UtilString.isInList(biomes, biome.getRegistryName())) {
        result.add(block);
      }
    }
    for (Map.Entry<String, List<String>> entry : this.getMapBiome(ConfigHandler.SAPLING_BIOMES).entrySet()) {
      String block = entry.getKey();
      List<String> biomes = entry.getValue();
      if (UtilString.isInList(biomes, biome.getRegistryName())) {
        result.add(block);
      }
    }
    return result;
  }
}