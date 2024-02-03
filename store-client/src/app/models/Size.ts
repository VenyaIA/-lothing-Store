import {Product} from "./Product";

export interface Size {
  id?: number;
  title: string;
  products?: Product[];
}
