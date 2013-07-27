package kegare.givingitem;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod
(
	modid = "kegare.givingitem"
)
@NetworkMod
(
	clientSideRequired = false,
	serverSideRequired = false
)
public class GivingItem
{
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
			ItemStack itemstack = player.getCurrentEquippedItem();

			if (player.isSneaking() && itemstack != null)
			{
				if (!target.inventory.addItemStackToInventory(itemstack))
				{
					target.dropPlayerItem(itemstack.copy());
					itemstack.stackSize = 0;
				}

				player.playSound("random.pop", 1.0F, 1.0F);
				target.playSound("random.pop", 1.0F, 1.0F);
			}
		}
	}
}