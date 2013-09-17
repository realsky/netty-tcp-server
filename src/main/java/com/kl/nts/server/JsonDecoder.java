package com.kl.nts.server;

import com.kl.nts.json.AuthMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

/**
 * Json Decoder fpr outgoing messages
 */
public class JsonDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg == null) {
            return;
        }
        byte[] buffer = new byte[msg.readableBytes()];
        msg.getBytes(0, buffer);
        AuthMessage auth = jsonMapper.readValue(buffer, AuthMessage.class);
        out.add(auth);
    }
}
