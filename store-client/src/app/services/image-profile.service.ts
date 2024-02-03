import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const IMAGE_PROFILE_API = 'http://localhost:8080/api/imageProfile/';

@Injectable({
  providedIn: 'root'
})
export class ImageProfileService {

  constructor(private http: HttpClient) {
  }

  uploadImageProfileCurrentCustomer(file: File): Observable<any> {
    const uploadData = new FormData();
    uploadData.append('file', file);

    return this.http.post(IMAGE_PROFILE_API + 'upload', uploadData);
  }

  getImageProfileCurrentCustomer(): Observable<any> {
    return this.http.get(IMAGE_PROFILE_API + 'image');
  }
}
