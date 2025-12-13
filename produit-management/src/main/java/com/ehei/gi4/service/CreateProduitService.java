package com.ehei.gi4.service;

import com.ehei.gi4.produit.Produit;

import java.util.ArrayList;
import java.util.List;

public class CreateProduitService {

    private static final List<Produit> produits = new ArrayList<>();

    public Produit createProduit(Long id,String nom, double prix, int quantite) {
        Produit p = new Produit(id ,nom, prix, quantite);
        produits.add(p);
        return p;
    }

    public void clearAllProduits() {
        produits.clear();
    }

    public List<Produit> getAllProduits() {
        return new ArrayList<>(produits);
    }
}
