import { IProductOrder } from 'app/shared/model/orders/product-order.model';

export const enum OrderStatus {
    COMPLETED = 'COMPLETED',
    PENDING = 'PENDING',
    CANELLED = 'CANELLED'
}

export interface ICompleteOrder {
    id?: number;
    invoiceId?: number;
    status?: OrderStatus;
    customerId?: number;
    totalPrice?: number;
    orderDate?: string;
    productOrders?: IProductOrder[];
}

export class CompleteOrder implements ICompleteOrder {
    constructor(
        public id?: number,
        public invoiceId?: number,
        public status?: OrderStatus,
        public customerId?: number,
        public totalPrice?: number,
        public orderDate?: string,
        public productOrders?: IProductOrder[]
    ) {}
}
