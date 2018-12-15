package com.jhipster.bachelor.invoices.service;

import com.jhipster.bachelor.invoices.domain.Invoice;
import com.jhipster.bachelor.invoices.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Invoice.
 */
@Service
@Transactional
public class InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save
     * @return the persisted entity
     */
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Get all the invoices.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Invoice> findAll() {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll();
    }


    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }
}
