import { IProduct } from 'app/shared/model/inventory/product.model';

export interface IProductCategory {
    id?: number;
    name?: string;
    products?: IProduct[];
}

export class ProductCategory implements IProductCategory {
    constructor(public id?: number, public name?: string, public products?: IProduct[]) {}
}
