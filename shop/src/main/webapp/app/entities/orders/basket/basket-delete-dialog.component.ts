import { BasketEvent } from './../../../shared/model/orders/basket-event.model';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBasket } from 'app/shared/model/orders/basket.model';
import { BasketService } from './basket.service';

@Component({
    selector: 'jhi-basket-delete-dialog',
    templateUrl: './basket-delete-dialog.component.html'
})
export class BasketDeleteDialogComponent {
    basket: IBasket;

    constructor(private basketService: BasketService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.basketService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'basketListModification',
                content: 'Deleted an basket'
            });
            this.activeModal.dismiss(true);
        });
    }

    confirmDeleteEvent(basket: IBasket) {
        this.basketService.createEvent(new BasketEvent(this.basket, 'BASKET_DELETED')).subscribe(response => {
            this.eventManager.broadcast({
                name: 'basketListModification',
                content: 'Deleted a basket'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-basket-delete-popup',
    template: ''
})
export class BasketDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ basket }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BasketDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.basket = basket;
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
