import { MyBasketComponent } from './my-basket.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Basket } from 'app/shared/model/orders/basket.model';
import { BasketService } from './basket.service';
import { BasketComponent } from './basket.component';
import { BasketDetailComponent } from './basket-detail.component';
import { BasketUpdateComponent } from './basket-update.component';
import { BasketDeletePopupComponent } from './basket-delete-dialog.component';
import { IBasket } from 'app/shared/model/orders/basket.model';

@Injectable({ providedIn: 'root' })
export class BasketResolve implements Resolve<IBasket> {
    constructor(private service: BasketService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((basket: HttpResponse<Basket>) => basket.body));
        }
        return of(new Basket());
    }
}

export const basketRoute: Routes = [
    {
        path: 'basket',
        component: BasketComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Baskets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'my-basket',
        component: MyBasketComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'My Basket'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'basket/:id/view',
        component: BasketDetailComponent,
        resolve: {
            basket: BasketResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Baskets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'basket/new',
        component: BasketUpdateComponent,
        resolve: {
            basket: BasketResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Baskets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'basket/:id/edit',
        component: BasketUpdateComponent,
        resolve: {
            basket: BasketResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Baskets'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const basketPopupRoute: Routes = [
    {
        path: 'basket/:id/delete',
        component: BasketDeletePopupComponent,
        resolve: {
            basket: BasketResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Baskets'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
