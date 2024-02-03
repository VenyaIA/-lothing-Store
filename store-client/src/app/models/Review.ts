import {Product} from "./Product";

export interface Review {
  id?: number;
  username: string;
  userId: number;
  message: string;
  product?: Product;
}
