import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProductOrder } from 'app/shared/model/orders/product-order.model';
import { ProductOrderService } from './product-order.service';
import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { CompleteOrderService } from 'app/entities/orders/complete-order';
import { IBasket } from 'app/shared/model/orders/basket.model';
import { BasketService } from 'app/entities/orders/basket';
import { ProductOrderEvent } from 'app/shared/model/orders/product-order-event.model';

@Component({
    selector: 'jhi-product-order-update',
    templateUrl: './product-order-update.component.html'
})
export class ProductOrderUpdateComponent implements OnInit {
    productOrder: IProductOrder;
    isSaving: boolean;

    completeorders: ICompleteOrder[];

    baskets: IBasket[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private productOrderService: ProductOrderService,
        private completeOrderService: CompleteOrderService,
        private basketService: BasketService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ productOrder }) => {
            this.productOrder = productOrder;
        });
        this.completeOrderService.query().subscribe(
            (res: HttpResponse<ICompleteOrder[]>) => {
                this.completeorders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.basketService.query().subscribe(
            (res: HttpResponse<IBasket[]>) => {
                this.baskets = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.productOrder.id !== undefined) {
            this.subscribeToSaveResponse(
                this.productOrderService.createEvent(new ProductOrderEvent(this.productOrder, 'PRODUCT_ORDER_UPDATED'))
            );
        } else {
            this.productOrder.id = this.randomInt();
            this.subscribeToSaveResponse(
                this.productOrderService.createEvent(new ProductOrderEvent(this.productOrder, 'PRODUCT_ORDER_CREATED'))
            );
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProductOrder>>) {
        result.subscribe((res: HttpResponse<IProductOrder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCompleteOrderById(index: number, item: ICompleteOrder) {
        return item.id;
    }

    trackBasketById(index: number, item: IBasket) {
        return item.id;
    }

    private randomInt() {
        return Math.floor(Math.random() * (10000 - 1 + 1)) + 1;
    }
}
