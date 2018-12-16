import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { Principal } from 'app/core';
import { CompleteOrderService } from './complete-order.service';

@Component({
    selector: 'jhi-complete-order',
    templateUrl: './complete-order-overview.component.html'
})
export class CompleteOrderOverviewComponent implements OnInit, OnDestroy {
    completeOrders: ICompleteOrder[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private completeOrderService: CompleteOrderService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.completeOrderService.query().subscribe(
            (res: HttpResponse<ICompleteOrder[]>) => {
                this.completeOrders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCompleteOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
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
