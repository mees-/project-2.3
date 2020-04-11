package connection.eventHandlers;

public class EventPayload {
    private EventType type;

    EventPayload(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
