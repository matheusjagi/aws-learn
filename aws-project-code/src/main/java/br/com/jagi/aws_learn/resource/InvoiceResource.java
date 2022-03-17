package br.com.jagi.aws_learn.resource;

import br.com.jagi.aws_learn.model.Invoice;
import br.com.jagi.aws_learn.model.UrlResponse;
import br.com.jagi.aws_learn.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@Slf4j
@RequiredArgsConstructor
public class InvoiceResource {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<UrlResponse> createInvoiceUrl() {
        return new ResponseEntity<>(invoiceService.createInvoiceUrl(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> findAll() {
        return new ResponseEntity<>(invoiceService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/customername/{customerName}")
    public ResponseEntity<List<Invoice>> findByCustomerName(@PathVariable("customerName") String customerName) {
        return new ResponseEntity<>(invoiceService.getByCustomerName(customerName), HttpStatus.OK);
    }
}
