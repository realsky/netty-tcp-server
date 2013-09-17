package com.kl.nts.server;

import com.kl.nts.json.Token;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

/**
 * Json Encoder for incoming messages
 */
public class JsonEncoder extends MessageToMessageEncoder<Token> {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, Token token, List<Object> out) throws Exception {
        byte[] buffer = jsonMapper.writeValueAsBytes(token);
        if (buffer.length == 0) {
            return;
        }
        out.add(Unpooled.wrappedBuffer(buffer));
    }
}
