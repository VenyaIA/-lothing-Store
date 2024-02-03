import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const IMAGE_PRODUCT_API = 'http://localhost:8080/api/imageProduct/';

@Injectable({
  providedIn: 'root'
})
export class ImageProductService {

  constructor(private http: HttpClient) {
  }

  uploadImageProduct(url: string, productId: number): Observable<any> {
    return this.http.post(IMAGE_PRODUCT_API + productId + '/upload', {
      url: url
    });
  }

  getAllImageProductById(productId: number): Observable<any> {
    return this.http.get(IMAGE_PRODUCT_API + productId + '/images');
  }

  getImageProductByIdInProductById(productId: number, imageId: number): Observable<any> {
    return this.http.get(IMAGE_PRODUCT_API + productId + '/' + imageId);
  }

  deleteImageByIdFromProductById(productId: number, imageId: number): Observable<any> {
    return this.http.delete(IMAGE_PRODUCT_API + productId + '/' + imageId + '/delete');
  }
}
