import {Category} from "./Category";
import {Brand} from "./Brand";
import {Cart} from "./Cart";
import {Color} from "./Color";
import {ImageProduct} from "./ImageProduct";
import {OrderProduct} from "./OrderProduct";
import {Promotion} from "./Promotion";
import {Review} from "./Review";
import {Size} from "./Size";

export interface Product {
  id?: number;
  title: string;
  description: string;
  price: string;
  likes?: number;
  likesUsers?: string[];
  category?: Category;
  brand?: Brand;
  carts?: Cart[];
  colors?: Color[];
  imageProducts?: ImageProduct[];
  orderProducts?: OrderProduct[];
  promotions?: Promotion[];
  reviews?: Review[];
  sizes?: Size[];
  imgCollection?: Array<object>;
}
