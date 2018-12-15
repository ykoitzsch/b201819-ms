import { NgModule } from '@angular/core';

import { ShopSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ShopSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ShopSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ShopSharedCommonModule {}
