package com.jhipster.bachelor.ratings.web.rest;

import com.jhipster.bachelor.ratings.RatingsApp;

import com.jhipster.bachelor.ratings.domain.Rating;
import com.jhipster.bachelor.ratings.repository.RatingRepository;
import com.jhipster.bachelor.ratings.service.RatingService;
import com.jhipster.bachelor.ratings.web.rest.errors.ExceptionTranslator;

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


import static com.jhipster.bachelor.ratings.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RatingResource REST controller.
 *
 * @see RatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatingsApp.class)
public class RatingResourceIntTest {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private RatingService ratingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRatingMockMvc;

    private Rating rating;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RatingResource ratingResource = new RatingResource(ratingService);
        this.restRatingMockMvc = MockMvcBuilders.standaloneSetup(ratingResource)
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
    public static Rating createEntity(EntityManager em) {
        Rating rating = new Rating()
            .points(DEFAULT_POINTS)
            .productId(DEFAULT_PRODUCT_ID)
            .customerId(DEFAULT_CUSTOMER_ID)
            .description(DEFAULT_DESCRIPTION);
        return rating;
    }

    @Before
    public void initTest() {
        rating = createEntity(em);
    }

    @Test
    @Transactional
    public void createRating() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating
        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isCreated());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate + 1);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testRating.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testRating.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testRating.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating with an existing ID
        rating.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setPoints(null);

        // Create the Rating, which fails.

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setDescription(null);

        // Create the Rating, which fails.

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRatings() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        // Get all the ratingList
        restRatingMockMvc.perform(get("/api/ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        // Get the rating
        restRatingMockMvc.perform(get("/api/ratings/{id}", rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rating.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRating() throws Exception {
        // Get the rating
        restRatingMockMvc.perform(get("/api/ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRating() throws Exception {
        // Initialize the database
        ratingService.save(rating);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        // Disconnect from session so that the updates on updatedRating are not directly saved in db
        em.detach(updatedRating);
        updatedRating
            .points(UPDATED_POINTS)
            .productId(UPDATED_PRODUCT_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .description(UPDATED_DESCRIPTION);

        restRatingMockMvc.perform(put("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testRating.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testRating.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testRating.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Create the Rating

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc.perform(put("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRating() throws Exception {
        // Initialize the database
        ratingService.save(rating);

        int databaseSizeBeforeDelete = ratingRepository.findAll().size();

        // Get the rating
        restRatingMockMvc.perform(delete("/api/ratings/{id}", rating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rating.class);
        Rating rating1 = new Rating();
        rating1.setId(1L);
        Rating rating2 = new Rating();
        rating2.setId(rating1.getId());
        assertThat(rating1).isEqualTo(rating2);
        rating2.setId(2L);
        assertThat(rating1).isNotEqualTo(rating2);
        rating1.setId(null);
        assertThat(rating1).isNotEqualTo(rating2);
    }
}
