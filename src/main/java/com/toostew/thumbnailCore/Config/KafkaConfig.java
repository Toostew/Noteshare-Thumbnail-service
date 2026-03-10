package com.toostew.thumbnailCore.Config;

import com.toostew.thumbnailCore.Entities.ThumbnailRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.port}")
    private String kafkaPort;


    @Bean
    public ConsumerFactory<String, ThumbnailRequest> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPort);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "thumbnail-group-1");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 1. Create the deserializer manually
        JacksonJsonDeserializer<ThumbnailRequest> deserializer = new JacksonJsonDeserializer<>(ThumbnailRequest.class);

        // 2. Tell it to ignore the __TypeId__ header from the Producer
        deserializer.setUseTypeHeaders(false);

        // 3. Trust your local package
        deserializer.addTrustedPackages("com.toostew.thumbnailCore.Entities.ThumbnailRequest");

        //key deserialize
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);



        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ThumbnailRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ThumbnailRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());


        //the number of threads that can run at the same time for this web app
        //we can have a max thread count that is less than or equal to the number of partitions
        //set in the bean in the kafka producer (noteshare main app)
        factory.setConcurrency(3);

        return factory;
    }
}
