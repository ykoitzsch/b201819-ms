import { JhiAlertService } from 'ng-jhipster';
import { Basket } from 'app/shared/model/orders/basket.model';
import { AccountService } from '../../../core/auth/account.service';
import { ProductOrder } from '../../../shared/model/orders/product-order.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

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

    constructor(
        private activatedRoute: ActivatedRoute,
        private accountService: AccountService,
        private basketService: BasketService,
        private jhiAlertService: JhiAlertService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ product }) => {
            this.product = product;
            this.amount = 1;
        });
    }

    previousState() {
        window.history.back();
    }

    intoBasket(amount, product) {
        this.accountService.get().subscribe(response => {
            if (response.status === 200) {
                this.account = response.body;
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
        });
    }
}
