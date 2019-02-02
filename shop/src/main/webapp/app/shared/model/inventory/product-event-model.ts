import { IProduct } from './product.model';

export class ProductEvent {
    constructor(public product: IProduct, public event: String) {}
}
