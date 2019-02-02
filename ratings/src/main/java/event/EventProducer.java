package event;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventProducer {

  private final Logger log = LoggerFactory.getLogger(EventProducer.class);

  private Producer<Long, RatingEvent> producer;

  private String topic;

  private Properties kafkaProperties;

  public EventProducer() {
    topic = "rating.topics";
    kafkaProperties = new Properties();
    kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
    kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventSerializer.class.getName());
    producer = new KafkaProducer<>(kafkaProperties);
  }

  public void send(RatingEvent event) {
    ProducerRecord<Long, RatingEvent> record = new ProducerRecord<>(topic, event);
    producer.send(record);
    producer.close();
  }

}
