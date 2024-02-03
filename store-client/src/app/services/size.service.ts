import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const SIZE_API = 'http://localhost:8080/api/size/';

@Injectable({
  providedIn: 'root'
})
export class SizeService {

  constructor(private http: HttpClient) {
  }

  createSize(size: any): Observable<any> {
    return this.http.post(SIZE_API + 'create', size);
  }

  getAllSizes(): Observable<any> {
    return this.http.get(SIZE_API + 'all');
  }

  productByIdSizes(productId: number): Observable<any> {
    return this.http.get(SIZE_API + productId + '/sizes');
  }

  addProductByIdInSize(sizeId: number, productId: number): Observable<any> {
    return this.http.patch(SIZE_API + sizeId + '/' + productId + '/add', null);
  }

  deleteProductByIdFromSizeById(sizeId: number, productId: number): Observable<any> {
    return this.http.patch(SIZE_API + sizeId + '/' + productId + '/delete', null);
  }

  deleteSizeById(sizeId: number): Observable<any> {
    return this.http.delete(SIZE_API + sizeId + '/delete');
  }

}
