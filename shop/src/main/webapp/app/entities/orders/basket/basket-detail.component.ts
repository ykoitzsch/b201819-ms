import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBasket } from 'app/shared/model/orders/basket.model';

@Component({
    selector: 'jhi-basket-detail',
    templateUrl: './basket-detail.component.html'
})
export class BasketDetailComponent implements OnInit {
    basket: IBasket;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ basket }) => {
            this.basket = basket;
        });
    }

    previousState() {
        window.history.back();
    }
}
