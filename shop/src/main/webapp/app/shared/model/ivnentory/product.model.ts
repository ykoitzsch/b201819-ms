import { IProductCategory } from 'app/shared/model/ivnentory/product-category.model';

export interface IProduct {
    id?: number;
    name?: string;
    description?: string;
    price?: number;
    image?: string;
    productOrderId?: number;
    productCategory?: IProductCategory;
}

export class Product implements IProduct {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public price?: number,
        public image?: string,
        public productOrderId?: number,
        public productCategory?: IProductCategory
    ) {}
}
