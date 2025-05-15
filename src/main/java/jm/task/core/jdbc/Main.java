package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {


    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        // реализуйте алгоритм здесь
        userService.saveUser("Александр", "Лукашенко", (byte) 70);
        userService.saveUser("Дональд", "Трамп", (byte) 78);
        userService.saveUser("Владимир", "Путин", (byte) 72);
        userService.saveUser("Владимир", "Зеленский", (byte) 47);

//       userService.removeUserById(2);

       userService.getAllUsers();

//
   //      userService.cleanUsersTable();
//
//        userService.dropUsersTable();

    }
}
