import { Product } from 'app/shared/model/inventory/product.model';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { Principal, AccountService } from 'app/core';
import { CompleteOrderService } from './complete-order.service';
import { ProductService } from '../../inventory/product';
import { OrderStatus } from '../../../shared/model/orders/complete-order.model';

@Component({
    selector: 'jhi-complete-order',
    templateUrl: './complete-order-overview.component.html'
})
export class CompleteOrderOverviewComponent implements OnInit, OnDestroy {
    completeOrders: ICompleteOrder[];
    currentAccount: any;
    eventSubscriber: Subscription;
    products: Product[];

    constructor(
        private completeOrderService: CompleteOrderService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private productService: ProductService
    ) {}

    loadAll() {
        this.productService.query().subscribe(res => (this.products = res.body));
        this.completeOrderService.query().subscribe(
            (res: HttpResponse<ICompleteOrder[]>) => {
                this.completeOrders = res.body;
                for (const c of this.completeOrders) {
                    if (c.customerId !== this.currentAccount.id) {
                        this.completeOrders.splice(this.completeOrders.indexOf(c), 1); // remove orders which do not belong to current user
                    }
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.loadAll();
        this.registerChangeInCompleteOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    getProduct(productId) {
        for (const p of this.products) {
            if (p.id === productId) {
                return p;
            }
        }
    }

    payNow(order) {
        order.status = OrderStatus.COMPLETED;
        this.completeOrderService.update(order).subscribe(
            (res: HttpResponse<ICompleteOrder>) => {
                this.jhiAlertService.success('Order with order number ' + this.generateOrderNo(order) + ' has been paid');
            },
            (res: HttpErrorResponse) => {
                this.jhiAlertService.error(res.status + res.error, null, null);
                order.status = OrderStatus.PENDING;
            }
        );
    }

    hide(order) {
        this.completeOrders.splice(this.completeOrders.indexOf(order), 1);
    }
    generateOrderNo(timestamp) {
        return timestamp.replace(/[^a-zA-Z0-9]/g, '');
    }
    trackId(index: number, item: ICompleteOrder) {
        return item.id;
    }

    registerChangeInCompleteOrders() {
        this.eventSubscriber = this.eventManager.subscribe('completeOrderListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
