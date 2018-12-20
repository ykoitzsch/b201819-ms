import { AccountService } from './../../../core/auth/account.service';
import { InvoiceService } from './../../invoices/invoice/invoice.service';
import { Invoice, InvoiceStatus } from './../../../shared/model/invoices/invoice.model';
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
    invoice: Invoice;
    acc: Account;

    constructor(
        private completeOrderService: CompleteOrderService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private productService: ProductService,
        private invoiceService: InvoiceService,
        private accountService: AccountService
    ) {}

    loadAll() {
        this.productService.query().subscribe(res => (this.products = res.body));
        console.log(this.currentAccount);
        this.completeOrderService.query({ customerId: this.acc.id }).subscribe(
            (res: HttpResponse<ICompleteOrder[]>) => {
                this.completeOrders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.accountService.get().subscribe(
            (res: HttpResponse<Account>) => {
                this.acc = res.body;
                this.loadAll();
            },
            (res: HttpErrorResponse) => this.jhiAlertService.error(res.message)
        );
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
        this.invoice = new Invoice();
        this.invoice.amount = order.totalPrice;
        this.invoice.code = 'INVOICECODE';
        this.invoice.customerId = order.customerId;
        this.invoice.dueDate = '01.01.2019';
        this.invoice.orderId = order.id;
        this.invoice.paymentDate = Date();
        this.invoice.status = InvoiceStatus.PAID;
        this.invoiceService.create(this.invoice).subscribe(r => {
            console.log(r);
        });
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
