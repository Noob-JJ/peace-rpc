package remote.client;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelManager {

    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();


    public static Channel get(String address) {

        Channel channel = channelMap.get(address);

        if (Objects.nonNull(channel) && channel.isActive()) {
            return channel;
        } else {
            return null;
        }
    }

    public static void addChannel(String address, Channel channel) {
        channelMap.put(address, channel);
    }
}
