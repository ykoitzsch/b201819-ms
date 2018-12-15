export interface IRating {
    id?: number;
    points?: number;
    productId?: number;
    customerId?: number;
    description?: string;
}

export class Rating implements IRating {
    constructor(
        public id?: number,
        public points?: number,
        public productId?: number,
        public customerId?: number,
        public description?: string
    ) {}
}
