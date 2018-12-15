export interface ICustomer {
    id?: number;
    name?: string;
    email?: string;
    address?: string;
}

export class Customer implements ICustomer {
    constructor(public id?: number, public name?: string, public email?: string, public address?: string) {}
}
