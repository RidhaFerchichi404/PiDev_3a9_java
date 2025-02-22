package org.example;

import org.example.entities.Equipement;
import org.example.entities.Exercice;
import org.example.entities.SalleDeSport;
import org.example.service.EquipementService;
import org.example.service.ExerciceService;
import org.example.service.SalleDeSportService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        SalleDeSportService salleService = new SalleDeSportService();
        EquipementService equipementService = new EquipementService();
        ExerciceService exerciceService = new ExerciceService();

        while (true) {
            System.out.println("\n========= MENU PRINCIPAL =========");
            System.out.println("1. Gérer les Salles de Sport");
            System.out.println("2. Gérer les Équipements");
            System.out.println("3. Gérer les Exercices");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne

            switch (choix) {
                case 1:
                    gererSalles(scanner, salleService);
                    break;
                case 2:
                    gererEquipements(scanner, equipementService);
                    break;
                case 3:
                    gererExercices(scanner, exerciceService);
                    break;
                case 4:
                    System.out.println("Fermeture du programme...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    // ==========================
    // Gestion des Salles de Sport
    // ==========================
    private static void gererSalles(Scanner scanner, SalleDeSportService salleService) {
        try {
            System.out.println("\n===== Gestion des Salles =====");
            System.out.println("1. Ajouter une salle");
            System.out.println("2. Afficher les salles");
            System.out.println("3. Modifier une salle");
            System.out.println("4. Supprimer une salle");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("Nom de la salle : ");
                    String nom = scanner.nextLine();
                    System.out.print("Zone : ");
                    String zone = scanner.nextLine();
                    System.out.print("Image URL : ");
                    String image = scanner.nextLine();
                    System.out.print("ID Utilisateur : ");
                    int idUser = scanner.nextInt();
                    SalleDeSport salle = new SalleDeSport(nom, zone, image, idUser);
                    salleService.create(salle);
                    System.out.println("Salle ajoutée avec succès !");
                    break;

                case 2:
                    List<SalleDeSport> salles = salleService.readAll();
                    System.out.println("Liste des salles : " + salles);
                    break;

                case 3:
                    System.out.print("ID de la salle à modifier : ");
                    int idModif = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nouveau nom : ");
                    String newNom = scanner.nextLine();
                    System.out.print("Nouvelle zone : ");
                    String newZone = scanner.nextLine();
                    System.out.print("Nouvelle image URL : ");
                    String newImage = scanner.nextLine();
                    System.out.print("Nouvelle iduser : ");
                    idUser = scanner.nextInt();
                    SalleDeSport salleModif = new SalleDeSport(idModif, newNom, newZone, newImage, idUser);
                    salleService.update(salleModif);
                    System.out.println("Salle modifiée !");
                    break;

                case 4:
                    System.out.print("ID de la salle à supprimer : ");
                    int idSuppr = scanner.nextInt();
                    SalleDeSport salleSuppr = new SalleDeSport(idSuppr, "", "", "", 1);
                    salleService.delete(salleSuppr);
                    System.out.println("Salle supprimée !");
                    break;

                default:
                    System.out.println("Choix invalide !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // Gestion des Équipements
    // ==========================
    private static void gererEquipements(Scanner scanner, EquipementService equipementService) {
        try {
            System.out.println("\n===== Gestion des Équipements =====");
            System.out.println("1. Ajouter un équipement");
            System.out.println("2. Afficher les équipements");
            System.out.println("3. Modifier un équipement");
            System.out.println("4. Supprimer un équipement");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("ID Salle : ");
                    int idSalle = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nom de l'équipement : ");
                    String nom = scanner.nextLine();
                    System.out.print("En fonctionnement ? (true/false) : ");
                    boolean fonctionnement = scanner.nextBoolean();
                    Equipement equipement = new Equipement(idSalle, nom, fonctionnement, new Date(), new Date(), 1);
                    equipementService.create(equipement);
                    System.out.println("Équipement ajouté !");
                    break;

                case 2:
                    List<Equipement> equipements = equipementService.readAll();
                    System.out.println("Liste des équipements : " + equipements);
                    break;

                case 3:
                    System.out.print("ID équipement à modifier : ");
                    int idModif = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("ID salle à modifier : ");
                    int idsalle = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nouveau nom : ");
                    String newNom = scanner.nextLine();
                    Equipement equipementModif = new Equipement(idModif, idsalle , newNom, true, new Date(), new Date(), 1);
                    equipementService.update(equipementModif);
                    System.out.println("Équipement modifié !");
                    break;

                case 4:
                    System.out.print("ID équipement à supprimer : ");
                    int idSuppr = scanner.nextInt();
                    System.out.print("ID salle à supprimer : ");
                    idsalle = scanner.nextInt();
                    Equipement equipementSuppr = new Equipement(idSuppr,  idsalle , "", true, null, null, 0);
                    equipementService.delete(equipementSuppr);
                    System.out.println("Équipement supprimé !");
                    break;

                default:
                    System.out.println("Choix invalide !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // Gestion des Exercices
    // ==========================
    private static void gererExercices(Scanner scanner, ExerciceService exerciceService) {
        try {
            System.out.println("\n===== Gestion des Exercices =====");
            System.out.println("1. Ajouter un exercice");
            System.out.println("2. Afficher les exercices");
            System.out.println("3. Modifier un exercice");
            System.out.println("4. Supprimer un exercice");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("Nom de l'exercice : ");
                    String nom = scanner.nextLine();
                    System.out.print("Description : ");
                    String description = scanner.nextLine();
                    System.out.print("Image : ");
                    String image = scanner.nextLine();
                    System.out.print("ID Utilisateur : ");
                    int idUser = scanner.nextInt();
                    System.out.print("ID Equipement : ");
                    int idEquipement = scanner.nextInt();
                    Exercice exercice = new Exercice(description, image, idUser, idEquipement, nom);
                    exerciceService.create(exercice);
                    System.out.println("Exercice ajouté !");
                    break;

                case 2:
                    List<Exercice> exercices = exerciceService.readAll();
                    System.out.println("Liste des exercices : " + exercices);
                    break;

                case 3:
                    System.out.print("ID exercice à modifier : ");
                    int idModif = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("ID equipement à modifier : ");
                    idEquipement = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nouveau nom : ");
                    String newNom = scanner.nextLine();
                    Exercice exerciceModif = new Exercice(idModif, "", "", 1, idEquipement, newNom);
                    exerciceService.update(exerciceModif);
                    System.out.println("Exercice modifié !");
                    break;

                case 4:
                    System.out.print("ID exercice à supprimer : ");
                    int idSuppr = scanner.nextInt();
                    System.out.print("ID equipement à modifier : ");
                    idEquipement = scanner.nextInt();
                    scanner.nextLine();
                    Exercice exerciceSuppr = new Exercice(idSuppr, "", "", 1,  idEquipement, "");
                    exerciceService.delete(exerciceSuppr);
                    System.out.println("Exercice supprimé !");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
