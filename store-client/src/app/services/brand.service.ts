import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const BRAND_API = 'http://localhost:8080/api/brand/';

@Injectable({
  providedIn: 'root'
})
export class BrandService {

  constructor(private http: HttpClient) { }

  createBrand(brand: any): Observable<any> {
    return this.http.post(BRAND_API + 'create', brand);
  }

  getBrandById(id: number): Observable<any> {
    return this.http.get(BRAND_API + id);
  }

  getAllBrands(): Observable<any> {
    return this.http.get(BRAND_API + 'all');
  }

  addProductByIdInBrand(brandId: number, productId: number): Observable<any> {
    return this.http.patch(BRAND_API + brandId + '/' + productId + '/add', null);
  }

  deleteBrandById(branId: number) {
    return this.http.delete(BRAND_API + branId + '/delete');
  }

}
