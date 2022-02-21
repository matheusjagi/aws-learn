package br.com.jagi.aws_learn.service;

import br.com.jagi.aws_learn.model.Product;
import br.com.jagi.aws_learn.model.enums.EventTypeEnum;
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
    private final ProductPublisher productPublisher;

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
        Product productCreated = productRepository.save(product);
        productPublisher.publishProductEvent(productCreated, EventTypeEnum.PRODUCT_CREATED, "Jagi");
        return productCreated;
    }

    public Product update(Product product) {
        existsIdException(product.getId());
        Product productUpdated = productRepository.save(product);
        productPublisher.publishProductEvent(productUpdated, EventTypeEnum.PRODUCT_UPDATE, "Iara");
        return productUpdated;
    }

    public void delete(Long id) {
        existsIdException(id);
        Product productDeleted = findById(id);
        productRepository.deleteById(id);
        productPublisher.publishProductEvent(productDeleted, EventTypeEnum.PRODUCT_DELETED, "Kimura");
    }

    private void existsIdException(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }
}
