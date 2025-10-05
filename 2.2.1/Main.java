import java.io.*;
import java.util.*;

// Single Main class
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n==== Menu ====");
            System.out.println("1. Sum of Integers using Autoboxing/Unboxing");
            System.out.println("2. Student Serialization/Deserialization");
            System.out.println("3. Employee Management with Serialization");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> sumIntegers();
                case 2 -> studentSerializationDemo();
                case 3 -> employeeManagementDemo();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 0);

        sc.close();
    }

    // ===== Task 1 =====
    static void sumIntegers() {
        Integer a = 10; // autoboxing
        Integer b = 20;
        int sum = a + b; // unboxing
        System.out.println("Sum = " + sum);
    }

    // ===== Task 2 =====
    static void studentSerializationDemo() {
        try {
            Student s = new Student(1, "Alice", 85);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("student.ser"));
            oos.writeObject(s);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("student.ser"));
            Student s2 = (Student) ois.readObject();
            ois.close();

            System.out.println("Deserialized Student: " + s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Task 3 =====
    static void employeeManagementDemo() {
        Scanner sc = new Scanner(System.in);
        String empFile = "employees.ser";

        try {
            System.out.print("Enter employee ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter employee name: ");
            String name = sc.nextLine();
            System.out.print("Enter employee salary: ");
            double salary = sc.nextDouble();

            Employee e = new Employee(id, name, salary);

            // Append mode for serialization
            boolean append = new File(empFile).exists();
            ObjectOutputStream oos = append
                    ? new AppendableObjectOutputStream(new FileOutputStream(empFile, true))
                    : new ObjectOutputStream(new FileOutputStream(empFile));

            oos.writeObject(e);
            oos.close();

            // Read back all employees
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(empFile));
            System.out.println("\n--- Employee Records ---");
            while (true) {
                Employee emp = (Employee) ois.readObject();
                System.out.println(emp);
            }
        } catch (EOFException eof) {
            System.out.println("End of employee records.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===== Nested Classes =====
    static class Student implements Serializable {
        int id;
        String name;
        int marks;

        Student(int id, String name, int marks) {
            this.id = id;
            this.name = name;
            this.marks = marks;
        }

        public String toString() {
            return "Student{id=" + id + ", name='" + name + "', marks=" + marks + "}";
        }
    }

    static class Employee implements Serializable {
        int id;
        String name;
        double salary;

        Employee(int id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', salary=" + salary + "}";
        }
    }

    static class AppendableObjectOutputStream extends ObjectOutputStream {
        AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // avoid writing new header
        }
    }
}
