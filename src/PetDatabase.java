import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PetDatabase {

    private static final int MAX_PETS = 5;
    private static final int MIN_AGE = 1;
    private static final int MAX_AGE = 20;
    private static final String DATA_FILE = "pets.txt";

    private static ArrayList<Pet> pets = new ArrayList<>();

    public static void main(String[] args) {
        loadFromFile(DATA_FILE);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

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
                    saveToFile(DATA_FILE);
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
        int addedCount = 0;

        while (true) {
            System.out.print("Enter pet name and age (or type 'done'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Error: " + input + " is not a valid input.");
                continue;
            }

            String name = parts[0];
            int age;

            try {
                age = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Error: " + parts[1] + " is not a valid age.");
                continue;
            }

            if (age < MIN_AGE || age > MAX_AGE) {
                System.out.println("Error: " + age + " is not a valid age.");
                continue;
            }

            if (pets.size() >= MAX_PETS) {
                System.out.println("Error: Database supports only 5 entries.");
                continue;
            }

            pets.add(new Pet(name, age));
            addedCount++;
        }

        System.out.println(addedCount + " pets added.");
    }

    private static void showPets() {
        System.out.println("+----+----------+-----+");
        System.out.printf("| %-2s | %-8s | %-3s |%n", "ID", "NAME", "AGE");
        System.out.println("+----+----------+-----+");

        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            System.out.printf("| %-2d | %-8s | %-3d |%n", i, pet.getName(), pet.getAge());
        }

        System.out.println("+----+----------+-----+");
        System.out.println(pets.size() + " rows in set.");
    }

    private static void searchPets(Scanner scanner) {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().toLowerCase().trim();
        boolean found = false;

        System.out.println("+----+----------+-----+");
        System.out.printf("| %-2s | %-8s | %-3s |%n", "ID", "NAME", "AGE");
        System.out.println("+----+----------+-----+");

        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            if (pet.getName().toLowerCase().contains(name)) {
                System.out.printf("| %-2d | %-8s | %-3d |%n", i, pet.getName(), pet.getAge());
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
        String input = scanner.nextLine().trim();
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
        String newName = scanner.nextLine().trim();

        System.out.print("Enter new age (leave blank to keep " + pet.getAge() + "): ");
        String newAgeInput = scanner.nextLine().trim();

        if (!newName.isEmpty()) {
            pet.setName(newName);
        }

        if (!newAgeInput.isEmpty()) {
            try {
                int newAge = Integer.parseInt(newAgeInput);

                if (newAge < MIN_AGE || newAge > MAX_AGE) {
                    System.out.println("Error: " + newAge + " is not a valid age.");
                } else {
                    pet.setAge(newAge);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: " + newAgeInput + " is not a valid age.");
            }
        }

        System.out.println("Pet updated.");
    }

    private static void removePet(Scanner scanner) {
        System.out.print("Enter pet ID to remove: ");
        String input = scanner.nextLine().trim();
        int id;

        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID " + input + " does not exist.");
            return;
        }

        if (id < 0 || id >= pets.size()) {
            System.out.println("Error: ID " + id + " does not exist.");
            return;
        }

        Pet removed = pets.remove(id);
        System.out.println("Removed pet: " + removed.getName());
    }

    private static void loadFromFile(String filename) {
        Path path = Path.of(filename);
        if (!Files.exists(path)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            for (String rawLine : lines) {
                if (pets.size() >= MAX_PETS) {
                    break;
                }

                String line = rawLine.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts;
                if (line.contains(",")) {
                    parts = line.split(",");
                } else {
                    parts = line.split("\\s+");
                }

                if (parts.length != 2) {
                    continue;
                }

                String name = parts[0].trim();
                String ageText = parts[1].trim();

                int age;
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException e) {
                    continue;
                }

                if (age < MIN_AGE || age > MAX_AGE) {
                    continue;
                }

                pets.add(new Pet(name, age));
            }
        } catch (IOException e) {
            System.out.println("Error: Could not load file.");
        }
    }

    private static void saveToFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();

        for (Pet pet : pets) {
            lines.add(pet.getName() + "," + pet.getAge());
        }

        try {
            Files.write(Path.of(filename), lines);
        } catch (IOException e) {
            System.out.println("Error: Could not save file.");
        }
    }
}
