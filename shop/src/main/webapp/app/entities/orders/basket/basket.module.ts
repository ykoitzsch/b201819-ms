import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ShopSharedModule } from 'app/shared';
import {
    BasketComponent,
    BasketDetailComponent,
    BasketUpdateComponent,
    BasketDeletePopupComponent,
    BasketDeleteDialogComponent,
    basketRoute,
    basketPopupRoute,
    MyBasketComponent
} from '.';

const ENTITY_STATES = [...basketRoute, ...basketPopupRoute];

@NgModule({
    imports: [ShopSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BasketComponent,
        BasketDetailComponent,
        BasketUpdateComponent,
        BasketDeleteDialogComponent,
        BasketDeletePopupComponent,
        MyBasketComponent
    ],
    entryComponents: [BasketComponent, BasketUpdateComponent, BasketDeleteDialogComponent, BasketDeletePopupComponent, MyBasketComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShopBasketModule {}
