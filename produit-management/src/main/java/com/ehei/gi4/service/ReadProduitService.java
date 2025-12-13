package com.ehei.gi4.service;

import com.ehei.gi4.produit.Produit;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReadProduitService {

    private final CreateProduitService createService = new CreateProduitService();

    // Get all products
    public List<Produit> getAllProduits() {
        return createService.getAllProduits();
    }

    // Get total products count
    public int getTotalProduits() {
        return createService.getAllProduits().size();
    }

    // Find a product by name (case-insensitive)
    public Optional<Produit> getProduitByNom(String nom) {
        return createService.getAllProduits().stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nom))
                .findFirst();
    }

    // Find products cheaper than a certain price
    public List<Produit> getProduitsMoinsChersQue(double prix) {
        return createService.getAllProduits().stream()
                .filter(p -> p.getPrix() < prix)
                .collect(Collectors.toList());
    }
}
