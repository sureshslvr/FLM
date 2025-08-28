package comparableorcomparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("David", 20, 1));
        students.add(new Student("Bob", 22, 4));
        students.add(new Student("nani", 21, 3));
        students.add(new Student("suresh", 23, 2));
        System.out.println(students);
        // Sorting students by name using Comparable
        Collections.sort(students);
        System.out.println(students);
        // Sorting students by age using Comparator
        Collections.sort(students, (s1, s2) -> Integer.compare(s1.getAge(), s2.getAge()));
        System.out.println(students);
        //sorting students by id using class IdComparator
        Collections.sort(students,new IdComaparator());
        System.out.println(students);

        List<Employee> employees=new ArrayList<>();
        employees.add(new Employee(29,"suresh","abc"));
        employees.add(new Employee(9,"nani","xyz"));

        //sort using comparator and lamda expression
        employees.sort((e1,e2)->Integer.compare(e1.getId(),e2.getId()));
        System.out.println(employees);
        employees.sort((e1,e2)->e1.getCompany().compareTo(e2.getCompany()));
        System.out.println(employees);
    }

}
