package com.ehei.gi4;

import com.ehei.gi4.service.*;
import com.ehei.gi4.produit.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

class ReadProduitServiceTest {

    private ReadProduitService readService;
    private CreateProduitService createService;

    @BeforeEach
    void setUp() {
        createService = new CreateProduitService();
        readService = new ReadProduitService();

        // Nettoyer avant chaque test
        createService.clearAllProduits();

        // Créer des produits de test
        createService.createProduit(1L,"Laptop Dell", 899.99, 10);
        createService.createProduit(2L,"Souris Logitech", 29.99, 25);
        createService.createProduit(3L,"Clavier Mécanique", 89.99, 15);
        createService.createProduit(4L,"Écran 4K", 299.99, 8);
    }

    @Test
    @DisplayName("Test getAllProduits retourne tous les produits")
    void testGetAllProduits() {
        // Act
        List<Produit> produits = readService.getAllProduits();

        // Assert
        assertEquals(4, produits.size());
        assertTrue(produits.stream().anyMatch(p -> p.getNom().equals("Laptop Dell")));
        assertTrue(produits.stream().anyMatch(p -> p.getNom().equals("Souris Logitech")));
        assertTrue(produits.stream().anyMatch(p -> p.getNom().equals("Clavier Mécanique")));
        assertTrue(produits.stream().anyMatch(p -> p.getNom().equals("Écran 4K")));
    }

    @Test
    @DisplayName("Test getTotalProduits retourne le bon nombre")
    void testGetTotalProduits() {
        // Act
        int total = readService.getTotalProduits();

        // Assert
        assertEquals(4, total);
    }

    @Test
    @DisplayName("Test getTotalProduits avec liste vide")
    void testGetTotalProduitsListeVide() {
        // Arrange
        createService.clearAllProduits();

        // Act
        int total = readService.getTotalProduits();

        // Assert
        assertEquals(0, total);
    }

    @Test
    @DisplayName("Test getProduitByNom - produit existant")
    void testGetProduitByNomExistant() {
        // Act
        Optional<Produit> result = readService.getProduitByNom("Laptop Dell");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Laptop Dell", result.get().getNom());
        assertEquals(899.99, result.get().getPrix(), 0.001);
        assertEquals(10, result.get().getQuantite());
    }

    @Test
    @DisplayName("Test getProduitByNom - insensible à la casse")
    void testGetProduitByNomCaseInsensitive() {
        // Test avec différentes casse
        assertTrue(readService.getProduitByNom("LAPTOP DELL").isPresent());
        assertTrue(readService.getProduitByNom("laptop dell").isPresent());
        assertTrue(readService.getProduitByNom("Laptop Dell").isPresent());
    }

    @Test
    @DisplayName("Test getProduitByNom - produit inexistant")
    void testGetProduitByNomInexistant() {
        // Act
        Optional<Produit> result = readService.getProduitByNom("Produit Inexistant");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test getProduitByNom - nom null")
    void testGetProduitByNomNull() {
        // Act
        Optional<Produit> result = readService.getProduitByNom(null);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test getProduitByNom - nom vide")
    void testGetProduitByNomVide() {
        // Act
        Optional<Produit> result = readService.getProduitByNom("");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test getProduitsMoinsChersQue - avec limite")
    void testGetProduitsMoinsChersQue() {
        // Act - produits à moins de 100€
        List<Produit> produits = readService.getProduitsMoinsChersQue(100.0);

        // Assert
        assertEquals(2, produits.size()); // Souris (29.99) et Clavier (89.99)
        assertTrue(produits.stream().anyMatch(p -> p.getNom().equals("Souris Logitech")));
        assertTrue(produits.stream().anyMatch(p -> p.getNom().equals("Clavier Mécanique")));
        assertFalse(produits.stream().anyMatch(p -> p.getNom().equals("Laptop Dell"))); // 899.99 > 100
        assertFalse(produits.stream().anyMatch(p -> p.getNom().equals("Écran 4K"))); // 299.99 > 100
    }

    @Test
    @DisplayName("Test getProduitsMoinsChersQue - avec prix zéro")
    void testGetProduitsMoinsChersQuePrixZero() {
        // Arrange - ajouter un produit gratuit
        createService.createProduit(5L,"Produit Gratuit", 0.0, 100);

        // Act
        List<Produit> produits = readService.getProduitsMoinsChersQue(0.0);

        // Assert
        assertEquals(0, produits.size()); // Aucun produit < 0
    }

    @Test
    @DisplayName("Test getProduitsMoinsChersQue - avec prix très élevé")
    void testGetProduitsMoinsChersQuePrixEleve() {
        // Act - tous les produits sont < 1000€
        List<Produit> produits = readService.getProduitsMoinsChersQue(1000.0);

        // Assert
        assertEquals(4, produits.size()); // Tous les produits
    }

    @Test
    @DisplayName("Test getProduitsMoinsChersQue - liste vide")
    void testGetProduitsMoinsChersQueListeVide() {
        // Arrange
        createService.clearAllProduits();

        // Act
        List<Produit> produits = readService.getProduitsMoinsChersQue(100.0);

        // Assert
        assertTrue(produits.isEmpty());
    }

    @Test
    @DisplayName("Test scénario complet de recherche")
    void testScenarioCompletRecherche() {
        // 1. Vérifier le total
        assertEquals(4, readService.getTotalProduits());

        // 2. Rechercher par nom (insensible casse)
        assertTrue(readService.getProduitByNom("SOURIS LOGITECH").isPresent());

        // 3. Rechercher produits pas chers
        List<Produit> pasChers = readService.getProduitsMoinsChersQue(50.0);
        assertEquals(1, pasChers.size()); // Seulement la souris (29.99)
        assertEquals("Souris Logitech", pasChers.get(0).getNom());

        // 4. Vérifier tous les produits
        List<Produit> tous = readService.getAllProduits();
        assertEquals(4, tous.size());
    }

    @Test
    @DisplayName("Test recherche avec produits identiques")
    void testRechercheAvecProduitsIdentiques() {
        // Arrange - ajouter deux produits avec même nom mais casse différente
        createService.createProduit(6L,"produit test", 50.0, 5);
        createService.createProduit(7L,"PRODUIT TEST", 60.0, 3);

        // Act - recherche devrait trouver le premier
        Optional<Produit> result = readService.getProduitByNom("Produit Test");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(50.0, result.get().getPrix(), 0.001); // Doit trouver le premier
    }

    @Test
    @DisplayName("Test isolation des services")
    void testIsolationServices() {
        // Vérifier que CreateService et ReadService utilisent la même liste
        createService.createProduit(8L,"Nouveau Produit", 100.0, 10);

        // ReadService devrait voir le nouveau produit
        assertEquals(5, readService.getTotalProduits());
        assertTrue(readService.getProduitByNom("Nouveau Produit").isPresent());
    }
}