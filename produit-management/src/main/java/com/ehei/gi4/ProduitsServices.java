package com.ehei.gi4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


public abstract class ProduitsServices {


    protected static final List<Produit> produits = new ArrayList<>();
    protected static final AtomicLong nextId = new AtomicLong(1L);


    protected void validateProduit(Produit produit) {
        if (produit == null) {
            throw new IllegalArgumentException("Le produit ne peut pas être null");
        }

        if (produit.getNom() == null || produit.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }

        if (produit.getPrix() <= 0) {
            throw new IllegalArgumentException("Le prix doit être > 0");
        }

        if (produit.getQuantite() < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
    }


    protected Optional<Produit> findProduitById(Long id) {
        return produits.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }


    protected Optional<Produit> findProduitByNom(String nom) {
        if (nom == null) return Optional.empty();
        return produits.stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nom.trim()))
                .findFirst();
    }


    protected Long generateNextId() {
        return nextId.getAndIncrement();
    }

    public List<Produit> getAllProduits() {
        return Collections.unmodifiableList(produits);
    }


    public int getTotalProduits() {
        return produits.size();
    }

    public void clearAllProduits() {
        produits.clear();
        nextId.set(1L);
    }
}