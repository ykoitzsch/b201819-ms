package event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ConsumerChannelT {

  String INPUT = "consumerChannelT";

  @Input(INPUT)
  SubscribableChannel inbound();
}
