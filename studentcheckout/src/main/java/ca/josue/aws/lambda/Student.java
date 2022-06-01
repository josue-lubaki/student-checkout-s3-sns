package ca.josue.aws.lambda;

public class Student {
    public int rollNo;
    public String name;
    public int testScore;
    public char grade;

    public Student() {
    }

    public Student(int rollNo, String name, int testScore, char grade) {
        this.rollNo = rollNo;
        this.name = name;
        this.testScore = testScore;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student " + rollNo +
                ": \nname='" + name + '\'' +
                "\ntestScore=" + testScore +
                "\ngrade=" + grade;
    }
}
