import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IProductCategory } from 'app/shared/model/inventory/product-category.model';
import { ProductCategoryService } from './product-category.service';
import { CategoryEvent } from '../../../shared/model/inventory/category-event-model';

@Component({
    selector: 'jhi-product-category-update',
    templateUrl: './product-category-update.component.html'
})
export class ProductCategoryUpdateComponent implements OnInit {
    productCategory: IProductCategory;
    isSaving: boolean;

    constructor(private productCategoryService: ProductCategoryService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ productCategory }) => {
            this.productCategory = productCategory;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.productCategory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.productCategoryService.createEvent(new CategoryEvent(this.productCategory, 'CATEGORY_UPDATED'))
            );
        } else {
            this.productCategory.id = this.randomInt();
            this.subscribeToSaveResponse(
                this.productCategoryService.createEvent(new CategoryEvent(this.productCategory, 'CATEGORY_CREATED'))
            );
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private randomInt() {
        return Math.floor(Math.random() * (10000 - 1 + 1)) + 1;
    }
}
