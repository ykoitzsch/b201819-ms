package event.product;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EventDeserializer implements Deserializer<ProductEvent> {

  @Override
  public void configure(Map configs, boolean isKey) {
    // TODO Auto-generated method stub
  }

  @Override
  public ProductEvent deserialize(String topic, byte[] data) {
    ObjectMapper mapper = new ObjectMapper();
    ProductEvent event = null;
    try {
      event = mapper.readValue(data, ProductEvent.class);

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
