package org.tp2.repository;

import org.tp2.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Vous pouvez ajouter des méthodes personnalisées si nécessaire, par exemple :
    // List<Product> findByName(String name);
}
