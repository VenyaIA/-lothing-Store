import {Product} from "./Product";
import {Customer} from "./Customer";
import {Payment} from "./Payment";

export interface OrderProduct {
  id?: number;
  status: string;
  customer?: Customer;
  products?: Product[];
  payment?: Payment;
}
