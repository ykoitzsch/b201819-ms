import { RatingService } from './../../ratings/rating/rating.service';
import { Rating } from './../../../shared/model/ratings/rating.model';
import { JhiAlertService } from 'ng-jhipster';
import { Basket } from 'app/shared/model/orders/basket.model';
import { AccountService } from '../../../core/auth/account.service';
import { ProductOrder } from '../../../shared/model/orders/product-order.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { IProduct } from 'app/shared/model/inventory/product.model';
import { BasketService } from '../../orders/basket';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-product-detail',
    templateUrl: './product-detail.component.html'
})
export class ProductDetailComponent implements OnInit {
    product: IProduct;
    amount: number;
    account: Account;
    basket: Basket;
    ratings: Rating[];
    currentRating: number;
    customerDesc: String;
    customerPoints: number;
    customerRatedAlready: boolean;

    constructor(
        private activatedRoute: ActivatedRoute,
        private accountService: AccountService,
        private basketService: BasketService,
        private jhiAlertService: JhiAlertService,
        private ratingService: RatingService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ product }) => {
            this.product = product;
            this.amount = 1;
            this.customerDesc = '';
            this.customerPoints = 1;
            this.currentRating = 0;
            this.customerRatedAlready = false;
        });

        this.accountService.get().subscribe(response => {
            if (response.status === 200) {
                this.account = response.body;
            }
        });
        this.loadRatings();
    }

    previousState() {
        window.history.back();
    }

    intoBasket(amount, product) {
        this.basketService.find(+this.account.id).subscribe(
            (res: HttpResponse<Basket>) => {
                this.basket = res.body;
                if (this.basket.productOrders === null) {
                    this.basket.productOrders = [];
                }
                this.basket.productOrders.push(new ProductOrder(null, amount, +this.account.id, product.id, null, null));
                this.basketService.update(this.basket).subscribe((r: HttpResponse<Basket>) => {
                    this.jhiAlertService.success(amount + 'x ' + product.name + ' has successfully been added to your basket!');
                });
            },
            (res: HttpErrorResponse) => {
                this.jhiAlertService.error(res.status + ': No basket with id ' + this.account.id + ' found');
            }
        );
    }

    calculateRating() {
        for (const r of this.ratings) {
            this.currentRating = this.currentRating + r.points;
        }
        this.currentRating = this.currentRating / this.ratings.length;
    }

    rate(points, desc) {
        this.ratingService.create(new Rating(null, points, this.product.id, +this.account.id, desc)).subscribe(
            (res: HttpResponse<Rating>) => {
                if (!this.ratingExists()) {
                    this.ratings.push(res.body);
                    this.ratingExists();
                } else {
                    this.jhiAlertService.error('You have already rated this product');
                }
            },
            (res: HttpErrorResponse) => {
                this.jhiAlertService.error('Rating could not been saved');
            }
        );
    }
    ratingExists() {
        for (const r of this.ratings) {
            if (r.customerId === +this.account.id) {
                this.customerRatedAlready = true;
                return true;
            }
        }
        return false;
    }

    loadRatings() {
        this.ratingService.query({ productId: this.product.id }).subscribe(
            (res: HttpResponse<Rating[]>) => {
                this.ratings = res.body;
                this.calculateRating();
                this.ratingExists();
            },
            (res: HttpErrorResponse) => {
                this.jhiAlertService.error('Rating Service is at the moment not available');
            }
        );
    }
}
