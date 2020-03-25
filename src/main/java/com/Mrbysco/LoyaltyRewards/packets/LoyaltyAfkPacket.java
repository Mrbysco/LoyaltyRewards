package com.mrbysco.loyaltyrewards.packets;

import com.mrbysco.loyaltyrewards.handlers.AntiAfkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoyaltyAfkPacket implements IMessage
{
    private boolean isAFK;
	
	public LoyaltyAfkPacket() {}
	
	public LoyaltyAfkPacket(boolean afk)
	{
		this.isAFK = afk;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.isAFK = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.isAFK);
	}
	
	public static class PacketHandler implements IMessageHandler<LoyaltyAfkPacket, IMessage>
	{
		@Override
		public IMessage onMessage(LoyaltyAfkPacket message, MessageContext ctx) {
			AntiAfkHandler.changeAFK(ctx.getServerHandler().player, message.isAFK);
			return null;
		}
	}
}