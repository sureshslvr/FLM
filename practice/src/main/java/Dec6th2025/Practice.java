package Dec6th2025;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Practice {

    public static void main(String[] args) {
        //lets say we have list of employees in db how will you get the list of emp names using java 8
        //we have find all employees from db and get the list of employees the using streams we can get list of emp names

        List<Employee> employeeList=new ArrayList<>();
        for(int i=0;i<=10;i++){
            Employee emp=new Employee();
            emp.setEmpId(i);
            emp.setEmpName("emp "+i);
            employeeList.add(emp);
        }

        List<String> empnames = employeeList.parallelStream().map(Employee::getEmpName).collect(Collectors.toList());
        System.out.println(empnames);


    }
}


