package com.kl.nts.task;

import com.kl.nts.json.AuthMessage;
import com.kl.nts.json.Token;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

public class ClientTask {
    private static final Logger LOG = Logger.getLogger(ClientTask.class.getName());

    private Channel channel;

    public ClientTask(Channel channel) {
        this.channel = channel;
    }

    public void acceptRequest(final AuthMessage auth) {
        try {
            LOG.info("New execution started in thread pool.");
            Token token = new Token(auth.getUserName().toUpperCase() + ":" + auth.getPassword().toUpperCase());
            channel.writeAndFlush(token);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void unregisteredChannel() {
        LOG.info("Channel unregistered.");
    }
}
