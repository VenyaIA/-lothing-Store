import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const ORDER_API = 'http://localhost:8080/api/order/';

@Injectable({
  providedIn: 'root'
})
export class OrderProductService {

  constructor(private http: HttpClient) {
  }

  createOrderProduct(orderProduct: any): Observable<any> {
    return this.http.post(ORDER_API + 'create', orderProduct);
  }

  getOrderProductByIdForCustomer(orderId: number): Observable<any> {
    return this.http.get(ORDER_API + 'customer/' + orderId);
  }

  getAllOrderProductByCurrentCustomer(): Observable<any> {
    return this.http.get(ORDER_API + 'customer/all');
  }

  addProductByIdInOrderProductById(orderId: number, productId: number): Observable<any> {
    return this.http.patch(ORDER_API + 'customer/' + orderId + '/' + productId + '/add', null);
  }

  deleteProductByIdFromOrderProductById(orderId: number, productId: number): Observable<any> {
    return this.http.patch(ORDER_API + 'customer/' + orderId + '/' + productId + '/delete', null);
  }

  deleteOrderProductById(orderId: number): Observable<any> {
    return this.http.delete(ORDER_API + 'customer/' + orderId + '/delete');
  }
}
