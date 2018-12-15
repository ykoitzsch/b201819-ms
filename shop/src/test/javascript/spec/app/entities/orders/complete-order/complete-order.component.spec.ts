/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ShopTestModule } from '../../../../test.module';
import { CompleteOrderComponent } from 'app/entities/orders/complete-order/complete-order.component';
import { CompleteOrderService } from 'app/entities/orders/complete-order/complete-order.service';
import { CompleteOrder } from 'app/shared/model/orders/complete-order.model';

describe('Component Tests', () => {
    describe('CompleteOrder Management Component', () => {
        let comp: CompleteOrderComponent;
        let fixture: ComponentFixture<CompleteOrderComponent>;
        let service: CompleteOrderService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ShopTestModule],
                declarations: [CompleteOrderComponent],
                providers: []
            })
                .overrideTemplate(CompleteOrderComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CompleteOrderComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompleteOrderService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new CompleteOrder(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.completeOrders[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
