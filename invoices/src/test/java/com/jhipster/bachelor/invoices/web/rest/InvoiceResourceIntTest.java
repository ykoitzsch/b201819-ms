package com.jhipster.bachelor.invoices.web.rest;

import com.jhipster.bachelor.invoices.InvoicesApp;

import com.jhipster.bachelor.invoices.domain.Invoice;
import com.jhipster.bachelor.invoices.repository.InvoiceRepository;
import com.jhipster.bachelor.invoices.service.InvoiceService;
import com.jhipster.bachelor.invoices.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.jhipster.bachelor.invoices.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jhipster.bachelor.invoices.domain.enumeration.InvoiceStatus;
/**
 * Test class for the InvoiceResource REST controller.
 *
 * @see InvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoicesApp.class)
public class InvoiceResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final InvoiceStatus DEFAULT_STATUS = InvoiceStatus.PAID;
    private static final InvoiceStatus UPDATED_STATUS = InvoiceStatus.PENDING;

    private static final String DEFAULT_DUE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DUE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_DATE = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .code(DEFAULT_CODE)
            .status(DEFAULT_STATUS)
            .dueDate(DEFAULT_DUE_DATE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .amount(DEFAULT_AMOUNT)
            .customerId(DEFAULT_CUSTOMER_ID)
            .orderId(DEFAULT_ORDER_ID);
        return invoice;
    }

    @Before
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoice.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testInvoice.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testInvoice.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvoice.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testInvoice.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setStatus(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceService.save(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .dueDate(UPDATED_DUE_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT)
            .customerId(UPDATED_CUSTOMER_ID)
            .orderId(UPDATED_ORDER_ID);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoice)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoice.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testInvoice.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testInvoice.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoice.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testInvoice.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceService.save(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Get the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }
}
