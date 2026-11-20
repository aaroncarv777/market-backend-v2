package com.merida.tecnm.market_backend_v2.web.controller;

import com.merida.tecnm.market_backend_v2.domain.Purchase;
import com.merida.tecnm.market_backend_v2.domain.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
@Tag(name = "Purchase", description = "Transactional purchase management (Master-Detail)")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("")
    @Operation(summary = "Get all purchases", description = "Return a list of all registered purchases")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of purchases")
    public ResponseEntity<List<Purchase>> getAll() {
        return new ResponseEntity<>(purchaseService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase by ID", description = "Return a purchase by its ID if it exists")
    @ApiResponse(responseCode = "200", description = "Purchase found")
    @ApiResponse(responseCode = "404", description = "Purchase not found")
    public ResponseEntity<Purchase> getPurchase(
            @Parameter(description = "Purchase ID", example = "1", required = true)
            @PathVariable("id") int purchaseId) {
        return purchaseService.getPurchase(purchaseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get purchases by client ID", description = "Return all purchases made by a specific client")
    @ApiResponse(responseCode = "200", description = "Purchases found")
    @ApiResponse(responseCode = "404", description = "Purchases not found for the client")
    public ResponseEntity<List<Purchase>> getByClient(
            @Parameter(description = "Client ID", example = "4546221", required = true)
            @PathVariable("clientId") String clientId) {
        return purchaseService.getByClient(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @Operation(
            summary = "Create a new purchase (Master-Detail)",
            description = "Register a purchase header with its acquired products in a single transaction (Master-Detail cascade save)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Complex Purchase Example (Ticket Maestro-Detalle)",
                                    value = """
                                            {
                                              "idCliente": "CLI-001",
                                              "medioPago": "TARJETA",
                                              "comentario": "Entregar por la tarde",
                                              "productosAdquiridos": [
                                                {
                                                  "idProducto": 15,
                                                  "cantidad": 2,
                                                  "total": 500.0
                                                },
                                                {
                                                  "idProducto": 8,
                                                  "cantidad": 1,
                                                  "total": 150.0
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Purchase created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid purchase data")
    public ResponseEntity<Purchase> save(@RequestBody Purchase purchase) {
        return new ResponseEntity<>(purchaseService.save(purchase), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing purchase",
            description = "Update purchase header information or status by its ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Purchase Update Example",
                                    value = """
                                            {
                                              "medioPago": "TARJETA",
                                              "comentario": "Entregar urgente por la mañana",
                                              "estado": "ENTREGADO"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Purchase updated successfully")
    @ApiResponse(responseCode = "404", description = "Purchase not found")
    public ResponseEntity<Purchase> update(
            @Parameter(description = "Purchase ID to update", example = "1", required = true)
            @PathVariable("id") int purchaseId,
            @RequestBody Purchase purchase) {
        return purchaseService.update(purchaseId, purchase)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete / Cancel a purchase by ID", description = "Deletes a purchase and its acquired products in cascade")
    @ApiResponse(responseCode = "200", description = "Purchase deleted successfully")
    @ApiResponse(responseCode = "404", description = "Purchase not found")
    public ResponseEntity delete(
            @Parameter(description = "Purchase ID to delete", example = "2", required = true)
            @PathVariable("id") int purchaseId) {
        if (purchaseService.delete(purchaseId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
