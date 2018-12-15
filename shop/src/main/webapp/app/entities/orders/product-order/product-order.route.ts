import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ProductOrder } from 'app/shared/model/orders/product-order.model';
import { ProductOrderService } from './product-order.service';
import { ProductOrderComponent } from './product-order.component';
import { ProductOrderDetailComponent } from './product-order-detail.component';
import { ProductOrderUpdateComponent } from './product-order-update.component';
import { ProductOrderDeletePopupComponent } from './product-order-delete-dialog.component';
import { IProductOrder } from 'app/shared/model/orders/product-order.model';

@Injectable({ providedIn: 'root' })
export class ProductOrderResolve implements Resolve<IProductOrder> {
    constructor(private service: ProductOrderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((productOrder: HttpResponse<ProductOrder>) => productOrder.body));
        }
        return of(new ProductOrder());
    }
}

export const productOrderRoute: Routes = [
    {
        path: 'product-order',
        component: ProductOrderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product-order/:id/view',
        component: ProductOrderDetailComponent,
        resolve: {
            productOrder: ProductOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product-order/new',
        component: ProductOrderUpdateComponent,
        resolve: {
            productOrder: ProductOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product-order/:id/edit',
        component: ProductOrderUpdateComponent,
        resolve: {
            productOrder: ProductOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductOrders'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const productOrderPopupRoute: Routes = [
    {
        path: 'product-order/:id/delete',
        component: ProductOrderDeletePopupComponent,
        resolve: {
            productOrder: ProductOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductOrders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
