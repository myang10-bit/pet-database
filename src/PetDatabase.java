import java.util.ArrayList;
import java.util.Scanner;

public class PetDatabase {

    private static ArrayList<Pet> pets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Pet Database Program.");
            System.out.println("What would you like to do?");
            System.out.println("1) View all pets");
            System.out.println("2) Add more pets");
            System.out.println("3) Search pets by name");
            System.out.println("4) Search pets by age");
            System.out.println("5) Exit program");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showPets();
                    break;
                case "2":
                    addPets(scanner);
                    break;
                case "3":
                    searchByName(scanner);
                    break;
                case "4":
                    searchByAge(scanner);
                    break;
                case "5":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void addPets(Scanner scanner) {
        int count = 0;

        while (true) {
            System.out.print("add pet (name, age): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid input. Please enter: name age");
                continue;
            }

            String name = parts[0];
            int age;

            try {
                age = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Age must be a number.");
                continue;
            }

            pets.add(new Pet(name, age));
            count++;
        }

        System.out.println(count + " pets added.");
    }

    private static void showPets() {
        printTableHeader();

        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            System.out.printf("| %-3d | %-10s | %-4d |\n", i, pet.getName(), pet.getAge());
        }

        printTableFooter(pets.size());
    }

    private static void searchByName(Scanner scanner) {
        System.out.print("Enter name to search: ");
        String query = scanner.nextLine().trim().toLowerCase();

        printTableHeader();

        int matches = 0;
        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            if (pet.getName().toLowerCase().contains(query)) {
                System.out.printf("| %-3d | %-10s | %-4d |\n", i, pet.getName(), pet.getAge());
                matches++;
            }
        }

        printTableFooter(matches);
    }

    private static void searchByAge(Scanner scanner) {
        System.out.print("Enter age to search: ");
        String input = scanner.nextLine().trim();

        int ageQuery;
        try {
            ageQuery = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Age must be a number.");
            return;
        }

        printTableHeader();

        int matches = 0;
        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            if (pet.getAge() == ageQuery) {
                System.out.printf("| %-3d | %-10s | %-4d |\n", i, pet.getName(), pet.getAge());
                matches++;
            }
        }

        printTableFooter(matches);
    }

    private static void printTableHeader() {
        System.out.println("+----------------------+");
        System.out.printf("| %-3s | %-10s | %-4s |\n", "ID", "NAME", "AGE");
        System.out.println("+----------------------+");
    }

    private static void printTableFooter(int rows) {
        System.out.println("+----------------------+");
        System.out.println(rows + " rows in set.");
    }
}
