export const enum InvoiceStatus {
    PAID = 'PAID',
    PENDING = 'PENDING',
    OVERDUE = 'OVERDUE'
}

export interface IInvoice {
    id?: number;
    code?: string;
    status?: InvoiceStatus;
    dueDate?: string;
    paymentDate?: string;
    amount?: number;
    customerId?: number;
    orderId?: number;
}

export class Invoice implements IInvoice {
    constructor(
        public id?: number,
        public code?: string,
        public status?: InvoiceStatus,
        public dueDate?: string,
        public paymentDate?: string,
        public amount?: number,
        public customerId?: number,
        public orderId?: number
    ) {}
}
