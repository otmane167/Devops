package com.ehei.gi4.service;

import com.ehei.gi4.exception.ProduitNotFoundException;
import com.ehei.gi4.produit.Produit;

import java.util.ArrayList;
import java.util.List;

public class DeleteProduitService extends ProduitService {

    /** Supprime un produit par son ID */
    public boolean deleteProduitById(Long id) {
        try {
            // Récupérer le produit
            Produit produit = getProduitById(id);

            // Supprimer de la liste
            List<Produit> produits = getAllProduits();
            return produits.remove(produit);

        } catch (ProduitNotFoundException e) {
            // Relancer l'exception avec un message plus précis
            throw new ProduitNotFoundException("Produit non trouvé avec ID: " + id);
        }
    }

    /** Supprime un produit par son nom */
    public boolean deleteProduitByNom(String nom) {
        try {
            // Récupérer le produit
            Produit produit = getProduitByNom(nom);

            // Supprimer de la liste
            List<Produit> produits = getAllProduits();
            return produits.remove(produit);

        } catch (ProduitNotFoundException e) {
            throw new ProduitNotFoundException("Produit non trouvé avec nom: " + nom);
        }
    }

    /** Supprime plusieurs produits par leurs IDs */
    public int deleteProduitsByIds(List<Long> ids) {
        int count = 0;
        for (Long id : ids) {
            try {
                if (deleteProduitById(id)) {
                    count++;
                }
            } catch (ProduitNotFoundException e) {
                // Ignorer silencieusement les produits non trouvés
            }
        }
        return count;
    }

    /** Supprime les produits trop chers (prix > limite) */
    public int deleteProduitsTropChers(double prixMax) {
        List<Produit> produits = getAllProduits();
        List<Produit> aSupprimer = new ArrayList<>();

        // Identifier les produits à supprimer
        for (Produit p : produits) {
            if (p.getPrix() > prixMax) {
                aSupprimer.add(p);
            }
        }

        // Supprimer en une fois
        produits.removeAll(aSupprimer);

        return aSupprimer.size();
    }

    /** Supprime les produits en rupture de stock (quantité = 0) */
    public int deleteProduitsEnRupture() {
        List<Produit> produits = getAllProduits();
        List<Produit> aSupprimer = new ArrayList<>();

        // Identifier les produits à supprimer
        for (Produit p : produits) {
            if (p.getQuantite() == 0) {
                aSupprimer.add(p);
            }
        }

        // Supprimer en une fois
        produits.removeAll(aSupprimer);

        return aSupprimer.size();
    }

    /** Supprime tous les produits */
    public void deleteAllProduits() {
        List<Produit> produits = getAllProduits();
        produits.clear();
    }

    /** Supprime un produit s'il existe, retourne false sinon */
    public boolean deleteIfExists(Long id) {
        try {
            return deleteProduitById(id);
        } catch (ProduitNotFoundException e) {
            return false;
        }
    }
}