package mods.givingitem;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide = "mods.givingitem.ClientProxy", serverSide = "mods.givingitem.CommonProxy")
	public static CommonProxy proxy;

	public void load()
	{
		MinecraftForge.EVENT_BUS.register(GivingItem.instance);
	}
}