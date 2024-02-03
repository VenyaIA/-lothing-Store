import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product} from "../models/Product";

const PRODUCT_API = 'http://localhost:8080/api/product/';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) {
  }

  createProduct(product: any): Observable<any> {
    return this.http.post(PRODUCT_API + 'create', product);
  }

  getAllProducts(): Observable<any> {
    return this.http.get(PRODUCT_API + 'all');
  }

  getProductById(productId: number): Observable<any> {
    return this.http.get(PRODUCT_API + productId);
  }

  deleteProductById(productId: number): Observable<any> {
    return this.http.delete(PRODUCT_API + productId + '/delete');
  }

  updateProduct(product: Product): Observable<any> {
    return this.http.patch(PRODUCT_API + 'update', product);
  }

  getAllProductByBrand(brandId: number): Observable<any> {
    return this.http.get(PRODUCT_API + brandId + '/productsByBrand');
  }

  getAllProductsByCategory(categoryId: number): Observable<any> {
    return this.http.get(PRODUCT_API + categoryId + '/productsByCategory');
  }

  getAllProductsByCustomerCart(): Observable<any> {
    return this.http.get(PRODUCT_API + 'customerCart/products');
  }

  getAllProductsForOrderProductById(orderProductId: number):Observable<any> {
    return this.http.get(PRODUCT_API + orderProductId + '/products');
  }
}
