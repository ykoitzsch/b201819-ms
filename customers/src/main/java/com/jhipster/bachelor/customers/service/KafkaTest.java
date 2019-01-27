package com.jhipster.bachelor.customers.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;

import event.CustomerEvent;
import event.EventDeserializer;

public class KafkaTest {

  public static void main(String[] args) {
    runConsumer();
  }

  static void runConsumer() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, EventDeserializer.class.getName());

    KafkaConsumer<Long, CustomerEvent> consumer = new KafkaConsumer<Long, CustomerEvent>(props);
    consumer.subscribe(Arrays.asList("c.topics"));
    consumer.poll(Duration.ofMillis(1000));
    consumer.seekToBeginning(consumer.assignment());

    boolean foundRecords = false;
    while ( !foundRecords) {
      consumer.seekToBeginning(consumer.assignment());
      ConsumerRecords<Long, CustomerEvent> records = consumer.poll(Duration.ofMillis(1000));
      if (records.count() > 0L)
        foundRecords = true;
      System.out.println(".");
      for (ConsumerRecord<Long, CustomerEvent> record : records) {

        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
      }
    }

  }
}
