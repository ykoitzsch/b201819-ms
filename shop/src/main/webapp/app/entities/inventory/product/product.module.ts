import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ShopSharedModule } from 'app/shared';
import {
    ProductComponent,
    ProductDetailComponent,
    ProductUpdateComponent,
    ProductDeletePopupComponent,
    ProductDeleteDialogComponent,
    ProductShopComponent,
    productRoute,
    productPopupRoute
} from '.';

const ENTITY_STATES = [...productRoute, ...productPopupRoute];

@NgModule({
    imports: [ShopSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProductComponent,
        ProductDetailComponent,
        ProductUpdateComponent,
        ProductDeleteDialogComponent,
        ProductDeletePopupComponent,
        ProductShopComponent
    ],
    entryComponents: [
        ProductComponent,
        ProductUpdateComponent,
        ProductDeleteDialogComponent,
        ProductDeletePopupComponent,
        ProductShopComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShopProductModule {}
