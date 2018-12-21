import { AccountService } from 'app/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoice } from 'app/shared/model/invoices/invoice.model';

@Component({
    selector: 'jhi-invoice-detail',
    templateUrl: './invoice-detail.component.html'
})
export class InvoiceDetailComponent implements OnInit {
    invoice: IInvoice;

    constructor(private activatedRoute: ActivatedRoute, private accountService: AccountService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ invoice }) => {
            this.invoice = invoice;
        });
    }

    previousState() {
        window.history.back();
    }
}
