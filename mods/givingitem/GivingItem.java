package mods.givingitem;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "GivingItem", name = "GivingItem", version = "1.0.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class GivingItem
{
	@Instance("GivingItem")
	public static GivingItem instance;

	@Init
	public void init(FMLInitializationEvent event)
	{
		CommonProxy.proxy.load();
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