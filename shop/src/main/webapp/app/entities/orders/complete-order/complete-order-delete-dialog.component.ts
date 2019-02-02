import { CompleteOrderEvent } from './../../../shared/model/orders/complete-order-event.model';
import { ICompleteOrder } from './../../../shared/model/orders/complete-order.model';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { CompleteOrderService } from './complete-order.service';

@Component({
    selector: 'jhi-complete-order-delete-dialog',
    templateUrl: './complete-order-delete-dialog.component.html'
})
export class CompleteOrderDeleteDialogComponent {
    completeOrder: ICompleteOrder;

    constructor(
        private completeOrderService: CompleteOrderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.completeOrderService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'completeOrderListModification',
                content: 'Deleted an completeOrder'
            });
            this.activeModal.dismiss(true);
        });
    }

    confirmDeleteEvent(completeOrder: ICompleteOrder) {
        this.completeOrderService.createEvent(new CompleteOrderEvent(this.completeOrder, 'COMPLETE_ORDER_DELETED')).subscribe(response => {
            this.eventManager.broadcast({
                name: 'completeOrderListModification',
                content: 'Deleted a completeOrder'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-complete-order-delete-popup',
    template: ''
})
export class CompleteOrderDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ completeOrder }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CompleteOrderDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.completeOrder = completeOrder;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
