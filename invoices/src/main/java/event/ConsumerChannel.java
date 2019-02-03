package event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ConsumerChannel {

  String INPUT = "consumerChannel";

  @Input(INPUT)
  SubscribableChannel inbound();
}
