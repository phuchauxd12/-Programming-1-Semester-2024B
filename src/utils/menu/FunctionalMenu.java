package utils.menu;

abstract class FunctionalMenu extends Menu {
    protected final MainMenu mainMenu;

    FunctionalMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    protected void mainMenu() {
        int option = 100;
        do {
            displayMenu();
            option = getOption(option, input);
            Runnable action = menuActions.get(option);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
        mainMenu.mainMenu();
    }
}
