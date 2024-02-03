import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Brand} from "../../../models/Brand";
import {BrandService} from "../../../services/brand.service";
import {NotificationService} from "../../../services/notification.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-brand',
  templateUrl: './add-brand.component.html',
  styleUrls: ['./add-brand.component.css']
})
export class AddBrandComponent implements OnInit {
  public addBrandForm!: FormGroup;
  isBrandCreated = false;
  createdBrand!: Brand;

  constructor(private fb: FormBuilder,
              private brandService: BrandService,
              private notification: NotificationService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.addBrandForm = this.createAddBrand();
  }

  private createAddBrand(): FormGroup {
    return this.fb.group({
      title: ['', Validators.compose([Validators.required])],
      description: ['', Validators.compose([Validators.required])]
    });
  }


  submit(): void {
    console.log(this.addBrandForm.value);

    this.brandService.createBrand({
      title: this.addBrandForm.value.title,
      description: this.addBrandForm.value.description,
    }).subscribe(data => {
      this.isBrandCreated = true;
      this.createdBrand = data;
      console.log(data);
      this.notification.showSnackBar("Brand was created");
      this.router.navigate(['/profile']);
    });
  }

}
