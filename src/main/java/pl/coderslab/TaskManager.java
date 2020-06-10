package pl.coderslab;

import consoleColors.ConsoleColors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TaskManager {

    private static String[][] tasks;

    public static void main(String[] args) {
        tasks = loadTasks();

        Scanner scan = new Scanner(System.in);

        wyswiOpcje();

        while (scan.hasNextLine()) {

            String opcja = scan.next();

            switch (opcja) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask(tasks, getTheNumber());
                    break;
                case "list":
                    listTask(tasks);
                    break;
                case "exit":
                    exit(tasks);
                    break;
                default:
                    System.out.println();
                    System.out.println("Wybierz poprawną opcję:");
                    break;
            }
            wyswiOpcje();

        }

    }



    public static int getTheNumber() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Wybierz numer zadania do skasowania:");
        boolean jestWieksze = false;
        String a = scanner.nextLine();

        while (!jestWieksze) {
            if (NumberUtils.isParsable(a)) {
                int temp = Integer.parseInt(a);
                if (temp >= 0) {
                    jestWieksze = true;
                    break;
                }
            }
            System.out.println("Podano zła liczbę. Podaj liczbę równa bądź wiekszą niż 0");
            jestWieksze = false;
            a = scanner.nextLine();
        }
        return Integer.parseInt(a);
    }

    public static String[][] loadTasks() {

        Path plik = Paths.get("tasks.csv");
        if (!Files.exists(plik)) {
            System.out.println("Plik nie istnieje!");
            System.exit(-1);
        }
        String[][] arr = null;
        try {
            List<String> lista = Files.readAllLines(plik);
            arr = new String[lista.size()][lista.get(0).split(",").length];

            for (int i = 0; i < lista.size(); i++) {
                String[] podzial = lista.get(i).split(",");
                System.arraycopy(podzial, 0, arr[i], 0, podzial.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arr;
    }

    public static void wyswiOpcje() {
        System.out.println("\n" + ConsoleColors.BLUE + "Wybierz opcje:");
        System.out.println(ConsoleColors.RESET + "add");
        System.out.println("remove");
        System.out.println("list");
        System.out.println("exit");
    }

    public static void addTask() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Opisz zadanie");
        String desc = scan.nextLine();

        System.out.println("Dodaj termin zakonczenia zadania");
        Pattern pat = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        String termin = scan.nextLine();

        do {
            if (!pat.matcher(termin).matches()) {
                System.out.println("Zły format daty! Przykład: 2020-08-11. \nPodaj nową datę:");
            } else {
                break;
            }
            termin = scan.nextLine();
        } while (true);

        System.out.println("Czy zadanie jest ważne: tak/nie");
        String czyWazne = scan.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = desc;
        tasks[tasks.length - 1][1] = termin;
        tasks[tasks.length - 1][2] = czyWazne;

    }

    public static void removeTask(String[][] tab, int index) {

        try {
            if (index < tab.length) {
                tasks = ArrayUtils.remove(tab, index);
                System.out.println("Skasowano zadanie o numerze " + index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element o tym indeksie nie istnieje w tablicy!");
        }
    }

    public static void listTask(String[][] tab) {

        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");

            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");

            }

            System.out.println("");
        }
    }

    public static void exit(String[][] tab) {
        Path plik = Paths.get("tasks.csv");

        String[] temp = new String[tasks.length];

        for (int i = 0; i < tab.length; i++) {
            temp[i] = String.join(",", tab[i]);

        }

        try {
            Files.write(plik, Arrays.asList(temp));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(ConsoleColors.RED + "Do zobaczenia");
        System.exit(-1);
    }
}


