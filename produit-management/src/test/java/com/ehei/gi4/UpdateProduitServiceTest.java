package com.ehei.gi4;

import com.ehei.gi4.service.CreateProduitService;
import com.ehei.gi4.service.UpdateProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateProduitServiceTest {

    private UpdateProduitService updateService;
    private CreateProduitService createService;
    private Produit produit;

    @BeforeEach
    void setUp() {
        updateService = new UpdateProduitService();
        createService = new CreateProduitService();
        createService.clearAllProduits(); // Nettoyer avant chaque test

        produit = createService.createProduit("Laptop", 1000.0, 10);
    }

    @Test
    @DisplayName("Test mise à jour complète valide")
    void testUpdateProduitValide() {
        Produit modifications = new Produit(null, "Laptop Gaming", 1500.0, 5);
        Produit result = updateService.updateProduit(produit.getId(), modifications);

        assertEquals("Laptop Gaming", result.getNom());
        assertEquals(1500.0, result.getPrix(), 0.001);
        assertEquals(5, result.getQuantite());
    }

    @Test
    @DisplayName("Test mise à jour avec prix négatif - doit échouer")
    void testUpdateProduitPrixNegatif() {
        Produit modifications = new Produit(null, "Laptop", -100.0, 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateService.updateProduit(produit.getId(), modifications)
        );

        assertTrue(exception.getMessage().contains("Le prix doit être > 0"));
    }

    @Test
    @DisplayName("Test mise à jour produit inexistant - doit échouer")
    void testUpdateProduitInexistant() {
        Produit modifications = new Produit(null, "Test", 100.0, 5);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateService.updateProduit(999L, modifications)
        );

        assertTrue(exception.getMessage().contains("Produit non trouvé"));
    }

    @Test
    @DisplayName("Test mise à jour nom avec duplication - doit échouer")
    void testUpdateProduitNomDuplique() {
        // Créer un deuxième produit
        createService.createProduit("Souris", 50.0, 20);

        // Essayer de renommer le laptop en "Souris"
        Produit modifications = new Produit(null, "Souris", 1000.0, 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateService.updateProduit(produit.getId(), modifications)
        );

        assertTrue(exception.getMessage().contains("existe déjà"));
    }

    @Test
    @DisplayName("Test mise à jour seulement le prix")
    void testUpdatePrixSeul() {
        Produit result = updateService.updatePrix(produit.getId(), 1200.0);

        assertEquals(1200.0, result.getPrix(), 0.001);
        assertEquals("Laptop", result.getNom()); // Nom inchangé
        assertEquals(10, result.getQuantite()); // Quantité inchangée
    }

    @Test
    @DisplayName("Test augmentation du stock")
    void testAugmenterStock() {
        Produit result = updateService.augmenterStock(produit.getId(), 5);

        assertEquals(15, result.getQuantite());
    }

    @Test
    @DisplayName("Test diminution du stock")
    void testDiminuerStock() {
        Produit result = updateService.diminuerStock(produit.getId(), 3);

        assertEquals(7, result.getQuantite());
    }

    @Test
    @DisplayName("Test diminution stock insuffisant - doit échouer")
    void testDiminuerStockInsuffisant() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateService.diminuerStock(produit.getId(), 15)
        );

        assertTrue(exception.getMessage().contains("Quantité insuffisante"));
    }
}