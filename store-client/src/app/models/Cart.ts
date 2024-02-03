import {Product} from "./Product";

export interface Cart {
  id?: number;
  customer?: string;
  totalPrice: number;
  products?: Product[];
}
