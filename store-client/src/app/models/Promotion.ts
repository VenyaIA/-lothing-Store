import {Product} from "./Product";

export interface Promotion {
  id?: number;
  title: string;
  description: string;
  discountPercent: number;
  products?: Product[];
}
