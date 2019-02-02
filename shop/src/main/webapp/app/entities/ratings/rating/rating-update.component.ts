import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IRating } from 'app/shared/model/ratings/rating.model';
import { RatingService } from './rating.service';
import { RatingEvent } from '../../../shared/model/ratings/rating-event.model';

@Component({
    selector: 'jhi-rating-update',
    templateUrl: './rating-update.component.html'
})
export class RatingUpdateComponent implements OnInit {
    rating: IRating;
    isSaving: boolean;

    constructor(private ratingService: RatingService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rating }) => {
            this.rating = rating;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rating.id !== undefined) {
            this.subscribeToSaveResponse(this.ratingService.createEvent(new RatingEvent(this.rating, 'RATING_UPDATED')));
        } else {
            this.rating.id = this.randomInt();
            this.subscribeToSaveResponse(this.ratingService.createEvent(new RatingEvent(this.rating, 'RATING_CREATED')));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private randomInt() {
        return Math.floor(Math.random() * (10000 - 1 + 1)) + 1;
    }
}
