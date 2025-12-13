package com.ehei.gi4.service;

import com.ehei.gi4.exception.ProduitDupliqueException;
import com.ehei.gi4.exception.ProduitInvalideException;
import com.ehei.gi4.exception.ProduitNotFoundException;
import com.ehei.gi4.produit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProduitService {

    private final List<Produit> produits = new ArrayList<>();

    /** CREATE **/
    public Produit ajouterProduit(Produit p) {
        validateProduit(p);

        if (existsById(p.getId())) {
            throw new ProduitDupliqueException("ID dupliqué : " + p.getId());
        }
        if (existsByNom(p.getNom())) {
            throw new ProduitDupliqueException("Nom dupliqué : " + p.getNom());
        }

        produits.add(p);
        return p;
    }

    /** READ ALL **/
    public List<Produit> getAllProduits() {
        return new ArrayList<>(produits);
    }

    /** READ BY ID **/
    public Produit getProduitById(Long id) {
        return produits.stream().filter(p -> p.getId().equals(id)).findFirst().orElseThrow(() -> new ProduitNotFoundException("Produit introuvable avec ID : " + id));
    }

    /** READ BY NOM **/
    public Produit getProduitByNom(String nom) {
        return produits.stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new ProduitNotFoundException("Produit introuvable avec nom : " + nom));
    }

    private void validateProduit(Produit p) {
        if (p == null) {
            throw new ProduitInvalideException("Produit nul");
        }
        if (p.getId() == null) {
            throw new ProduitInvalideException("ID obligatoire");
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

    private boolean existsById(Long id) {
        return produits.stream().anyMatch(p -> p.getId().equals(id));
    }

    private boolean existsByNom(String nom) {
        return produits.stream().anyMatch(p -> p.getNom().equalsIgnoreCase(nom));
    }
}
