package com.example;

import com.example.repository.*;
import com.example.service.*;
import com.example.test.TestScenarios;
import com.example.util.DataInitializer;
import com.example.util.PerformanceReport;
import com.example.util.DatabaseMigrationTool;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println(" APPLICATION DE RESERVATION DE SALLES ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-reservations");

        try {
            Sallerepository salleRepository = new SallerepositoryImpl(emf);
            SalleService salleService = new SalleServiceImpl(salleRepository);

            ReservationRepository reservationRepository = new ReservationRepositoryImpl(emf);
            ReservationService reservationService = new ReservationServiceImpl(emf, reservationRepository);

            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n MENU PRINCIPAL ");
                System.out.println("1. Initialiser les données de test");
                System.out.println("2. Exécuter les scénarios de test");
                System.out.println("3. Générer un rapport de performance");
                System.out.println("4. Quitter");
                System.out.print("Votre choix: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> new DataInitializer(emf).initializeData();
                    case 2 -> new TestScenarios(emf, salleService, reservationService).runAllTests();
                    case 3 -> new PerformanceReport(emf).runPerformanceTests();
                    case 4 -> {
                        exit = true;
                        System.out.println("Au revoir !");
                    }
                    default -> System.out.println("Choix invalide.");
                }
            }
        } finally {
            emf.close();
        }
    }
}
