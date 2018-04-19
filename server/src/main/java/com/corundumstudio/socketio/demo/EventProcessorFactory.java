package com.corundumstudio.socketio.demo;

import com.microsoft.azure.eventprocessorhost.IEventProcessorFactory;
import com.microsoft.azure.eventprocessorhost.PartitionContext;

public class EventProcessorFactory implements IEventProcessorFactory<EventProcessor> {

    private ISender sender;
    public EventProcessorFactory(ISender _sender) {
        sender = _sender;
    }

    public EventProcessor createEventProcessor(PartitionContext context) {
        return new EventProcessor(sender);
    }
}