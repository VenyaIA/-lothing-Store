import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const CATEGORY_API = 'http://localhost:8080/api/category/';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) { }

  createCategory(category: any): Observable<any> {
    return this.http.post(CATEGORY_API + 'create', category);
  }

  getAllCategories(): Observable<any> {
    return this.http.get(CATEGORY_API + 'all');
  }

  getCategoryById(categoryId: number):Observable<any> {
    return this.http.get(CATEGORY_API + categoryId);
  }

  addProductByIdInCategoryById(categoryId: number, productId: number) : Observable<any> {
    return this.http.patch(CATEGORY_API + categoryId + '/' + productId + '/add', null);
  }

  deleteCategory(categoryId: number):Observable<any> {
    return this.http.delete(CATEGORY_API + categoryId + '/delete');
  }
}
