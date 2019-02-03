package com.jhipster.bachelor.invoices.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;

import event.ConsumerChannel;

@EnableBinding(value = {Processor.class, ConsumerChannel.class})
public class MessagingConfiguration {

}
