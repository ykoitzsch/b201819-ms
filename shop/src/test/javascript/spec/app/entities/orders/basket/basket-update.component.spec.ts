/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ShopTestModule } from '../../../../test.module';
import { BasketUpdateComponent } from 'app/entities/orders/basket/basket-update.component';
import { BasketService } from 'app/entities/orders/basket/basket.service';
import { Basket } from 'app/shared/model/orders/basket.model';

describe('Component Tests', () => {
    describe('Basket Management Update Component', () => {
        let comp: BasketUpdateComponent;
        let fixture: ComponentFixture<BasketUpdateComponent>;
        let service: BasketService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ShopTestModule],
                declarations: [BasketUpdateComponent]
            })
                .overrideTemplate(BasketUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BasketUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BasketService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Basket(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.basket = entity;
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
                    const entity = new Basket();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.basket = entity;
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
