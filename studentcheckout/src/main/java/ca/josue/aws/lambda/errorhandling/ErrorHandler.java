package ca.josue.aws.lambda.errorhandling;

import ca.josue.aws.lambda.GradeCalculator;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(GradeCalculator.class);

    public void handler(SNSEvent event){
        event.getRecords().forEach(record -> {
            logger.info("Dead Letter Queue Event : " + record.toString());
        });
    }
}
