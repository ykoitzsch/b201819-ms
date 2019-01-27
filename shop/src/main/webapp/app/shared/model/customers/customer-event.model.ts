import { ICustomer } from './customer.model';

export class CustomerEvent {
    constructor(public customer: ICustomer, public event: String) {}
}
