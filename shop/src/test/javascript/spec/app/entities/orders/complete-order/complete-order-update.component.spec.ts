/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ShopTestModule } from '../../../../test.module';
import { CompleteOrderUpdateComponent } from 'app/entities/orders/complete-order/complete-order-update.component';
import { CompleteOrderService } from 'app/entities/orders/complete-order/complete-order.service';
import { CompleteOrder } from 'app/shared/model/orders/complete-order.model';

describe('Component Tests', () => {
    describe('CompleteOrder Management Update Component', () => {
        let comp: CompleteOrderUpdateComponent;
        let fixture: ComponentFixture<CompleteOrderUpdateComponent>;
        let service: CompleteOrderService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ShopTestModule],
                declarations: [CompleteOrderUpdateComponent]
            })
                .overrideTemplate(CompleteOrderUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CompleteOrderUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompleteOrderService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CompleteOrder(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.completeOrder = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CompleteOrder();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.completeOrder = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
