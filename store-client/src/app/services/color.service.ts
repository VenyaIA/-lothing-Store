import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const COLOR_API = 'http://localhost:8080/api/color/';

@Injectable({
  providedIn: 'root'
})
export class ColorService {

  constructor(private http: HttpClient) {
  }

  createColor(color: any): Observable<any> {
    return this.http.post(COLOR_API + 'create', color);
  }

  getAllColors(): Observable<any> {
    return this.http.get(COLOR_API + 'all');
  }

  getColorById(colorId: number): Observable<any> {
    return this.http.get(COLOR_API + colorId);
  }

  productByIdColors(productId: number): Observable<any> {
    return this.http.get(COLOR_API + productId + '/colors');
  }

  addProductByIdInColor(colorId: number, productId: number): Observable<any> {
    return this.http.patch(COLOR_API + colorId + '/' + productId + '/add', null);
  }

  deleteProductByIdFromColorById(colorId: number, productId: number): Observable<any> {
    return this.http.patch(COLOR_API + colorId + '/' + productId + '/delete', null);
  }

  deleteColorById(colorId: number): Observable<any> {
    return this.http.delete(COLOR_API + colorId + '/delete');
  }

}
