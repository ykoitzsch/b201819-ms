import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';

type EntityResponseType = HttpResponse<ICompleteOrder>;
type EntityArrayResponseType = HttpResponse<ICompleteOrder[]>;

@Injectable({ providedIn: 'root' })
export class CompleteOrderService {
    public resourceUrl = SERVER_API_URL + 'orders/api/complete-orders';
    public myOrdersUrl = SERVER_API_URL + 'orders/api/my-orders';

    constructor(private http: HttpClient) {}

    create(completeOrder: ICompleteOrder): Observable<EntityResponseType> {
        return this.http.post<ICompleteOrder>(this.resourceUrl, completeOrder, { observe: 'response' });
    }

    update(completeOrder: ICompleteOrder): Observable<EntityResponseType> {
        return this.http.put<ICompleteOrder>(this.resourceUrl, completeOrder, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICompleteOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICompleteOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByCustomerId(p?: any): Observable<EntityArrayResponseType> {
        return this.http.get<ICompleteOrder[]>(this.myOrdersUrl, { params: p, observe: 'response' });
    }
}
