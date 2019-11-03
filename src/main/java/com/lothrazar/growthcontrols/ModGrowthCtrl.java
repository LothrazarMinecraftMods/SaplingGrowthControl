package com.lothrazar.growthcontrols;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.growthcontrols.config.ConfigHandler;
import com.lothrazar.growthcontrols.item.ItemGrow;
import com.lothrazar.growthcontrols.setup.ClientProxy;
import com.lothrazar.growthcontrols.setup.IProxy;
import com.lothrazar.growthcontrols.setup.ServerProxy;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.IForgeRegistry;

@Mod("growthcontrols")
public class ModGrowthCtrl {

  public static final String MODID = "growthcontrols";
  private String certificateFingerprint = "@FINGERPRINT@";
  public static ConfigHandler config;
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
  public static final Logger LOGGER = LogManager.getLogger();

  public ModGrowthCtrl() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(new SaplingDespawnGrowth());
    MinecraftForge.EVENT_BUS.register(this);
    config = new ConfigHandler(ConfigHandler.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
  }

  private void setup(final FMLCommonSetupEvent event) {}

  @SubscribeEvent
  public static void onFingerprintViolation(FMLFingerprintViolationEvent event) {
    // https://tutorials.darkhax.net/tutorials/jar_signing/
    String source = (event.getSource() == null) ? "" : event.getSource().getName() + " ";
    String msg = MODID + "Invalid fingerprint detected! The file " + source + "may have been tampered with. This version will NOT be supported by the author!";
    //   System.out.println(msg);
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties().group(ItemGroup.BREWING);
      IForgeRegistry<Item> r = event.getRegistry();
      r.register(new ItemGrow(properties).setRegistryName("growth_detector"));
    }
  }
}