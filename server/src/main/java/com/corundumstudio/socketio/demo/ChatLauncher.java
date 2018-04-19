package com.corundumstudio.socketio.demo;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.*;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;

import java.util.concurrent.ExecutionException;

public class ChatLauncher {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);
        SocketConfig socketConfig = config.getSocketConfig();
        socketConfig.setReuseAddress(true);

        String consumerGroupName = "$Default";
        String namespaceName = "titan-events";
        String eventHubName = "titan-hub";
        String sasKeyName = "RootManageSharedAccessKey";
        String sasKey = "EyWH2SjZfm3PgOjwj5rcIGt+P76Q+HqqgFy3kzBt8U8=";
        String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=titaneventsstorage;AccountKey=a9aiaNrbLv0BZ8SboF1qF/CudP3qVBTd5Nd+Z/ybYauFza4F/GBZYPZWfLixdi/T0uK1+LnCdn5bhy6uIn6DEw==;EndpointSuffix=core.windows.net";
        String storageContainerName = "titaneventcontainer";
        String hostNamePrefix = "_titanEvents_";

        ConnectionStringBuilder eventHubConnectionString = new ConnectionStringBuilder()
                .setNamespaceName(namespaceName)
                .setEventHubName(eventHubName)
                .setSasKeyName(sasKeyName)
                .setSasKey(sasKey);

        EventProcessorHost host = new EventProcessorHost(
                EventProcessorHost.createHostName(hostNamePrefix),
                eventHubName,
                consumerGroupName,
                eventHubConnectionString.toString(),
                storageConnectionString,
                storageContainerName);

        System.out.println("Registering host named " + host.getHostName());
        EventProcessorOptions options = new EventProcessorOptions();
        options.setExceptionNotification(new ErrorNotificationHandler());

        final SocketIOServer server = new SocketIOServer(config);
        server.addEventListener("chatevent", ChatObject.class, new Listener(new Sender(server)));

        server.start();

        listenEventHub(host, options, new Sender(server));

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }

    private static void listenEventHub(EventProcessorHost host, EventProcessorOptions options, Sender sender) throws InterruptedException, ExecutionException {

        host.registerEventProcessorFactory(new EventProcessorFactory(sender), options)
                .whenComplete((unused, e) ->
                {
                    if (e != null) {
                        System.out.println("Failure while registering: " + e.toString());
                        if (e.getCause() != null) {
                            System.out.println("Inner exception: " + e.getCause().toString());
                        }
                    }
                }).thenAccept((unused) ->
        {
            System.out.println("Press enter to stop.");
            try {
                System.in.read();
            } catch (Exception e) {
                System.out.println("Keyboard read failed: " + e.toString());
            }
        })
                .thenCompose((unused) ->
                {
                    return host.unregisterEventProcessor();
                })
                .exceptionally((e) ->
                {
                    System.out.println("Failure while unregistering: " + e.toString());
                    if (e.getCause() != null) {
                        System.out.println("Inner exception: " + e.getCause().toString());
                    }
                    return null;
                })
                .get();

        System.out.println("End of sample");
    }
}


