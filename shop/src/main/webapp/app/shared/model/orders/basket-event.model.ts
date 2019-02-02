import { IBasket } from './basket.model';

export class BasketEvent {
    constructor(public basket: IBasket, public event: String) {}
}
