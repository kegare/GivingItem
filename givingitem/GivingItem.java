package givingitem;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
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
	static final String version = "1.0.3";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		event.getModMetadata().version = version;
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