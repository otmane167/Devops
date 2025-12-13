package com.ehei.gi4;


import com.ehei.gi4.produit.Produit;
import com.ehei.gi4.service.CreateProduitService;
import com.ehei.gi4.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class CreateProduitServiceTest {

    private CreateProduitService createService;

    @BeforeEach
    void setUp() {
        createService = new CreateProduitService();
        createService.clearAllProduits(); // Nettoyer avant chaque test
    }

    @Test
    @DisplayName("Test création produit valide")
    void testCreateProduitValide() {
        // Arrange
        Long id = null;
        String nom = "Laptop";
        double prix = 899.99;
        int quantite = 10;

        // Act
        Produit result = createService.createProduit(id,nom, prix, quantite);

        // Assert
        assertNotNull(result);
        assertEquals(nom, result.getNom());
        assertEquals(prix, result.getPrix(), 0.001);
        assertEquals(quantite, result.getQuantite());
        assertNotNull(result.getId()); // ID devrait être auto-généré
    }

    @Test
    @DisplayName("Test création multiple produits")
    void testCreateMultipleProduits() {
        // Act
        Produit p1 = createService.createProduit(null,"Produit1", 100.0, 5);
        Produit p2 = createService.createProduit(null,"Produit2", 200.0, 3);
        Produit p3 = createService.createProduit(null,"Produit3", 300.0, 7);

        // Assert
        assertEquals(3, createService.getAllProduits().size());
        assertTrue(createService.getAllProduits().contains(p1));
        assertTrue(createService.getAllProduits().contains(p2));
        assertTrue(createService.getAllProduits().contains(p3));
    }

    @Test
    @DisplayName("Test création avec prix zéro")
    void testCreateProduitPrixZero() {
        // Act
        Produit result = createService.createProduit(null,"Produit Gratuit", 0.0, 100);

        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.getPrix(), 0.001);
    }

    @Test
    @DisplayName("Test création avec quantité zéro")
    void testCreateProduitQuantiteZero() {
        // Act
        Produit result = createService.createProduit(null,"Produit Épuisé", 50.0, 0);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getQuantite());
    }

    @Test
    @DisplayName("Test création avec nom contenant espaces")
    void testCreateProduitNomAvecEspaces() {
        // Arrange
        String nom = "  Produit Test  ";

        // Act
        Produit result = createService.createProduit(null,"laptop", 100.0, 10);

        // Assert
        assertEquals(nom, result.getNom()); // Garde les espaces
    }

    @Test
    @DisplayName("Test création produit avec valeurs limites")
    void testCreateProduitValeursLimites() {
        // Test avec très petit prix
        Produit p1 = createService.createProduit(null, "ordinateur", 1,10);
        assertEquals(0.01, p1.getPrix(), 0.001);

        // Test avec grand prix
        Produit p2 = createService.createProduit(null, "souris", 1,10);
        assertEquals(999999.99, p2.getPrix(), 0.001);

        // Test avec grande quantité
        Produit p3 = createService.createProduit(null, "clavier", 1000000,10);
        assertEquals(1000000, p3.getQuantite());
    }

    @Test
    @DisplayName("Test IDs uniques auto-générés")
    void testIdsUniquesAutoGenerees() {
        // Act
        Produit p1 = createService.createProduit(null, "clavier", 1000000,10);
        Produit p2 = createService.createProduit(null, "casque", 1000000,10);
        Produit p3 = createService.createProduit(null, "tablet", 1000000,10);

        // Assert
        assertNotNull(p1.getId());
        assertNotNull(p2.getId());
        assertNotNull(p3.getId());

        // Les IDs doivent être différents
        assertNotEquals(p1.getId(), p2.getId());
        assertNotEquals(p1.getId(), p3.getId());
        assertNotEquals(p2.getId(), p3.getId());
    }

    @Test
    @DisplayName("Test getAllProduits retourne copie")
    void testGetAllProduitsRetourneCopie() {
        // Arrange
        createService.createProduit(null, "chargeur", 5,50);
        createService.createProduit(null, "smartwatch", 3,50);

        // Act
        List<Produit> produits1 = createService.getAllProduits();
        List<Produit> produits2 = createService.getAllProduits();

        // Assert - doivent être égaux mais pas la même référence
        assertEquals(produits1.size(), produits2.size());
        assertNotSame(produits1, produits2); // Pas la même liste
    }


    @Test
    @DisplayName("Test clearAllProduits vide la liste")
    void testClearAllProduits() {
        // Arrange
        createService.createProduit(null, "ecran", 5,70);
        createService.createProduit(null, "ecran", 3,60);
        assertEquals(2, createService.getAllProduits().size());

        // Act
        createService.clearAllProduits();

        // Assert
        assertEquals(0, createService.getAllProduits().size());
        assertTrue(createService.getAllProduits().isEmpty());
    }

    @Test
    @DisplayName("Test scénario complet création")
    void testScenarioCompletCreation() {
        // 1. Créer plusieurs produits
        Produit p1 = createService.createProduit(null,"Laptop", 899.99, 10);
        Produit p2 = createService.createProduit(null,"Souris", 29.99, 25);
        Produit p3 = createService.createProduit(null,"Clavier", 89.99, 15);

        // 2. Vérifier la liste
        List<Produit> produits = createService.getAllProduits();
        assertEquals(3, produits.size());

        // 3. Vérifier les attributs
        assertEquals("Laptop", p1.getNom());
        assertEquals(899.99, p1.getPrix(), 0.001);
        assertEquals(10, p1.getQuantite());

        // 4. Nettoyer et vérifier
        createService.clearAllProduits();
        assertEquals(0, createService.getAllProduits().size());
    }
}