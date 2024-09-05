package Controllers;

import Model.Admin;
import Model.Librarian;
import Model.Manager;
import Model.User;

public class ManageUsersController {

    private UserController userController;
    private AdminController adminController;
    private ManagerController managerController;
    private LibrarianController librarianController;

    public ManageUsersController(UserController userController, AdminController adminController, ManagerController managerController, LibrarianController librarianController){
        this.userController = userController;
        this.adminController = adminController;
        this.managerController = managerController;
        this.librarianController = librarianController;
    }


    public void updateUser(User updatedUser) {
        userController.writeUsers();
        if (updatedUser.getAccessLvl().equalsIgnoreCase("Librarian")) {
            librarianController.updateLibrarian((Librarian) updatedUser);
        } else if (updatedUser.getAccessLvl().equalsIgnoreCase("Manager")) {
            managerController.updateManager((Manager) updatedUser);
        } else if (updatedUser.getAccessLvl().equalsIgnoreCase("Admin")) {
            adminController.updateAdmin((Admin) updatedUser);
        }
    }

    public void deleteUser(User updatedUser) {
        userController.writeUsers();
        if (updatedUser.getAccessLvl().equalsIgnoreCase("Librarian")) {
            librarianController.removeLibrarian((Librarian) updatedUser);
        } else if (updatedUser.getAccessLvl().equalsIgnoreCase("Manager")) {
            managerController.removeManager((Manager) updatedUser);
        } else if (updatedUser.getAccessLvl().equalsIgnoreCase("Admin")) {
            adminController.removeAdmin((Admin) updatedUser);
        }
    }


}


