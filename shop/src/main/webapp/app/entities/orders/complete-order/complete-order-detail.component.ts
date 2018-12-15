import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';

@Component({
    selector: 'jhi-complete-order-detail',
    templateUrl: './complete-order-detail.component.html'
})
export class CompleteOrderDetailComponent implements OnInit {
    completeOrder: ICompleteOrder;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ completeOrder }) => {
            this.completeOrder = completeOrder;
        });
    }

    previousState() {
        window.history.back();
    }
}
