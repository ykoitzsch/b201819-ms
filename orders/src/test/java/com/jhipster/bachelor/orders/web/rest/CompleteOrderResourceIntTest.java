package com.jhipster.bachelor.orders.web.rest;

import com.jhipster.bachelor.orders.OrdersApp;

import com.jhipster.bachelor.orders.domain.CompleteOrder;
import com.jhipster.bachelor.orders.repository.CompleteOrderRepository;
import com.jhipster.bachelor.orders.service.CompleteOrderService;
import com.jhipster.bachelor.orders.web.rest.errors.ExceptionTranslator;

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


import static com.jhipster.bachelor.orders.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jhipster.bachelor.orders.domain.enumeration.OrderStatus;
/**
 * Test class for the CompleteOrderResource REST controller.
 *
 * @see CompleteOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrdersApp.class)
public class CompleteOrderResourceIntTest {

    private static final Long DEFAULT_INVOICE_ID = 1L;
    private static final Long UPDATED_INVOICE_ID = 2L;

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.COMPLETED;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PENDING;

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final String DEFAULT_ORDER_DATE = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_DATE = "BBBBBBBBBB";

    @Autowired
    private CompleteOrderRepository completeOrderRepository;
    
    @Autowired
    private CompleteOrderService completeOrderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompleteOrderMockMvc;

    private CompleteOrder completeOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompleteOrderResource completeOrderResource = new CompleteOrderResource(completeOrderService);
        this.restCompleteOrderMockMvc = MockMvcBuilders.standaloneSetup(completeOrderResource)
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
    public static CompleteOrder createEntity(EntityManager em) {
        CompleteOrder completeOrder = new CompleteOrder()
            .invoiceId(DEFAULT_INVOICE_ID)
            .status(DEFAULT_STATUS)
            .customerId(DEFAULT_CUSTOMER_ID)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .orderDate(DEFAULT_ORDER_DATE);
        return completeOrder;
    }

    @Before
    public void initTest() {
        completeOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompleteOrder() throws Exception {
        int databaseSizeBeforeCreate = completeOrderRepository.findAll().size();

        // Create the CompleteOrder
        restCompleteOrderMockMvc.perform(post("/api/complete-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(completeOrder)))
            .andExpect(status().isCreated());

        // Validate the CompleteOrder in the database
        List<CompleteOrder> completeOrderList = completeOrderRepository.findAll();
        assertThat(completeOrderList).hasSize(databaseSizeBeforeCreate + 1);
        CompleteOrder testCompleteOrder = completeOrderList.get(completeOrderList.size() - 1);
        assertThat(testCompleteOrder.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testCompleteOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCompleteOrder.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testCompleteOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testCompleteOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
    }

    @Test
    @Transactional
    public void createCompleteOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = completeOrderRepository.findAll().size();

        // Create the CompleteOrder with an existing ID
        completeOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompleteOrderMockMvc.perform(post("/api/complete-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(completeOrder)))
            .andExpect(status().isBadRequest());

        // Validate the CompleteOrder in the database
        List<CompleteOrder> completeOrderList = completeOrderRepository.findAll();
        assertThat(completeOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompleteOrders() throws Exception {
        // Initialize the database
        completeOrderRepository.saveAndFlush(completeOrder);

        // Get all the completeOrderList
        restCompleteOrderMockMvc.perform(get("/api/complete-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(completeOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCompleteOrder() throws Exception {
        // Initialize the database
        completeOrderRepository.saveAndFlush(completeOrder);

        // Get the completeOrder
        restCompleteOrderMockMvc.perform(get("/api/complete-orders/{id}", completeOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(completeOrder.getId().intValue()))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompleteOrder() throws Exception {
        // Get the completeOrder
        restCompleteOrderMockMvc.perform(get("/api/complete-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompleteOrder() throws Exception {
        // Initialize the database
        completeOrderService.save(completeOrder);

        int databaseSizeBeforeUpdate = completeOrderRepository.findAll().size();

        // Update the completeOrder
        CompleteOrder updatedCompleteOrder = completeOrderRepository.findById(completeOrder.getId()).get();
        // Disconnect from session so that the updates on updatedCompleteOrder are not directly saved in db
        em.detach(updatedCompleteOrder);
        updatedCompleteOrder
            .invoiceId(UPDATED_INVOICE_ID)
            .status(UPDATED_STATUS)
            .customerId(UPDATED_CUSTOMER_ID)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .orderDate(UPDATED_ORDER_DATE);

        restCompleteOrderMockMvc.perform(put("/api/complete-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompleteOrder)))
            .andExpect(status().isOk());

        // Validate the CompleteOrder in the database
        List<CompleteOrder> completeOrderList = completeOrderRepository.findAll();
        assertThat(completeOrderList).hasSize(databaseSizeBeforeUpdate);
        CompleteOrder testCompleteOrder = completeOrderList.get(completeOrderList.size() - 1);
        assertThat(testCompleteOrder.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testCompleteOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCompleteOrder.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCompleteOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCompleteOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCompleteOrder() throws Exception {
        int databaseSizeBeforeUpdate = completeOrderRepository.findAll().size();

        // Create the CompleteOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompleteOrderMockMvc.perform(put("/api/complete-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(completeOrder)))
            .andExpect(status().isBadRequest());

        // Validate the CompleteOrder in the database
        List<CompleteOrder> completeOrderList = completeOrderRepository.findAll();
        assertThat(completeOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCompleteOrder() throws Exception {
        // Initialize the database
        completeOrderService.save(completeOrder);

        int databaseSizeBeforeDelete = completeOrderRepository.findAll().size();

        // Get the completeOrder
        restCompleteOrderMockMvc.perform(delete("/api/complete-orders/{id}", completeOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompleteOrder> completeOrderList = completeOrderRepository.findAll();
        assertThat(completeOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompleteOrder.class);
        CompleteOrder completeOrder1 = new CompleteOrder();
        completeOrder1.setId(1L);
        CompleteOrder completeOrder2 = new CompleteOrder();
        completeOrder2.setId(completeOrder1.getId());
        assertThat(completeOrder1).isEqualTo(completeOrder2);
        completeOrder2.setId(2L);
        assertThat(completeOrder1).isNotEqualTo(completeOrder2);
        completeOrder1.setId(null);
        assertThat(completeOrder1).isNotEqualTo(completeOrder2);
    }
}
