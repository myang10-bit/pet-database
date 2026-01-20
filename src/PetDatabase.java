import java.util.ArrayList;
import java.util.Scanner;

public class PetDatabase {

    private static ArrayList<Pet> pets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addPets(scanner);
                    break;
                case "2":
                    showPets();
                    break;
                case "3":
                    searchPets(scanner);
                    break;
                case "4":
                    updatePet(scanner);
                    break;
                case "5":
                    removePet(scanner);
                    break;
                case "6":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nPet Database Menu");
        System.out.println("1) Add pets");
        System.out.println("2) View pets");
        System.out.println("3) Search pets");
        System.out.println("4) Update pet");
        System.out.println("5) Remove pet");
        System.out.println("6) Exit");
    }

    private static void addPets(Scanner scanner) {
        int count = 0;

        while (true) {
            System.out.print("Enter pet name and age (or type 'done'): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid input. Format: name age");
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
        System.out.println("+----+----------+-----+");
        System.out.printf("| %-2s | %-8s | %-3s |\n", "ID", "NAME", "AGE");
        System.out.println("+----+----------+-----+");

        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            System.out.printf("| %-2d | %-8s | %-3d |\n", i, pet.getName(), pet.getAge());
        }

        System.out.println("+----+----------+-----+");
        System.out.println(pets.size() + " rows in set.");
    }

    private static void searchPets(Scanner scanner) {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().toLowerCase();
        boolean found = false;

        System.out.println("+----+----------+-----+");
        System.out.printf("| %-2s | %-8s | %-3s |\n", "ID", "NAME", "AGE");
        System.out.println("+----+----------+-----+");

        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            if (pet.getName().toLowerCase().contains(name)) {
                System.out.printf("| %-2d | %-8s | %-3d |\n", i, pet.getName(), pet.getAge());
                found = true;
            }
        }

        System.out.println("+----+----------+-----+");

        if (!found) {
            System.out.println("No pets found.");
        }
    }

    private static void updatePet(Scanner scanner) {
        System.out.print("Enter pet ID to update: ");
        String input = scanner.nextLine();
        int id;

        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        if (id < 0 || id >= pets.size()) {
            System.out.println("Pet not found.");
            return;
        }

        Pet pet = pets.get(id);

        System.out.print("Enter new name (leave blank to keep '" + pet.getName() + "'): ");
        String newName = scanner.nextLine();

        System.out.print("Enter new age (leave blank to keep " + pet.getAge() + "): ");
        String newAgeInput = scanner.nextLine();

        if (!newName.isEmpty()) {
            pet.setName(newName);
        }

        if (!newAgeInput.isEmpty()) {
            try {
                int newAge = Integer.parseInt(newAgeInput);
                pet.setAge(newAge);
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Keeping old age.");
            }
        }

        System.out.println("Pet updated.");
    }

    private static void removePet(Scanner scanner) {
        System.out.print("Enter pet ID to remove: ");
        String input = scanner.nextLine();
        int id;

        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        if (id < 0 || id >= pets.size()) {
            System.out.println("Pet not found.");
            return;
        }

        Pet removed = pets.remove(id);
        System.out.println("Removed pet: " + removed.getName());
    }
}
