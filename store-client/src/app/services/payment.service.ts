import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const PAYMENT_API = 'http://localhost:8080/api/payment/';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient) {
  }

  createPaymentForOrderProductById(payment: any, orderProductId: number): Observable<any> {
    return this.http.post(PAYMENT_API + orderProductId + '/create', payment);
  }

  updateStatusPaymentForOrderProductById(orderProductId: number): Observable<any> {
    return this.http.patch(PAYMENT_API + orderProductId + '/update', null);
  }
}


