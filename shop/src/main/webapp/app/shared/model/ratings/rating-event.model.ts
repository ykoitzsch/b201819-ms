import { IRating } from 'app/shared/model/ratings/rating.model';

export class RatingEvent {
    constructor(public rating: IRating, public event: String) {}
}
