import {Product} from "./Product";

export interface Color {
  id?: number;
  name: string;
  hexCode: string;
  products?: Product[];
}
