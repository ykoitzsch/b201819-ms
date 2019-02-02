import { IInvoice } from 'app/shared/model/invoices/invoice.model';

export class InvoiceEvent {
    constructor(public invoice: IInvoice, public event: String) {}
}
