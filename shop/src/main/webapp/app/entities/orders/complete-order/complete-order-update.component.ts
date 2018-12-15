import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { CompleteOrderService } from './complete-order.service';

@Component({
    selector: 'jhi-complete-order-update',
    templateUrl: './complete-order-update.component.html'
})
export class CompleteOrderUpdateComponent implements OnInit {
    completeOrder: ICompleteOrder;
    isSaving: boolean;

    constructor(private completeOrderService: CompleteOrderService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ completeOrder }) => {
            this.completeOrder = completeOrder;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.completeOrder.id !== undefined) {
            this.subscribeToSaveResponse(this.completeOrderService.update(this.completeOrder));
        } else {
            this.subscribeToSaveResponse(this.completeOrderService.create(this.completeOrder));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICompleteOrder>>) {
        result.subscribe((res: HttpResponse<ICompleteOrder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
