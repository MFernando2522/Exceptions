import java.io.*;
import java.util.*;

/**
 * Main class that manages student records.
 * This class demonstrates file I/O and exception handling in Java.
 */
public class StudentRecordsManager {

    /**
     * Main method to run the program.
     */
    public static void main(String[] args) {
        StudentRecordsManager manager = new StudentRecordsManager();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter input filename: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter output filename: ");
        String outputFile = scanner.nextLine();

        try {
            manager.processStudentRecords(inputFile, outputFile);
        } catch (Exception e) {
            System.err.println("Error processing student records: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    /**
     * Process student records from an input file and write results to an output file.
     */
    public void processStudentRecords(String inputFile, String outputFile) {
        try {
            List<Student> students = readStudentRecords(inputFile);
            writeResultsToFile(students, outputFile);
            System.out.println("Student records processed successfully. Output written to " + outputFile);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            System.err.println("Please check the file path and name, and ensure the file exists.");
        } catch (IOException e) {
            System.err.println("I/O error occurred: " + e.getMessage());
            System.err.println("Please check file permissions and ensure sufficient disk space.");
        }
    }

    /**
     * Read student records from a file and convert them to Student objects.
     */
    public List<Student> readStudentRecords(String filename) throws IOException {
        List<Student> students = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 6) {
                        throw new ArrayIndexOutOfBoundsException("Insufficient fields in line");
                    }

                    String studentId = parts[0].trim();
                    String name = parts[1].trim();
                    int[] grades = new int[4];

                    for (int i = 0; i < 4; i++) {
                        grades[i] = Integer.parseInt(parts[i + 2].trim());
                        if (grades[i] < 0 || grades[i] > 100) {
                            throw new InvalidGradeException("Grade " + grades[i] + " is outside valid range (0-100)");
                        }
                    }

                    students.add(new Student(studentId, name, grades));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing grade at line " + lineNumber + ": " + e.getMessage());
                } catch (InvalidGradeException e) {
                    System.err.println("Invalid grade at line " + lineNumber + ": " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Invalid format at line " + lineNumber + ": " + e.getMessage());
                }
            }
        }

        return students;
    }

    /**
     * Write processed student results to an output file.
     */
    public void writeResultsToFile(List<Student> students, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.println("Student Records Report");
            writer.println("=====================");
            writer.println();

            // Write student information
            for (Student student : students) {
                writer.println(student.toString());
                writer.println("---------------------");
            }

            // Calculate and write statistics
            int totalStudents = students.size();
            double sum = 0;
            int[] gradeCounts = new int[5]; // A, B, C, D, F

            for (Student student : students) {
                sum += student.getAverageGrade();
                char letter = student.getLetterGrade();
                switch (letter) {
                    case 'A': gradeCounts[0]++; break;
                    case 'B': gradeCounts[1]++; break;
                    case 'C': gradeCounts[2]++; break;
                    case 'D': gradeCounts[3]++; break;
                    case 'F': gradeCounts[4]++; break;
                }
            }

            double classAverage = totalStudents > 0 ? sum / totalStudents : 0;

            writer.println("Class Statistics:");
            writer.println("----------------");
            writer.println("Total Students: " + totalStudents);
            writer.println("Class Average: " + String.format("%.2f", classAverage));
            writer.println("Grade Distribution:");
            writer.println("A: " + gradeCounts[0]);
            writer.println("B: " + gradeCounts[1]);
            writer.println("C: " + gradeCounts[2]);
            writer.println("D: " + gradeCounts[3]);
            writer.println("F: " + gradeCounts[4]);
        }
    }
}