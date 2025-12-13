package com.ehei.gi4.service;

import com.ehei.gi4.exception.ProduitDupliqueException;
import com.ehei.gi4.exception.ProduitInvalideException;
import com.ehei.gi4.exception.ProduitNotFoundException;
import com.ehei.gi4.produit.Produit;

import java.util.List;

public class UpdateProduitService extends ProduitService {

    public Produit updateProduit(Long id, Produit nouveauProduit) {
        // Récupérer le produit existant
        Produit produitExistant = getProduitById(id);

        // Valider le nouveau produit
        validateProduit(nouveauProduit);

        // Vérifier unicité du nom (sauf produit actuel)
        verifierUniciteNomPourUpdate(id, nouveauProduit.getNom());

        // Mettre à jour les attributs
        produitExistant.setNom(nouveauProduit.getNom());
        produitExistant.setPrix(nouveauProduit.getPrix());
        produitExistant.setQuantite(nouveauProduit.getQuantite());

        return produitExistant;
    }

    public Produit updateNom(Long id, String nouveauNom) {
        // Récupérer le produit
        Produit produit = getProduitById(id);

        // Validation du nom
        if (nouveauNom == null || nouveauNom.trim().isEmpty()) {
            throw new ProduitInvalideException("Le nom ne peut pas être vide");
        }

        // Vérifier unicité
        verifierUniciteNomPourUpdate(id, nouveauNom);

        // Mettre à jour
        produit.setNom(nouveauNom.trim());

        return produit;
    }

    public Produit updatePrix(Long id, double nouveauPrix) {
        // Validation du prix
        if (nouveauPrix <= 0) {
            throw new ProduitInvalideException("Le prix doit être strictement positif");
        }

        // Récupérer et mettre à jour
        Produit produit = getProduitById(id);
        produit.setPrix(nouveauPrix);

        return produit;
    }

    public Produit updateQuantite(Long id, int nouvelleQuantite) {
        // Validation de la quantité
        if (nouvelleQuantite < 0) {
            throw new ProduitInvalideException("La quantité ne peut pas être négative");
        }

        // Récupérer et mettre à jour
        Produit produit = getProduitById(id);
        produit.setQuantite(nouvelleQuantite);

        return produit;
    }

    public Produit augmenterStock(Long id, int quantiteAAjouter) {
        // Validation
        if (quantiteAAjouter <= 0) {
            throw new ProduitInvalideException("La quantité à ajouter doit être > 0");
        }

        // Récupérer et augmenter
        Produit produit = getProduitById(id);
        produit.setQuantite(produit.getQuantite() + quantiteAAjouter);

        return produit;
    }

    public Produit diminuerStock(Long id, int quantiteARetirer) {
        // Validation
        if (quantiteARetirer <= 0) {
            throw new ProduitInvalideException("La quantité à retirer doit être > 0");
        }

        // Récupérer et vérifier stock suffisant
        Produit produit = getProduitById(id);
        int nouvelleQuantite = produit.getQuantite() - quantiteARetirer;

        if (nouvelleQuantite < 0) {
            throw new ProduitInvalideException(
                    "Quantité insuffisante. Stock actuel: " + produit.getQuantite()
            );
        }

        // Mettre à jour
        produit.setQuantite(nouvelleQuantite);

        return produit;
    }

    /** Vérifie qu'un nom n'est pas déjà utilisé par un autre produit */
    private void verifierUniciteNomPourUpdate(Long idProduitCourant, String nouveauNom) {
        List<Produit> produits = getAllProduits();

        boolean nomDejaUtilise = produits.stream()
                .filter(p -> !p.getId().equals(idProduitCourant)) // Exclure le produit courant
                .anyMatch(p -> p.getNom().equalsIgnoreCase(nouveauNom.trim()));

        if (nomDejaUtilise) {
            throw new ProduitDupliqueException(
                    "Un autre produit avec ce nom existe déjà: " + nouveauNom
            );
        }
    }

    /** Validation d'un produit (copie de votre logique) */
    private void validateProduit(Produit p) {
        if (p == null) {
            throw new ProduitInvalideException("Produit nul");
        }
        if (p.getNom() == null || p.getNom().trim().isEmpty()) {
            throw new ProduitInvalideException("Nom obligatoire");
        }
        if (p.getPrix() <= 0) {
            throw new ProduitInvalideException("Le prix doit être strictement positif");
        }
        if (p.getQuantite() < 0) {
            throw new ProduitInvalideException("La quantité ne peut pas être négative");
        }
    }
}