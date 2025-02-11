package main;

import entities.Coach;
import entities.Course;
import services.CoachService;
import services.CourseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class TestMain {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CoachService coachService = new CoachService();
    private static final CourseService courseService = new CourseService();

    public static void main(String[] args) {
        while (true) {
            try {
                showMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1 -> handleCoachOperations();
                    case 2 -> handleCourseOperations();
                    case 0 -> {
                        System.out.println("Au revoir!");
                        return;
                    }
                    default -> System.out.println("Choix invalide!");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Gérer les Coachs");
        System.out.println("2. Gérer les Cours");
        System.out.println("0. Quitter");
        System.out.print("Votre choix: ");
    }

    private static void handleCoachOperations() {
        while (true) {
            try {
                showCoachMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1 -> createCoach();
                    case 2 -> displayAllCoaches();
                    case 3 -> updateCoach();
                    case 4 -> deleteCoach();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Choix invalide!");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    private static void handleCourseOperations() {
        while (true) {
            try {
                showCourseMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1 -> createCourse();
                    case 2 -> displayAllCourses();
                    case 3 -> updateCourse();
                    case 4 -> deleteCourse();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Choix invalide!");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    private static void showCoachMenu() {
        System.out.println("\n=== Gestion des Coachs ===");
        System.out.println("1. Ajouter un coach");
        System.out.println("2. Afficher tous les coachs");
        System.out.println("3. Modifier un coach");
        System.out.println("4. Supprimer un coach");
        System.out.println("0. Retour au menu principal");
        System.out.print("Votre choix: ");
    }

    private static void showCourseMenu() {
        System.out.println("\n=== Gestion des Cours ===");
        System.out.println("1. Ajouter un cours");
        System.out.println("2. Afficher tous les cours");
        System.out.println("3. Modifier un cours");
        System.out.println("4. Supprimer un cours");
        System.out.println("0. Retour au menu principal");
        System.out.print("Votre choix: ");
    }

    private static void createCoach() throws Exception {
        System.out.println("\n=== Création d'un Coach ===");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
        System.out.print("Spécialité: ");
        String specialite = scanner.nextLine();
        System.out.print("Salaire: ");
        BigDecimal salaire = scanner.nextBigDecimal();

        Coach coach = new Coach(nom, prenom, email, telephone, specialite, salaire, LocalDate.now());
        coachService.create(coach);
        System.out.println("Coach créé avec succès!");
    }

    private static void displayAllCoaches() throws Exception {
        System.out.println("\n=== Liste des Coachs ===");
        coachService.readAll().forEach(System.out::println);
    }

    private static void updateCoach() throws Exception {
        System.out.println("\n=== Modification d'un Coach ===");
        displayAllCoaches();
        System.out.print("Entrez l'ID du coach à modifier: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        Coach coach = coachService.readById(id);
        if (coach == null) {
            System.out.println("Coach non trouvé!");
            return;
        }

        System.out.print("Nouveau téléphone (Enter pour garder l'ancien): ");
        String telephone = scanner.nextLine();
        if (!telephone.isEmpty()) coach.setTelephone(telephone);

        System.out.print("Nouveau salaire (0 pour garder l'ancien): ");
        BigDecimal salaire = scanner.nextBigDecimal();
        if (salaire.compareTo(BigDecimal.ZERO) != 0) coach.setSalaire(salaire);

        coachService.update(coach);
        System.out.println("Coach modifié avec succès!");
    }

    private static void deleteCoach() throws Exception {
        System.out.println("\n=== Suppression d'un Coach ===");
        displayAllCoaches();
        System.out.print("Entrez l'ID du coach à supprimer: ");
        int id = scanner.nextInt();
        coachService.delete(id);
        System.out.println("Coach supprimé avec succès!");
    }

    private static void createCourse() throws Exception {
        System.out.println("\n=== Création d'un Cours ===");
        System.out.print("Nom du cours: ");
        String nomCours = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Heure (HH:mm): ");
        String heureStr = scanner.nextLine();
        LocalTime horaire = LocalTime.parse(heureStr);
        System.out.print("Jour: ");
        String jour = scanner.nextLine();
        System.out.print("Durée (minutes): ");
        int duree = scanner.nextInt();

        displayAllCoaches();
        System.out.print("ID du coach: ");
        int idCoach = scanner.nextInt();

        Course course = new Course(nomCours, description, horaire, jour, duree, idCoach);
        courseService.create(course);
        System.out.println("Cours créé avec succès!");
    }

    private static void displayAllCourses() throws Exception {
        System.out.println("\n=== Liste des Cours ===");
        courseService.readAll().forEach(System.out::println);
    }

    private static void updateCourse() throws Exception {
        System.out.println("\n=== Modification d'un Cours ===");
        displayAllCourses();
        System.out.print("Entrez l'ID du cours à modifier: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        Course course = courseService.readById(id);
        if (course == null) {
            System.out.println("Cours non trouvé!");
            return;
        }

        System.out.print("Nouveau jour (Enter pour garder l'ancien): ");
        String jour = scanner.nextLine();
        if (!jour.isEmpty()) course.setJour(jour);

        System.out.print("Nouvelle durée (0 pour garder l'ancienne): ");
        int duree = scanner.nextInt();
        if (duree != 0) course.setDuree(duree);

        courseService.update(course);
        System.out.println("Cours modifié avec succès!");
    }

    private static void deleteCourse() throws Exception {
        System.out.println("\n=== Suppression d'un Cours ===");
        displayAllCourses();
        System.out.print("Entrez l'ID du cours à supprimer: ");
        int id = scanner.nextInt();
        courseService.delete(id);
        System.out.println("Cours supprimé avec succès!");
    } }
