/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShopTestModule } from '../../../../test.module';
import { CompleteOrderDetailComponent } from 'app/entities/orders/complete-order/complete-order-detail.component';
import { CompleteOrder } from 'app/shared/model/orders/complete-order.model';

describe('Component Tests', () => {
    describe('CompleteOrder Management Detail Component', () => {
        let comp: CompleteOrderDetailComponent;
        let fixture: ComponentFixture<CompleteOrderDetailComponent>;
        const route = ({ data: of({ completeOrder: new CompleteOrder(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ShopTestModule],
                declarations: [CompleteOrderDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CompleteOrderDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CompleteOrderDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.completeOrder).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
