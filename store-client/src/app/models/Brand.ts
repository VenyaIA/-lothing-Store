import {Product} from "./Product";

export interface Brand {
  id?: number;
  title: string;
  description: string;
  products?: Product[];
}
