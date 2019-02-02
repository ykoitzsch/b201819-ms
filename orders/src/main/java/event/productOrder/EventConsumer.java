package event.productOrder;

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

import event.EventConfig;

public class EventConsumer {

  private final Logger log = LoggerFactory.getLogger(EventProducer.class);

  private final KafkaConsumer<Long, ProductOrderEvent> consumer;

  private Properties kafkaProperties;

  public EventConsumer() {
    kafkaProperties = new Properties();
    kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, EventConfig.SERVERS);
    kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, EventConfig.GROUP_ID);
    kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
    kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, EventDeserializer.class.getName());
    consumer = new KafkaConsumer<Long, ProductOrderEvent>(kafkaProperties);
    consumer.subscribe(Arrays.asList(EventConfig.PRODUCT_ORDER_TOPIC));
    consumer.poll(Duration.ofMillis(100));
  }

  public List<ProductOrderEvent> consume() {
    List<ProductOrderEvent> consumedEvents = new ArrayList<>();
    int attempts = 0;
    while (attempts < 100) {
      attempts++ ;
      consumer.seekToBeginning(consumer.assignment());
      ConsumerRecords<Long, ProductOrderEvent> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<Long, ProductOrderEvent> record : records) {
        consumedEvents.add(record.value());
      }
      if (records.count() > 0L)
        break;
    }
    consumer.close();
    return consumedEvents;
  }

}
