package event.productOrder;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EventSerializer implements Serializer<ProductOrderEvent> {

  private final Logger log = LoggerFactory.getLogger(EventSerializer.class);

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    // TODO Auto-generated method stub

  }

  @Override
  public byte[] serialize(String topic, ProductOrderEvent data) {
    byte[] retVal = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      retVal = objectMapper.writeValueAsString(data).getBytes();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return retVal;
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

}
