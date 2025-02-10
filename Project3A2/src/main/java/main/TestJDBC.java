package main;

import entities.Abonnement;
import entities.Promotion;
import services.AbonnementService;
import services.PromotionService;
import utils.MyConnection;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TestJDBC {

    public static void main(String[] args) throws SQLException {
        // Initialisation de la connexion à la base de données
        MyConnection mc = MyConnection.getInstance();

        // Création des instances des services
        AbonnementService serviceAbonnement = new AbonnementService();
        PromotionService servicePromotion = new PromotionService();

        // Scanner pour la saisie utilisateur
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("=== Menu ===");
            System.out.println("1. Ajouter un abonnement");
            System.out.println("2. Mettre à jour un abonnement");
            System.out.println("3. Supprimer un abonnement");
            System.out.println("4. Afficher tous les abonnements");
            System.out.println("5. Ajouter une promotion");
            System.out.println("6. Mettre à jour une promotion");
            System.out.println("7. Supprimer une promotion");
            System.out.println("8. Afficher toutes les promotions");
            System.out.println("9. Quitter");
            System.out.print("Choisissez une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            switch (choice) {
                case 1:
                    // Ajouter un abonnement
                    System.out.println("=== Ajouter un abonnement ===");
                    System.out.print("Nom de l'abonnement : ");
                    String nom = scanner.nextLine();
                    System.out.print("Description : ");
                    String descriptiona = scanner.nextLine();
                    System.out.print("Durée (en jours) : ");
                    int duree = scanner.nextInt();
                    System.out.print("Prix : ");
                    double prix = scanner.nextDouble();
                    scanner.nextLine(); // Consommer la nouvelle ligne restante
                    System.out.print("Nom de la salle de sport associée : ");
                    String salleNom = scanner.nextLine();

                    // Récupérer l'ID de la salle de sport
                    int SalleId = serviceAbonnement.getIdByName(salleNom);

                    if (SalleId == 0) {
                        System.out.println("Erreur : Aucune salle de sport trouvée avec le nom fourni.");
                        break;
                    }

                    // Création de l'objet Abonnement
                    Abonnement nouvelAbonnement = new Abonnement();
                    nouvelAbonnement.setNom(nom);
                    nouvelAbonnement.setdescriptiona(descriptiona);
                    nouvelAbonnement.setDuree(duree);
                    nouvelAbonnement.setPrix(prix);
                    nouvelAbonnement.setSalleDeSportId(SalleId);
                    nouvelAbonnement.setSalleNom(salleNom);

                    // Tentative d'ajout de l'abonnement
                    try {
                        serviceAbonnement.create(nouvelAbonnement);
                        System.out.println("Abonnement ajouté avec succès.");
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de l'ajout de l'abonnement : " + e.getMessage());
                    }
                    break;

                case 2:
                    // Mettre à jour un abonnement
                    System.out.print("Entrez l'ID de l'abonnement à mettre à jour : ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine(); // Consommer la nouvelle ligne restante
                    System.out.print("Entrez la nouvelle description : ");
                    String newDescriptiona = scanner.nextLine();

                    // Création de l'objet Abonnement à mettre à jour
                    Abonnement updateAbonnement = new Abonnement();
                    updateAbonnement.setId(updateId);
                    updateAbonnement.setdescriptiona(newDescriptiona);

                    try {
                        serviceAbonnement.update(updateAbonnement);
                        System.out.println("Abonnement mis à jour avec succès.");
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de la mise à jour de l'abonnement : " + e.getMessage());
                    }
                    break;

                case 3:
                    // Supprimer un abonnement (logique existante)
                    // Supprimer un abonnement
                    System.out.print("Entrez l'ID de l'abonnement à supprimer : ");
                    int deleteId = scanner.nextInt();
                    Abonnement deleteAbonnement = new Abonnement();
                    deleteAbonnement.setId(deleteId);

                    try {
                        serviceAbonnement.delete(deleteAbonnement);
                        System.out.println("Abonnement supprimé avec succès.");
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de la suppression de l'abonnement : " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        List<Abonnement> abonnements = serviceAbonnement.readAll();
                        if (abonnements.isEmpty()) {
                            System.out.println("Aucun abonnement trouvé.");
                        } else {
                            System.out.println("=== Liste des abonnements ===");
                            for (Abonnement abonnement : abonnements) {
                                System.out.println(abonnement);
                            }
                        }
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de l'affichage des abonnements : " + e.getMessage());
                    }

                    break;

                case 5:
                    // Ajouter une promotion
                    System.out.println("=== Ajouter une promotion ===");
                    System.out.print("Code Promo : ");
                    String codePromo = scanner.nextLine();
                    System.out.print("Description : ");
                    String description = scanner.nextLine();
                    System.out.print("Type de réduction : ");
                    String typeReduction = scanner.nextLine();
                    System.out.print("Valeur de réduction : ");
                    BigDecimal valeurReduction = scanner.nextBigDecimal();
                    scanner.nextLine();
                    System.out.print("Date début (YYYY-MM-DD) : ");
                    String dateDebut = scanner.nextLine();
                    System.out.print("Date fin (YYYY-MM-DD) : ");
                    String dateFin = scanner.nextLine();
                    System.out.print("ID de l'abonnement : ");
                    int abonnementId = scanner.nextInt();
                    System.out.print("ID de la salle : ");
                    int salleId = scanner.nextInt();

                    Promotion newPromotion = new Promotion();
                    newPromotion.setCodePromo(codePromo);
                    newPromotion.setDescription(description);
                    newPromotion.setTypeReduction(typeReduction);
                    newPromotion.setValeurReduction(valeurReduction);
                    newPromotion.setDateDebut(dateDebut);
                    newPromotion.setDateFin(dateFin);
                    newPromotion.setAbonnementId(abonnementId);
                    newPromotion.setSalleId(salleId);

                    servicePromotion.create(newPromotion);
                    System.out.println("Promotion ajoutée avec succès.");
                    break;

                case 6:
                    // Mettre à jour une promotion
                    System.out.print("Entrez l'ID de la promotion à mettre à jour : ");
                    int promoId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nouvelle description : ");
                    String newDescription = scanner.nextLine();

                    Promotion updatePromotion = new Promotion(promoId, newDescription);
                    servicePromotion.update(updatePromotion);
                    try {
                        servicePromotion.update(updatePromotion);
                        System.out.println("Promotion mise à jour avec succès.");
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de la mise à jour de la promotion : " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 7:
                    // Supprimer une promotion
                    System.out.print("Entrez l'ID de la promotion à supprimer : ");
                    int deletePromoId = scanner.nextInt();
                    servicePromotion.delete(deletePromoId);
                    System.out.println("Promotion supprimée avec succès.");
                    break;

                case 8:
                    // Afficher toutes les promotions
                    List<Promotion> promotions = servicePromotion.readAll();
                    if (promotions.isEmpty()) {
                        System.out.println("Aucune promotion trouvée.");
                    } else {
                        System.out.println("=== Liste des promotions ===");
                        for (Promotion promo : promotions) {
                            System.out.println(promo);
                        }
                    }
                    break;

                case 9:
                    // Quitter
                    exit = true;
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }

        scanner.close();
    }
}
