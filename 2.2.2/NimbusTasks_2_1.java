import java.io.*;
import java.util.*;

class Student implements Serializable {
    int studentID;
    String name, grade;

    Student(int studentID, String name, String grade) {
        this.studentID = studentID;
        this.name = name;
        this.grade = grade;
    }

    void display() {
        System.out.println("Student ID: " + studentID + ", Name: " + name + ", Grade: " + grade);
    }
}

class Employee implements Serializable {
    int id;
    String name, designation;
    double salary;

    Employee(int id, String name, String designation, double salary) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.salary = salary;
    }

    void display() {
        System.out.println("ID: " + id + ", Name: " + name + ", Designation: " + designation + ", Salary: " + salary);
    }
}

class AppendableObjectOutputStream extends ObjectOutputStream {
    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }
    @Override
    protected void writeStreamHeader() throws IOException {
        File file = new File("employees.ser");
        if(file.length() == 0) {
            super.writeStreamHeader();
        } else {
            reset();
        }
    }
}

public class NimbusTasks_2_1 {
    static Scanner sc = new Scanner(System.in);
    static String empFile = "employees.ser";
    static String studentFile = "student.ser";

    public static void main(String[] args) {
        while(true) {
            System.out.println("\n--- Nimbus Java Tasks Menu ---");
            System.out.println("1. Sum of Integers Using Autoboxing/Unboxing");
            System.out.println("2. Serialize/Deserialize Student Object");
            System.out.println("3. Employee Management System");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1: sumIntegers(); break;
                case 2: studentSerialization(); break;
                case 3: employeeManagement(); break;
                case 4: System.out.println("Exiting..."); sc.close(); System.exit(0);
                default: System.out.println("Invalid choice! Try again.");
            }
        }
    }

    static void sumIntegers() {
        System.out.println("\nEnter integers separated by space:");
        String[] inputs = sc.nextLine().split(" ");
        ArrayList<Integer> numbers = new ArrayList<>();
        for(String s : inputs) {
            numbers.add(Integer.parseInt(s));
        }
        int sum = 0;
        for(Integer n : numbers) sum += n;
        System.out.println("Total sum: " + sum);
    }

    static void studentSerialization() {
        System.out.print("\nEnter Student ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Grade: ");
        String grade = sc.nextLine();

        Student s = new Student(id, name, grade);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(studentFile))) {
            oos.writeObject(s);
            System.out.println("Student object serialized successfully.");
        } catch(IOException e) { e.printStackTrace(); }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(studentFile))) {
            Student deserializedStudent = (Student) ois.readObject();
            System.out.println("Deserialized Student data:");
            deserializedStudent.display();
        } catch(IOException | ClassNotFoundException e) { e.printStackTrace(); }
    }

    static void employeeManagement() {
        while(true) {
            System.out.println("\n--- Employee Management Menu ---");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    System.out.print("Enter Employee ID: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Designation: ");
                    String desig = sc.nextLine();
                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble(); sc.nextLine();
                    Employee emp = new Employee(id, name, desig, salary);

                    try (FileOutputStream fos = new FileOutputStream(empFile, true);
                         AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos)) {
                        oos.writeObject(emp);
                        System.out.println("Employee added successfully.");
                    } catch(IOException e) { e.printStackTrace(); }
                    break;

                case 2:
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(empFile))) {
                        System.out.println("\nEmployee Records:");
                        while(true) {
                            Employee e = (Employee) ois.readObject();
                            e.display();
                        }
                    } catch(EOFException e) {
                    } catch(IOException | ClassNotFoundException e) { e.printStackTrace(); }
                    break;

                case 3: return;
                default: System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
