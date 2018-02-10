package givingitem;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod
(
	modid = "givingitem",
	guiFactory = "givingitem.client.GivingGuiFactory",
	acceptedMinecraftVersions = "[1.12,)"
)
public class GivingItem
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.syncConfig();

		MinecraftForge.EVENT_BUS.register(this);
	}

	@NetworkCheckHandler
	public boolean netCheckHandler(Map<String, String> mods, Side side)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.getModID().equals("givingitem"))
		{
			Config.syncConfig();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityInteract(EntityInteract event)
	{
		World world = event.getWorld();

		if (!world.isRemote)
		{
			EntityPlayer player = event.getEntityPlayer();
			ItemStack held = event.getItemStack();
			Entity entity = event.getTarget();

			if (held.isEmpty() || !player.isSneaking() || !entity.isEntityAlive() || held.getItem() instanceof ItemBucket)
			{
				return;
			}

			EnumHand hand = event.getHand();
			ItemStack itemstack = held.copy();

			if (Config.giveToMob && entity instanceof EntityLivingBase)
			{
				EntityLivingBase target = (EntityLivingBase)entity;

				if (Config.swapWithMob)
				{
					player.setHeldItem(hand, target.getHeldItem(hand));
					target.setHeldItem(hand, itemstack);

					playGiveSound(player, target);

					event.setCanceled(true);
				}
				else if (target.getHeldItem(hand).isEmpty())
				{
					player.setHeldItem(hand, ItemStack.EMPTY);
					target.setHeldItem(hand, itemstack);

					playGiveSound(player, target);

					event.setCanceled(true);
				}
			}
			else if (Config.giveToPlayer && entity instanceof EntityPlayer)
			{
				EntityPlayer target = (EntityPlayer)entity;

				if (Config.swapWithPlayer)
				{
					player.setHeldItem(hand, target.getHeldItem(hand));
					target.setHeldItem(hand, itemstack);

					playGiveSound(player, target);

					event.setCanceled(true);
				}
				else
				{
					if (player.inventory.addItemStackToInventory(held))
					{
						playGiveSound(player, target);

						event.setCanceled(true);
					}
					else
					{
						held = player.getHeldItem(hand);

						if (held.isEmpty() || held.getCount() <= 0)
						{
							player.setHeldItem(hand, itemstack);
						}
					}
				}
			}
		}
	}

	private void playGiveSound(Entity entity, Entity target)
	{
		entity.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
		target.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
	}
}