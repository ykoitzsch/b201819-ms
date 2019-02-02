import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';

export class CompleteOrderEvent {
    constructor(public completeOrder: ICompleteOrder, public event: String) {}
}
