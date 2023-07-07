package ddd.plus.showcase.wms.domain.common.publisher;

public interface IEventPublisher {
    void publish(Object anyEvent);
}
