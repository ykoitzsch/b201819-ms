import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ShopCustomerModule as CustomersCustomerModule } from './customers/customer/customer.module';
import { ShopProductModule as IvnentoryProductModule } from './ivnentory/product/product.module';
import { ShopProductCategoryModule as IvnentoryProductCategoryModule } from './ivnentory/product-category/product-category.module';
import { ShopCompleteOrderModule as OrdersCompleteOrderModule } from './orders/complete-order/complete-order.module';
import { ShopProductOrderModule as OrdersProductOrderModule } from './orders/product-order/product-order.module';
import { ShopBasketModule as OrdersBasketModule } from './orders/basket/basket.module';
import { ShopRatingModule as RatingsRatingModule } from './ratings/rating/rating.module';
import { ShopInvoiceModule as InvoicesInvoiceModule } from './invoices/invoice/invoice.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        CustomersCustomerModule,
        IvnentoryProductModule,
        IvnentoryProductCategoryModule,
        OrdersCompleteOrderModule,
        OrdersProductOrderModule,
        OrdersBasketModule,
        RatingsRatingModule,
        InvoicesInvoiceModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShopEntityModule {}
