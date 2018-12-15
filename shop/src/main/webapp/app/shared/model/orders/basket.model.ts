import { IProductOrder } from 'app/shared/model/orders/product-order.model';

export interface IBasket {
    id?: number;
    customerId?: number;
    productOrders?: IProductOrder[];
}

export class Basket implements IBasket {
    constructor(public id?: number, public customerId?: number, public productOrders?: IProductOrder[]) {}
}
