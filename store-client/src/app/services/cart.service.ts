import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const CART_API = 'http://localhost:8080/api/cart/';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http: HttpClient) {
  }

  getCurrenCart(): Observable<any> {
    return this.http.get(CART_API + 'current');
  }

  clearCart(): Observable<any> {
    return this.http.patch(CART_API + 'clearCurrent', null);
  }

  addProductByIdInCurrentCart(productId: number): Observable<any> {
    return this.http.patch(CART_API + 'current/' + productId + '/add', null);
  }

  deleteProductByIdFromCurrentCart(productId: number): Observable<any> {
    return this.http.patch(CART_API + 'current/' + productId + '/delete', null);
  }
}
