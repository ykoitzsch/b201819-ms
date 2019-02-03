import { CompleteOrderEvent } from './../../../shared/model/orders/complete-order-event.model';
import { Account } from './../../../core/user/account.model';
import { AccountService } from './../../../core/auth/account.service';
import { InvoiceService } from './../../invoices/invoice/invoice.service';
import { Invoice, InvoiceStatus } from './../../../shared/model/invoices/invoice.model';
import { Product } from 'app/shared/model/inventory/product.model';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription, ObjectUnsubscribedError } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { v4 } from 'uuid';

import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { Principal, AccountService, User } from 'app/core';
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
    currentUser: User;

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
        this.accountService.get().subscribe(
            (res: HttpResponse<User>) => {
                this.completeOrderService.findByCustomerId({ customerId: res.body.id, login: res.body.login }).subscribe(
                    (r: HttpResponse<ICompleteOrder[]>) => {
                        this.completeOrders = r.body;
                    },
                    (r: HttpErrorResponse) => this.onError(r.message)
                );
            },
            (res: HttpErrorResponse) => this.jhiAlertService.error(res.message)
        );
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.loadAll();
        });

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

    /*
    payNow(order) {
        this.invoice = new Invoice();
        this.invoice.amount = order.totalPrice;
        this.invoice.code = this.generateInvoiceCode();
        this.invoice.customerId = order.customerId;
        this.invoice.dueDate = this.calculateDueDate();
        this.invoice.orderId = order.id;
        this.invoice.paymentDate = new Date().toDateString();
        this.invoice.status = InvoiceStatus.PAID;
        this.invoiceService.create(this.invoice).subscribe(
            (res: HttpResponse<Invoice>) => {
                order.status = OrderStatus.COMPLETED;
                order.invoiceId = res.body.id;
                this.completeOrderService.update(order).subscribe(
                    (r: HttpResponse<ICompleteOrder>) => {
                        this.jhiAlertService.success('Order with order number ' + this.generateOrderNo(order) + ' has been paid');
                    },
                    (r: HttpErrorResponse) => {
                        this.jhiAlertService.error(res.status + r.error, null, null);
                        order.status = OrderStatus.PENDING;
                    }
                );
            },
            (res: HttpErrorResponse) => {
                this.jhiAlertService.error(res.error);
            }
        );
    }
    */

    payNow(order) {
        order.status = OrderStatus.COMPLETED;
        this.completeOrderService.createEvent(new CompleteOrderEvent(order, 'COMPLETE_ORDER_UPDATED')).subscribe((r: HttpResponse<any>) => {
            this.jhiAlertService.success('Order with order number ' + this.generateOrderNo(order) + ' has been paid');
        });
    }

    /*
    generateInvoiceCode(): string {
        return 'xxxxxxxx'.replace(/[xy]/g, char => {
            const random = Math.random() * 16;
            const value = char === 'x' ? random : random % 4 + 8;
            return value.toString(7);
        });
    }

    calculateDueDate() {
        const date = new Date();
        date.setMonth(date.getMonth() + 1);
        return date.toDateString();
    }*/

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
