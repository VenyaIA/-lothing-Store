import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const REVIEW_API = 'http://localhost:8080/api/review/';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient) { }

  createReview(message: string, productId: number): Observable<any> {
    return this.http.post(REVIEW_API + productId + '/create', {
      message: message
    });
  }

  getAllReviewForProduct(productId: number): Observable<any> {
    return this.http.get(REVIEW_API + productId + '/all');
  }

  deleteReviewById(reviewId: number): Observable<any> {
    return this.http.delete(REVIEW_API + reviewId + '/delete');
  }
}
