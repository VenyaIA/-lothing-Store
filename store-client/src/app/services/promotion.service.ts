import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const PROMOTION_API = 'http://localhost:8080/api/promotion/';

@Injectable({
  providedIn: 'root'
})
export class PromotionService {

  constructor(private http: HttpClient) { }

  createPromotion(promotion: any): Observable<any> {
    return this.http.post(PROMOTION_API + 'create', promotion);
  }

  getAllPromotions(): Observable<any> {
    return this.http.get(PROMOTION_API + 'all');
  }

  productByIdPromotions(productId: number): Observable<any> {
    return this.http.get(PROMOTION_API + productId + '/promotions');
  }

  addProductByIdInPromotion(promotionId: number, productId: number) {
    return this.http.patch(PROMOTION_API + promotionId + '/' + productId + '/add', null);
  }

  deleteProductByIdFromPromotionById(promotionId: number, productId: number) {
    return this.http.patch(PROMOTION_API + promotionId + '/' + productId + '/delete', null);
  }

  deletePromotionById(promotionId: number) {
    return this.http.delete(PROMOTION_API + promotionId + '/delete');
  }
}
