import { AccountService } from 'app/core';
import { CompleteOrderService } from '../complete-order/complete-order.service';
import { CompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { BasketService } from 'app/entities/orders/basket';
import { ProductOrder } from '../../../shared/model/orders/product-order.model';
import { JhiAlertService } from 'ng-jhipster';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBasket } from 'app/shared/model/orders/basket.model';
import { ProductService, ProductDeletePopupComponent } from '../../inventory/product';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Product } from '../../../shared/model/inventory/product.model';
import { OrderStatus } from '../../../shared/model/orders/complete-order.model';

class ProductDto {
    constructor(
        private amount?: number,
        private name?: String,
        private price?: number,
        private image?: String,
        private description?: String,
        private category?: String,
        private productOrder?: ProductOrder
    ) {}
}

@Component({
    selector: 'jhi-basket-detail',
    templateUrl: './basket-detail.component.html'
})
export class BasketDetailComponent implements OnInit {
    basket: IBasket;
    products: ProductDto[] = [];
    totalProducts: number;
    totalPrice: number;

    constructor(
        private activatedRoute: ActivatedRoute,
        private productService: ProductService,
        private jhiAlertService: JhiAlertService,
        private basketService: BasketService,
        private completeOrderService: CompleteOrderService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ basket }) => {
            this.basket = basket;
            this.loadAllProducts();
        });
    }

    previousState() {
        window.history.back();
    }

    orderNow() {
        if (this.products.length > 0) {
            this.completeOrderService
                .create(
                    new CompleteOrder(
                        null,
                        null,
                        OrderStatus.PENDING,
                        this.basket.customerId,
                        this.totalPrice,
                        new Date(),
                        this.basket.productOrders
                    )
                )
                .subscribe(
                    (res: HttpResponse<CompleteOrder>) => {
                        this.products = [];
                    },
                    (res: HttpErrorResponse) => {
                        console.log(res);
                    }
                );
        }
    }

    loadAllProducts() {
        this.totalProducts = 0;
        this.totalPrice = 0.0;
        for (const productOrder of this.basket.productOrders) {
            this.productService.find(productOrder.productId).subscribe(
                (res: HttpResponse<Product>) => {
                    const product = res.body;
                    this.totalProducts = this.totalProducts + productOrder.amount;
                    this.totalPrice = this.totalPrice + productOrder.amount * product.price;
                    this.products.push(
                        new ProductDto(
                            productOrder.amount,
                            product.name,
                            product.price,
                            product.image,
                            product.description,
                            product.productCategory.name,
                            productOrder
                        )
                    );
                },
                (res: HttpErrorResponse) => {
                    this.jhiAlertService.error(res.status + ': Could not load product with id ' + productOrder.productId);
                }
            );
        }
    }

    remove(productDto) {
        this.basket.productOrders.splice(this.basket.productOrders.indexOf(productDto.productOrder), 1);
        this.basketService.update(this.basket).subscribe(
            (res: HttpResponse<IBasket>) => {
                this.products.splice(this.products.indexOf(productDto), 1);
                this.jhiAlertService.success(productDto.name + ' has been removed from the basket');
                this.totalProducts = this.totalProducts - productDto.amount;
                this.totalPrice = this.totalPrice - productDto.amount * productDto.price;
            },
            (res: HttpErrorResponse) => {
                this.jhiAlertService.error(res.status + ': Could not delete productOrder with id ' + productDto.productOrder.id);
            }
        );
    }
}
