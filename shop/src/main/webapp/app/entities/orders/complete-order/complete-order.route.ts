import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { CompleteOrderService } from './complete-order.service';
import { CompleteOrderComponent } from './complete-order.component';
import { CompleteOrderDetailComponent } from './complete-order-detail.component';
import { CompleteOrderUpdateComponent } from './complete-order-update.component';
import { CompleteOrderDeletePopupComponent } from './complete-order-delete-dialog.component';
import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';

@Injectable({ providedIn: 'root' })
export class CompleteOrderResolve implements Resolve<ICompleteOrder> {
    constructor(private service: CompleteOrderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((completeOrder: HttpResponse<CompleteOrder>) => completeOrder.body));
        }
        return of(new CompleteOrder());
    }
}

export const completeOrderRoute: Routes = [
    {
        path: 'complete-order',
        component: CompleteOrderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CompleteOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'complete-order/:id/view',
        component: CompleteOrderDetailComponent,
        resolve: {
            completeOrder: CompleteOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CompleteOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'complete-order/new',
        component: CompleteOrderUpdateComponent,
        resolve: {
            completeOrder: CompleteOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CompleteOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'complete-order/:id/edit',
        component: CompleteOrderUpdateComponent,
        resolve: {
            completeOrder: CompleteOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CompleteOrders'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const completeOrderPopupRoute: Routes = [
    {
        path: 'complete-order/:id/delete',
        component: CompleteOrderDeletePopupComponent,
        resolve: {
            completeOrder: CompleteOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CompleteOrders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
