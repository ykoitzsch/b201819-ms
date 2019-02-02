import { ProductOrderEvent } from '../../../shared/model/orders/product-order-event.model';
import { ProductOrder } from '../../../shared/model/orders/product-order.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProductOrder } from 'app/shared/model/orders/product-order.model';

type EntityResponseType = HttpResponse<IProductOrder>;
type EntityArrayResponseType = HttpResponse<IProductOrder[]>;

@Injectable({ providedIn: 'root' })
export class ProductOrderService {
    public resourceUrl = SERVER_API_URL + 'orders/api/product-orders';
    public eventUrl = SERVER_API_URL + 'orders/api/productOrder-events';

    constructor(private http: HttpClient) {}

    create(productOrder: IProductOrder): Observable<EntityResponseType> {
        return this.http.post<IProductOrder>(this.resourceUrl, productOrder, { observe: 'response' });
    }

    createEvent(event: ProductOrderEvent): Observable<HttpResponse<any>> {
        return this.http.post<ProductOrderEvent>(`${this.eventUrl}/${event.event}`, event.productOrder, { observe: 'response' });
    }

    update(productOrder: IProductOrder): Observable<EntityResponseType> {
        return this.http.put<IProductOrder>(this.resourceUrl, productOrder, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProductOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProductOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
