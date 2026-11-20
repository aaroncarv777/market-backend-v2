package com.merida.tecnm.market_backend_v2;

import com.merida.tecnm.market_backend_v2.domain.*;
import com.merida.tecnm.market_backend_v2.domain.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketBackendV2ApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PurchaseService purchaseService;

    @Test
    void contextLoads() {
        assertNotNull(categoryService);
        assertNotNull(clientService);
        assertNotNull(productService);
        assertNotNull(purchaseService);
    }

    @Test
    void testCategoryCrud() {
        // Create
        Category category = new Category();
        category.setCategory("Test Category");
        category.setActive(true);
        Category created = categoryService.save(category);
        assertNotNull(created.getCategoryId());
        int catId = created.getCategoryId();

        // Read
        Optional<Category> found = categoryService.getCategory(catId);
        assertTrue(found.isPresent());
        assertEquals("Test Category", found.get().getCategory());

        // Update
        Category toUpdate = new Category();
        toUpdate.setCategory("Updated Category");
        toUpdate.setActive(false);
        Optional<Category> updated = categoryService.update(catId, toUpdate);
        assertTrue(updated.isPresent());
        assertEquals("Updated Category", updated.get().getCategory());
        assertFalse(updated.get().isActive());

        // Delete
        boolean deleted = categoryService.delete(catId);
        assertTrue(deleted);
        assertFalse(categoryService.getCategory(catId).isPresent());
    }

    @Test
    void testClientCrud() {
        String testClientId = "TEST-CLI-999";

        // Create
        Client client = new Client();
        client.setClientId(testClientId);
        client.setName("Juan");
        client.setLastName("Perez");
        client.setCellphone(9991234567L);
        client.setAddress("Calle 50 #100");
        client.setEmail("juan.perez@test.com");
        Client created = clientService.save(client);
        assertNotNull(created);
        assertEquals(testClientId, created.getClientId());

        // Read
        Optional<Client> found = clientService.getClient(testClientId);
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getName());

        // Update
        Client toUpdate = new Client();
        toUpdate.setName("Juan Carlos");
        toUpdate.setLastName("Perez Lopez");
        toUpdate.setCellphone(9999876543L);
        toUpdate.setAddress("Calle 50 #100 Depto 2");
        toUpdate.setEmail("juan.carlos@test.com");
        Optional<Client> updated = clientService.update(testClientId, toUpdate);
        assertTrue(updated.isPresent());
        assertEquals("Juan Carlos", updated.get().getName());

        // Delete
        boolean deleted = clientService.delete(testClientId);
        assertTrue(deleted);
        assertFalse(clientService.getClient(testClientId).isPresent());
    }

    @Test
    void testProductCrud() {
        // Create
        Product product = new Product();
        product.setName("Test Product");
        product.setCategoryId(1);
        product.setPrice(150.0);
        product.setStock(50);
        product.setActive(true);
        Product created = productService.save(product);
        assertNotNull(created.getProductId());
        int prodId = created.getProductId();

        // Read
        Optional<Product> found = productService.getProduct(prodId);
        assertTrue(found.isPresent());
        assertEquals("Test Product", found.get().getName());

        // Update
        Product toUpdate = new Product();
        toUpdate.setName("Updated Product Name");
        toUpdate.setCategoryId(1);
        toUpdate.setPrice(180.0);
        toUpdate.setStock(40);
        toUpdate.setActive(true);
        Optional<Product> updated = productService.update(prodId, toUpdate);
        assertTrue(updated.isPresent());
        assertEquals("Updated Product Name", updated.get().getName());
        assertEquals(180.0, updated.get().getPrice());

        // Delete
        boolean deleted = productService.delete(prodId);
        assertTrue(deleted);
        assertFalse(productService.getProduct(prodId).isPresent());
    }

    @Test
    void testTransactionalPurchaseMasterDetail() {
        // Ensure client exists
        String clientId = "CLI-001";
        if (clientService.getClient(clientId).isEmpty()) {
            Client client = new Client();
            client.setClientId(clientId);
            client.setName("Cliente");
            client.setLastName("Prueba");
            client.setCellphone(9990001122L);
            client.setAddress("Calle Test");
            client.setEmail("test@cliente.com");
            clientService.save(client);
        }

        // Create Master-Detail Purchase
        Purchase purchase = new Purchase();
        purchase.setClientId(clientId);
        purchase.setPaymentMethod("TARJETA");
        purchase.setComment("Entregar por la tarde");
        purchase.setState("P");

        List<PurchaseItem> items = new ArrayList<>();

        PurchaseItem item1 = new PurchaseItem();
        item1.setProductId(15);
        item1.setQuantity(2);
        item1.setTotal(500.0);
        item1.setActive(true);
        items.add(item1);

        PurchaseItem item2 = new PurchaseItem();
        item2.setProductId(8);
        item2.setQuantity(1);
        item2.setTotal(150.0);
        item2.setActive(true);
        items.add(item2);

        purchase.setItems(items);

        // Save via service (with @Transactional and cascade = CascadeType.ALL)
        Purchase saved = purchaseService.save(purchase);

        assertNotNull(saved);
        assertNotNull(saved.getPurchaseId());
        assertTrue(saved.getPurchaseId() > 0);
        assertNotNull(saved.getItems());
        assertEquals(2, saved.getItems().size());

        // Verify retrieval by client
        Optional<List<Purchase>> clientPurchases = purchaseService.getByClient(clientId);
        assertTrue(clientPurchases.isPresent());
        assertFalse(clientPurchases.get().isEmpty());
    }
}
