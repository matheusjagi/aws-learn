package br.com.jagi.aws_learn.service;

import br.com.jagi.aws_learn.model.Product;
import br.com.jagi.aws_learn.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public Product findByCode(String code) {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product code not found"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) {
        existsIdException(product.getId());
        return productRepository.save(product);
    }

    public void delete(Long id) {
        existsIdException(id);
        productRepository.deleteById(id);
    }

    private void existsIdException(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }
}
