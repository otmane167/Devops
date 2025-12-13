package com.ehei.gi4;

import com.ehei.gi4.exception.*;
import com.ehei.gi4.produit.Produit;
import com.ehei.gi4.service.ProduitService;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ProduitServiceTest {

    private ProduitService service;

    @BeforeEach
    void setUp() {
        service = new ProduitService();
    }

    @Test
    void testAjouterProduitValide() {
        com.ehei.gi4.produit.Produit p = new com.ehei.gi4.produit.Produit(1L, "PC", 5000.0, 3);
        service.ajouterProduit(p);
        List<com.ehei.gi4.produit.Produit> all = service.getAllProduits();
        assertEquals(1, all.size());
        assertEquals("PC", all.get(0).getNom());
    }

    @Test
    void testPrixNegatif() {
        com.ehei.gi4.produit.Produit p = new com.ehei.gi4.produit.Produit(1L, "PC", -10.0, 2);
        assertThrows(ProduitInvalideException.class, () -> service.ajouterProduit(p));
    }

    @Test
    void testQuantiteNegative() {
        com.ehei.gi4.produit.Produit p = new com.ehei.gi4.produit.Produit(1L, "PC", 450.0, -1);
        assertThrows(ProduitInvalideException.class, () -> service.ajouterProduit(p));
    }

    @Test
    void testIdDuplique() {
        com.ehei.gi4.produit.Produit p1 = new com.ehei.gi4.produit.Produit(1L, "PC", 300.0, 4);
        com.ehei.gi4.produit.Produit p2 = new com.ehei.gi4.produit.Produit(1L, "Ecran", 150.0, 2);
        service.ajouterProduit(p1);
        assertThrows(ProduitDupliqueException.class, () -> service.ajouterProduit(p2));
    }

    @Test
    void testNomDuplique() {
        com.ehei.gi4.produit.Produit p1 = new com.ehei.gi4.produit.Produit(1L, "PC", 300.0, 4);
        com.ehei.gi4.produit.Produit p2 = new com.ehei.gi4.produit.Produit(2L, "PC", 150.0, 2);
        service.ajouterProduit(p1);
        assertThrows(ProduitDupliqueException.class, () -> service.ajouterProduit(p2));
    }

    @Test
    void testGetProduitById() {
        com.ehei.gi4.produit.Produit p = new com.ehei.gi4.produit.Produit(1L, "PC", 300.0, 4);
        service.ajouterProduit(p);
        com.ehei.gi4.produit.Produit found = service.getProduitById(1L);
        assertEquals("PC", found.getNom());
    }

    @Test
    void testGetProduitByIdNotFound() {
        assertThrows(ProduitNotFoundException.class, () -> service.getProduitById(999L));
    }

    @Test
    void testGetProduitByNom() {
        com.ehei.gi4.produit.Produit p = new com.ehei.gi4.produit.Produit(1L, "PC", 300.0, 4);
        service.ajouterProduit(p);
        Produit found = service.getProduitByNom("PC");
        assertEquals(1L, found.getId());
    }
}
