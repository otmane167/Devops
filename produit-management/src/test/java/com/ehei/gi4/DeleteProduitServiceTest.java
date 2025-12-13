package com.ehei.gi4;

import com.ehei.gi4.service.CreateProduitService;
import com.ehei.gi4.service.DeleteProduitService;
import com.ehei.gi4.service.ReadProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class DeleteProduitServiceTest {

    private DeleteProduitService deleteService;
    private CreateProduitService createService;
    private ReadProduitService readService;

    @BeforeEach
    void setUp() {
        deleteService = new DeleteProduitService();
        createService = new CreateProduitService();
        readService = new ReadProduitService();
        createService.clearAllProduits();

        // Créer quelques produits pour les tests
        createService.createProduit("Laptop", 1000.0, 10);
        createService.createProduit("Souris", 50.0, 20);
        createService.createProduit("Clavier", 80.0, 15);
    }

    @Test
    @DisplayName("Test suppression par ID valide")
    void testDeleteProduitByIdValide() {
        Long id = createService.createProduit("Test", 100.0, 5).getId();
        int initialCount = readService.getTotalProduits();

        boolean result = deleteService.deleteProduitById(id);

        assertTrue(result);
        assertEquals(initialCount - 1, readService.getTotalProduits());
    }

    @Test
    @DisplayName("Test suppression par ID inexistant - doit échouer")
    void testDeleteProduitByIdInexistant() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deleteService.deleteProduitById(999L)
        );

        assertTrue(exception.getMessage().contains("Produit non trouvé"));
    }

    @Test
    @DisplayName("Test suppression par nom valide")
    void testDeleteProduitByNomValide() {
        int initialCount = readService.getTotalProduits();

        boolean result = deleteService.deleteProduitByNom("Souris");

        assertTrue(result);
        assertEquals(initialCount - 1, readService.getTotalProduits());
    }

    @Test
    @DisplayName("Test suppression par nom avec mauvaise casse")
    void testDeleteProduitByNomCaseInsensitive() {
        // Le nom est "Souris" mais on cherche "SOURIS"
        boolean result = deleteService.deleteProduitByNom("SOURIS");

        assertTrue(result);
    }

    @Test
    @DisplayName("Test suppression multiple par IDs")
    void testDeleteMultipleProduits() {
        // Créer un produit supplémentaire
        Produit p4 = createService.createProduit("Écran", 300.0, 8);

        List<Long> ids = Arrays.asList(
                readService.getProduitByNom("Laptop").get().getId(),
                readService.getProduitByNom("Souris").get().getId()
        );

        int deleted = deleteService.deleteProduitsByIds(ids);

        assertEquals(2, deleted);
        assertEquals(2, readService.getTotalProduits()); // Restent: Clavier et Écran
    }

    @Test
    @DisplayName("Test suppression produits trop chers")
    void testDeleteProduitsTropChers() {
        // Ajouter un produit cher
        createService.createProduit("Serveur", 5000.0, 2);

        int deleted = deleteService.deleteProduitsTropChers(2000.0);

        assertEquals(1, deleted); // Serveur supprimé
        assertEquals(3, readService.getTotalProduits()); // Restent les 3 premiers
    }

    @Test
    @DisplayName("Test suppression produits en rupture")
    void testDeleteProduitsEnRupture() {
        // Ajouter un produit en rupture
        createService.createProduit("Câble", 10.0, 0);

        int deleted = deleteService.deleteProduitsEnRupture();

        assertEquals(1, deleted); // Câble supprimé
        assertEquals(3, readService.getTotalProduits()); // Restent les 3 premiers
    }

    @Test
    @DisplayName("Test suppression conditionnelle")
    void testDeleteIfExists() {
        Long existingId = readService.getProduitByNom("Laptop").get().getId();
        Long nonExistingId = 999L;

        assertTrue(deleteService.deleteIfExists(existingId));
        assertFalse(deleteService.deleteIfExists(nonExistingId));
    }

    @Test
    @DisplayName("Test suppression de tous les produits")
    void testDeleteAllProduits() {
        deleteService.deleteAllProduits();

        assertEquals(0, readService.getTotalProduits());
        assertTrue(readService.getAllProduits().isEmpty());
    }
}