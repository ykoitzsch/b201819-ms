import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IBasket } from 'app/shared/model/orders/basket.model';
import { Principal } from 'app/core';
import { BasketService } from './basket.service';

@Component({
    selector: 'jhi-basket',
    templateUrl: './basket.component.html'
})
export class BasketComponent implements OnInit, OnDestroy {
    baskets: IBasket[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private basketService: BasketService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.basketService.query().subscribe(
            (res: HttpResponse<IBasket[]>) => {
                this.baskets = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInBaskets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBasket) {
        return item.id;
    }

    registerChangeInBaskets() {
        this.eventSubscriber = this.eventManager.subscribe('basketListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
