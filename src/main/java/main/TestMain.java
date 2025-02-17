package main;

import entities.*;
import services.services.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class TestMain {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProduitService produitService = new ProduitService();
    private static final CommandeService commandeService = new CommandeService();

    public static void main(String[] args) {
        while (true) {
            try {
                showMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1 -> handleProduitOperations();
                    case 2 -> handleCommandeOperations();
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
        System.out.println("1. Gérer les Produits");
        System.out.println("2. Gérer les Commandes");
        System.out.println("0. Quitter");
        System.out.print("Votre choix: ");
    }

    private static void handleProduitOperations() {
        while (true) {
            try {
                showProduitMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1 -> createProduit();
                    case 2 -> displayAllProduits();
                    case 3 -> updateProduit();
                    case 4 -> deleteProduit();
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

    private static void handleCommandeOperations() {
        while (true) {
            try {
                showCommandeMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1 -> createCommande();
                    case 2 -> displayAllCommandes();
                    case 3 -> updateCommande();
                    case 4 -> deleteCommande();
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

    private static void showProduitMenu() {
        System.out.println("\n=== Gestion des Produits ===");
        System.out.println("1. Ajouter un produit");
        System.out.println("2. Afficher tous les produits");
        System.out.println("3. Modifier un produit");
        System.out.println("4. Supprimer un produit");
        System.out.println("0. Retour au menu principal");
        System.out.print("Votre choix: ");
    }

    private static void showCommandeMenu() {
        System.out.println("\n=== Gestion des Commandes ===");
        System.out.println("1. Ajouter une commande");
        System.out.println("2. Afficher toutes les commandes");
        System.out.println("3. Modifier une commande");
        System.out.println("4. Supprimer une commande");
        System.out.println("0. Retour au menu principal");
        System.out.print("Votre choix: ");
    }

    private static void createProduit() throws Exception {
        System.out.println("\n=== Création d'un Produit ===");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Catégorie: ");
        String categorie = scanner.nextLine();
        System.out.print("Prix: ");
        BigDecimal prix = scanner.nextBigDecimal();
        System.out.print("Quantité en stock: ");
        int quantiteStock = scanner.nextInt();
        System.out.print("Disponible (true/false): ");
        boolean disponible = scanner.nextBoolean();

        Produit produit = new Produit(nom, description, categorie, prix, quantiteStock, disponible);
        produitService.create(produit);
        System.out.println("Produit créé avec succès!");
    }

    private static void displayAllProduits() throws Exception {
        System.out.println("\n=== Liste des Produits ===");
        produitService.readAll().forEach(System.out::println);
    }

    private static void updateProduit() throws Exception {
        System.out.println("\n=== Modification d'un Produit ===");
        displayAllProduits();
        System.out.print("Entrez l'ID du produit à modifier: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        Produit produit = produitService.readById(id);
        if (produit == null) {
            System.out.println("Produit non trouvé!");
            return;
        }

        System.out.print("Nouveau prix (0 pour garder l'ancien): ");
        BigDecimal prix = scanner.nextBigDecimal();
        if (prix.compareTo(BigDecimal.ZERO) != 0) produit.setPrix(prix);

        System.out.print("Nouvelle quantité en stock (-1 pour garder l'ancienne): ");
        int quantite = scanner.nextInt();
        if (quantite != -1) produit.setQuantiteStock(quantite);

        produitService.update(produit);
        System.out.println("Produit modifié avec succès!");
    }

    private static void deleteProduit() throws Exception {
        System.out.println("\n=== Suppression d'un Produit ===");
        displayAllProduits();
        System.out.print("Entrez l'ID du produit à supprimer: ");
        int id = scanner.nextInt();
        produitService.delete(id);
        System.out.println("Produit supprimé avec succès!");
    }

    private static void createCommande() throws Exception {
        System.out.println("\n=== Création d'une Commande ===");
        displayAllProduits();
        System.out.print("ID du produit: ");
        int idProduit = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Nom du client: ");
        String nomClient = scanner.nextLine();
        System.out.print("Adresse de livraison: ");
        String adresseLivraison = scanner.nextLine();
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
        System.out.print("Quantité: ");
        int quantite = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        Commande commande = new Commande(
            idProduit, nomClient, adresseLivraison, telephone, quantite,
            LocalDateTime.now(), "En attente"
        );
        commandeService.create(commande);
        System.out.println("Commande créée avec succès!");
    }

    private static void displayAllCommandes() throws Exception {
        System.out.println("\n=== Liste des Commandes ===");
        commandeService.readAll().forEach(System.out::println);
    }

    private static void updateCommande() throws Exception {
        System.out.println("\n=== Modification d'une Commande ===");
        displayAllCommandes();
        System.out.print("Entrez l'ID de la commande à modifier: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        Commande commande = commandeService.readById(id);
        if (commande == null) {
            System.out.println("Commande non trouvée!");
            return;
        }

        System.out.print("Nouveau statut (Enter pour garder l'ancien): ");
        String statut = scanner.nextLine();
        if (!statut.isEmpty()) commande.setStatutCommande(statut);

        System.out.print("Nouvelle quantité (0 pour garder l'ancienne): ");
        int quantite = scanner.nextInt();
        if (quantite != 0) commande.setQuantite(quantite);

        commandeService.update(commande);
        System.out.println("Commande modifiée avec succès!");
    }

    private static void deleteCommande() throws Exception {
        System.out.println("\n=== Suppression d'une Commande ===");
        displayAllCommandes();
        System.out.print("Entrez l'ID de la commande à supprimer: ");
        int id = scanner.nextInt();
        commandeService.delete(id);
        System.out.println("Commande supprimée avec succès!");
    }
}
