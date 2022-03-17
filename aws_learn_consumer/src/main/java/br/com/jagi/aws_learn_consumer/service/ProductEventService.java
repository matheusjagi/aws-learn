package br.com.jagi.aws_learn_consumer.service;

import br.com.jagi.aws_learn_consumer.model.ProductEventLogDTO;
import br.com.jagi.aws_learn_consumer.repository.ProductEventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventService {

    private final ProductEventLogRepository productEventLogRepository;

    public List<ProductEventLogDTO> getAll() {
        return StreamSupport
                .stream(productEventLogRepository.findAll().spliterator(), false)
                .map(ProductEventLogDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductEventLogDTO> getByCode(String code) {
        return productEventLogRepository.findAllByPk(code)
                .stream()
                .map(ProductEventLogDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductEventLogDTO> getByCodeAndEventType(String code, String eventType) {
        return productEventLogRepository.findAllByPkAndSkStartsWith(code, eventType)
                .stream()
                .map(ProductEventLogDTO::new)
                .collect(Collectors.toList());
    }
}
