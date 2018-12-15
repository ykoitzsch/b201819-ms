/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ShopTestModule } from '../../../../test.module';
import { BasketComponent } from 'app/entities/orders/basket/basket.component';
import { BasketService } from 'app/entities/orders/basket/basket.service';
import { Basket } from 'app/shared/model/orders/basket.model';

describe('Component Tests', () => {
    describe('Basket Management Component', () => {
        let comp: BasketComponent;
        let fixture: ComponentFixture<BasketComponent>;
        let service: BasketService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ShopTestModule],
                declarations: [BasketComponent],
                providers: []
            })
                .overrideTemplate(BasketComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BasketComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BasketService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Basket(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.baskets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
