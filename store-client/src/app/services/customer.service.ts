import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const CUSTOMER_API = 'http://localhost:8080/api/customer/';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient) { }

  getCurrentCustomer(): Observable<any> {
    return this.http.get(CUSTOMER_API);
  }

  getCustomerProfile(customerId: number):Observable<any> {
    return this.http.get(CUSTOMER_API + customerId);
  }

  updateCustomer(customer: any): Observable<any> {
    return this.http.patch(CUSTOMER_API + 'update', customer);
  }
}
