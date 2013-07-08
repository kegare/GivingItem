package givingitem;

import java.net.URL;
import java.util.Properties;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod
(
	modid = "givingitem",
	name = "GivingItem",
	version = GivingItem.version,
	dependencies = "required-after:Forge"
)
@NetworkMod
(
	clientSideRequired = false,
	serverSideRequired = false
)
public class GivingItem
{
	static final String version = "1.0.4";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		try
		{
			URL url = new URL("https://dl.dropbox.com/u/51943112/kegare/kegare.info");
			Properties mappings = new Properties();

			mappings.load(url.openConnection().getInputStream());

			ModMetadata metadata = event.getModMetadata();
			metadata.version = version;
			metadata.description += " (Latest: " + mappings.getProperty("givingitem.latest", version) + ")";
			metadata.url = mappings.getProperty("givingitem.url", metadata.url);
		}
		catch (Exception e)
		{
			return;
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@ForgeSubscribe
	public void onEntityInteract(EntityInteractEvent event)
	{
		if (event.entityPlayer instanceof EntityPlayerMP && event.target instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
			EntityPlayerMP target = (EntityPlayerMP)event.target;
			ItemStack current = player.getCurrentEquippedItem();

			if (player.isSneaking() && current != null)
			{
				if (!target.inventory.addItemStackToInventory(current))
				{
					target.dropPlayerItem(ItemStack.copyItemStack(current));
					current.stackSize = 0;
				}

				player.playSound("random.pop", 1.0F, 1.0F);
				target.playSound("random.pop", 1.0F, 1.0F);
			}
		}
	}
}