import { IProductCategory } from 'app/shared/model/inventory/product-category.model';

export class CategoryEvent {
    constructor(public category: IProductCategory, public event: String) {}
}
