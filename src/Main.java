import data.user.UserDatabase;
import user.Client;
import user.Employee;
import user.User;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
        Employee employee1 = new Employee("employee1", "pass2", "Employee One", "Address 2", 987654321, "employee1@example.com", User.ROLE.EMPLOYEE);
        Client client1 = new Client("client1", "pass1", "Client One", "Address 1", 1234567890, "client1@example.com", 50000);
        UserDatabase.createDatabase();
//        UserDatabase.addUser(employee1);
//        UserDatabase.addUser(client1);
        var listUser = UserDatabase.loadUsers();
        System.out.println(listUser);
        }
    }
