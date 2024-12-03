package tfar.classicbar.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tfar.classicbar.ModUtils;

public class MessageExhaustionSync implements IMessage, IMessageHandler<MessageExhaustionSync, IMessage>
{
    private float exhaustionLevel;

    public MessageExhaustionSync(){}

    public MessageExhaustionSync(float exhaustionLevel)
    {
        this.exhaustionLevel = exhaustionLevel;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeFloat(exhaustionLevel);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        exhaustionLevel = buf.readFloat();
    }

    @Override
    public IMessage onMessage(final MessageExhaustionSync message, final MessageContext ctx)
    {
        // defer to the next game loop; we can't guarantee that Minecraft.thePlayer is initialized yet
        Minecraft.getMinecraft().addScheduledTask(() -> ModUtils.setExhaustion(NetworkHelper.getSidedPlayer(ctx), message.exhaustionLevel));
        return null;
    }
}
