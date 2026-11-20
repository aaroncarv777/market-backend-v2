package com.merida.tecnm.market_backend_v2.web.controller;

import com.merida.tecnm.market_backend_v2.domain.Category;
import com.merida.tecnm.market_backend_v2.domain.service.CategoryService;
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
@RequestMapping("/categories")
@Tag(name = "Category", description = "Manage product categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    @Operation(summary = "Get all categories", description = "Return a list of all product categories")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of categories")
    public ResponseEntity<List<Category>> getAll() {
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Return a category by its ID if it exists")
    @ApiResponse(responseCode = "200", description = "Category found")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<Category> getCategory(
            @Parameter(description = "Category ID", example = "1", required = true)
            @PathVariable("id") int categoryId) {
        return categoryService.getCategory(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @Operation(
            summary = "Create a new category",
            description = "Register a new category",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Category Example",
                                    value = """
                                            {
                                              "category": "Snacks y Golosinas",
                                              "active": true
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid category data")
    public ResponseEntity<Category> save(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.save(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing category",
            description = "Update an existing category by its ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Category Update Example",
                                    value = """
                                            {
                                              "category": "Snacks, Dulces y Golosinas",
                                              "active": true
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<Category> update(
            @Parameter(description = "Category ID to update", example = "1", required = true)
            @PathVariable("id") int categoryId,
            @RequestBody Category category) {
        return categoryService.update(categoryId, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category by ID", description = "Deletes a category if it exists")
    @ApiResponse(responseCode = "200", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity delete(
            @Parameter(description = "ID of the category to be deleted", example = "6", required = true)
            @PathVariable("id") int categoryId) {
        if (categoryService.delete(categoryId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
