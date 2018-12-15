import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IInvoice } from 'app/shared/model/invoices/invoice.model';
import { Principal } from 'app/core';
import { InvoiceService } from './invoice.service';

@Component({
    selector: 'jhi-invoice',
    templateUrl: './invoice.component.html'
})
export class InvoiceComponent implements OnInit, OnDestroy {
    invoices: IInvoice[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private invoiceService: InvoiceService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.invoiceService.query().subscribe(
            (res: HttpResponse<IInvoice[]>) => {
                this.invoices = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInInvoices();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IInvoice) {
        return item.id;
    }

    registerChangeInInvoices() {
        this.eventSubscriber = this.eventManager.subscribe('invoiceListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
