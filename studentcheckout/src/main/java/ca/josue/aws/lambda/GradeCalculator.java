package ca.josue.aws.lambda;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GradeCalculator {
    public static final String STUDENT_CHECKOUT_TOPIC = System.getenv("STUDENT_CHECKOUT_TOPIC");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();

    public void handler(S3Event event) {
        event.getRecords().forEach(record -> {
            S3ObjectInputStream s3ObjectInputStream = s3.getObject(
                    record.getS3().getBucket().getName(),
                    record.getS3().getObject().getKey()
            ).getObjectContent();

            try {
                List<Student> students = Arrays
                        .asList(objectMapper.readValue(s3ObjectInputStream, Student[].class));
                s3ObjectInputStream.close();

                // publish message to SNS
                publishMessageToSNS(students);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void publishMessageToSNS(List<Student> students) {
        students.stream()
                .map(GradeCalculator::calculateGrade)
                .forEach(student -> {
                    try {
                        sns.publish(
                            STUDENT_CHECKOUT_TOPIC,
                            objectMapper.writeValueAsString(student)
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    // solve the grade of the student
    private static Student calculateGrade(Student student) {
        // below 70% -> C, above 80% -> B and above 80% -> A
        student.grade = student.testScore < 70 ? 'C' : student.testScore < 90 ? 'B' : 'A';
        return student;
    }
}
