import { IProductOrder } from './product-order.model';

export class ProductOrderEvent {
    constructor(public productOrder: IProductOrder, public event: String) {}
}
