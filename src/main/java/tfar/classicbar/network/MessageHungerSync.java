package tfar.classicbar.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHungerSync implements IMessage, IMessageHandler<MessageHungerSync, IMessage>
{
    private int hungerLevel;

    public MessageHungerSync(){}

    public MessageHungerSync(int hungerLevel)
    {
        this.hungerLevel = hungerLevel;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(hungerLevel);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        hungerLevel = buf.readInt();
    }

    @Override
    public IMessage onMessage(final MessageHungerSync message, final MessageContext ctx)
    {
        // defer to the next game loop; we can't guarantee that Minecraft.thePlayer is initialized yet
        Minecraft.getMinecraft().addScheduledTask(() -> NetworkHelper.getSidedPlayer(ctx).getFoodStats().setFoodLevel(message.hungerLevel));
        return null;
    }
}