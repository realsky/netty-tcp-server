package com.kl.nts.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Json Decoder fpr outgoing messages
 */
public class HttpDecoder extends MessageToMessageDecoder<HttpMessage> {
    private static final Logger LOG = Logger.getLogger(HttpDecoder.class.getName());

    @Override
    protected void decode(ChannelHandlerContext ctx, HttpMessage msg, List<Object> out) throws Exception {
        if (msg instanceof FullHttpRequest && msg.getDecoderResult().isSuccess()) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if (request.content().isReadable()) {
                ByteBuf buffer = new UnpooledByteBufAllocator(false).buffer(request.content().readableBytes());
                request.content().getBytes(0, buffer);
                out.add(buffer);
            } else { //TODO: http://localhost:8080/?json={"userName":"testUser","password":"testPassword"}
                //TODO: long pooling
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
                Map<String, List<String>> params = queryStringDecoder.parameters();
                if (params.containsKey("json")) {
                    List<String> jsons = params.get("json");
                    if (jsons.size() > 0) {
                        String json = jsons.get(0);
                        out.add(Unpooled.wrappedBuffer(json.getBytes()));
                    } else {
                        LOG.warn("JSON content is empty.");
                    }
                } else {
                    LOG.warn("JSON content is empty.");
                }
            }
        } else {
            LOG.error(msg.getDecoderResult().cause());
        }
    }
}
