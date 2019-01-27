package event;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventConsumer {

  private final Logger log = LoggerFactory.getLogger(EventProducer.class);

  private final KafkaConsumer<Long, CustomerEvent> consumer;

  private Properties kafkaProperties;

  private String topic;

  public EventConsumer() {
    topic = "c.topics";
    kafkaProperties = new Properties();
    kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "customer-group-id");
    kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
    kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, EventDeserializer.class.getName());
    consumer = new KafkaConsumer<Long, CustomerEvent>(kafkaProperties);
    consumer.subscribe(Arrays.asList(topic));
    consumer.poll(Duration.ofMillis(100));
  }

  public List<CustomerEvent> consume() {
    List<CustomerEvent> consumedEvents = new ArrayList<>();
    int attempts = 0;
    while (attempts < 100) {
      attempts++ ;
      consumer.seekToBeginning(consumer.assignment());
      ConsumerRecords<Long, CustomerEvent> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<Long, CustomerEvent> record : records) {
        consumedEvents.add(record.value());
      }
      if (records.count() > 0L)
        break;
    }
    consumer.close();
    return consumedEvents;
  }

}
