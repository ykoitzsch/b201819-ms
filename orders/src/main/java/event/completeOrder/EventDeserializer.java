package event.completeOrder;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EventDeserializer implements Deserializer<CompleteOrderEvent> {

  @Override
  public void configure(Map configs, boolean isKey) {
    // TODO Auto-generated method stub
  }

  @Override
  public CompleteOrderEvent deserialize(String topic, byte[] data) {
    ObjectMapper mapper = new ObjectMapper();
    CompleteOrderEvent event = null;
    try {
      event = mapper.readValue(data, CompleteOrderEvent.class);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return event;
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

}
