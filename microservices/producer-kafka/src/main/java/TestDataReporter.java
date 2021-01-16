//Copyright (c) Microsoft Corporation. All rights reserved.
//Licensed under the MIT License.
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import java.sql.Timestamp;

public class TestDataReporter implements Runnable {

    private final int NUM_MESSAGES;
    private final String TOPIC;

    private Producer<Long, String> producer;

    public TestDataReporter(final Producer<Long, String> producer, String TOPIC, int NUM_MESSAGES) {
        this.producer = producer;
        this.TOPIC = TOPIC;
        this.NUM_MESSAGES = NUM_MESSAGES;
    }

    @Override
    public void run() {
        for(int i = 0; i < NUM_MESSAGES; i++) {                
            long time = System.currentTimeMillis();
            System.out.println("Test Data #" + i + " from thread #" + Thread.currentThread().getId());
            
            final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(TOPIC, time, "Test Data #" + i);
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        System.out.println(exception);
                        System.exit(1);
                    }
                }
            });
        }
        System.out.println("Finished sending " + NUM_MESSAGES + " messages from thread #" + Thread.currentThread().getId() + "!");
    }
}