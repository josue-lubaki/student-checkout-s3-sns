package ca.josue.aws.lambda;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReportGenerator {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handler(SNSEvent event){
        event.getRecords().forEach(snsRecord -> {
            try {
                Student student = objectMapper
                        .readValue(snsRecord.getSNS().getMessage(), Student.class);

                System.out.println("Student Checkout Event Received: " + student);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
