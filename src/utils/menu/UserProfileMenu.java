package utils.menu;

import user.User;
import utils.CommonFunc;
import utils.UserSession;

import java.util.Scanner;


public class UserProfileMenu extends Menu {

    public UserProfileMenu() {
        super();
        initializeMenu(MenuOption.MANAGER); // All users have the same menu option
    }

    @Override
    protected void initializeMenu(MenuOption menuOption) {
        menuItems.put(1,"View User Info");
        menuItems.put(2, "Modify Account");
        menuItems.put(3, "Delete Account");
        menuItems.put(0, "Exit");

        menuActions.put(1, this::UserInfo);
        menuActions.put(2, this::modifyAccount);
        menuActions.put(3, this::deleteAccount);
        menuActions.put(0, this::exit);
    }

    private void deleteAccount() {
        try {
            CommonFunc.addActivityLogForCurrentUser("Delete Account");
            User.deleteUser(currentUser.getUserID());
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private void modifyAccount() {
        String userID = currentUser.getUserID();
        Scanner input = new Scanner(System.in);
        System.out.println("Update User");
        boolean continueUpdate = true;
        int updateOption = 0;
        do {
            System.out.println("What would you like to update?");
            System.out.println("1. User Name");
            System.out.println("2. Password");
            System.out.println("3. Name");
            System.out.println("4. Address");
            System.out.println("5. Phone Number");
            System.out.println("6. Email");
            System.out.println("7. Date of Birth");
            System.out.println("8. Exit");
            updateOption = MainMenu.getOption(updateOption, input);
            if (updateOption == 8) {
                continueUpdate = false;
                continue;
            }
            try {
                CommonFunc.addActivityLogForCurrentUser("Modify Account");
                User.modifyUser(userID, updateOption);
            } catch (Exception e) {
                System.out.println("Error updating user: " + e.getMessage());
            }
        } while (continueUpdate);
    }

    private  void UserInfo(){
        System.out.println(UserSession.getCurrentUser().getUserInfo());
    }
}


