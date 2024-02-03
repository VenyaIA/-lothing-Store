import {Customer} from "./Customer";
import {OrderProduct} from "./OrderProduct";

export interface Payment {
  id?: number;
  paymentType: string;
  amount?: number;
  status: string;
  customer?: Customer;
  orderProduct?: OrderProduct;
}
