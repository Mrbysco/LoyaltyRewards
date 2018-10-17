package com.Mrbysco.LoyaltyRewards.packets;

import com.Mrbysco.LoyaltyRewards.toasts.ToastReward;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoyaltyToastPacket implements IMessage
{
    private ItemStack stack;
    private String textTranslation;
	
	public LoyaltyToastPacket() {}
	
	public LoyaltyToastPacket(ItemStack stack, String text)
	{
		this.stack = stack;
		this.textTranslation = text;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.stack = ByteBufUtils.readItemStack(buf);
		this.textTranslation = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, stack);
		ByteBufUtils.writeUTF8String(buf, textTranslation);
	}
	
	public static class PacketHandler implements IMessageHandler<LoyaltyToastPacket, IMessage>
	{
		@Override
		public IMessage onMessage(LoyaltyToastPacket message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> 
			{
				System.out.println(message.stack);
				System.out.println(message.textTranslation);
				Minecraft.getMinecraft().getToastGui().add(new ToastReward(message.stack, message.textTranslation));
			});
			return null;
		}
	}
}