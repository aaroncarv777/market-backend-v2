package com.merida.tecnm.market_backend_v2.domain.service;

import com.merida.tecnm.market_backend_v2.domain.Purchase;
import com.merida.tecnm.market_backend_v2.domain.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public List<Purchase> getAll() {
        return purchaseRepository.getAll();
    }

    public Optional<List<Purchase>> getByClient(String clientId) {
        return purchaseRepository.getByClient(clientId);
    }

    public Optional<Purchase> getPurchase(int purchaseId) {
        return purchaseRepository.getPurchase(purchaseId);
    }

    @Transactional
    public Purchase save(Purchase purchase) {
        if (purchase.getDate() == null) {
            purchase.setDate(LocalDateTime.now());
        }
        if (purchase.getState() == null || purchase.getState().isBlank()) {
            purchase.setState("P");
        }
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public Optional<Purchase> update(int purchaseId, Purchase purchase) {
        return getPurchase(purchaseId).map(existing -> {
            purchase.setPurchaseId(purchaseId);
            if (purchase.getDate() == null) {
                purchase.setDate(existing.getDate());
            }
            if (purchase.getClientId() == null) {
                purchase.setClientId(existing.getClientId());
            }
            if (purchase.getItems() == null || purchase.getItems().isEmpty()) {
                purchase.setItems(existing.getItems());
            }
            return purchaseRepository.save(purchase);
        });
    }

    @Transactional
    public boolean delete(int purchaseId) {
        return getPurchase(purchaseId).map(purchase -> {
            purchaseRepository.delete(purchaseId);
            return true;
        }).orElse(false);
    }
}
