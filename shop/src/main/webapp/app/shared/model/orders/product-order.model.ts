import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { IBasket } from 'app/shared/model/orders/basket.model';

export interface IProductOrder {
    id?: number;
    amount?: number;
    customerId?: number;
    productId?: number;
    completeOrder?: ICompleteOrder;
    basket?: IBasket;
}

export class ProductOrder implements IProductOrder {
    constructor(
        public id?: number,
        public amount?: number,
        public customerId?: number,
        public productId?: number,
        public completeOrder?: ICompleteOrder,
        public basket?: IBasket
    ) {}
}
