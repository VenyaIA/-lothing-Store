import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Brand} from "../../../models/Brand";
import {BrandService} from "../../../services/brand.service";
import {NotificationService} from "../../../services/notification.service";
import {Router} from "@angular/router";
import {CategoryService} from "../../../services/category.service";
import {Category} from "../../../models/Category";

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css']
})
export class AddCategoryComponent implements OnInit {
  public addCategoryForm!: FormGroup;
  isCategoryCreated = false;
  createdCategory!: Category;

  constructor(private fb: FormBuilder,
              private categoryService: CategoryService,
              private notification: NotificationService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.addCategoryForm = this.createAddBrand();
  }

  private createAddBrand(): FormGroup {
    return this.fb.group({
      name: ['', Validators.compose([Validators.required])],
      description: ['', Validators.compose([Validators.required])]
    });
  }


  submit(): void {
    console.log(this.addCategoryForm.value);

    this.categoryService.createCategory({
      name: this.addCategoryForm.value.name,
      description: this.addCategoryForm.value.description,
    }).subscribe(data => {
      this.isCategoryCreated = true;
      this.createdCategory = data;
      console.log(data);
      this.notification.showSnackBar("Category was created");
      this.router.navigate(['/profile']);
    });
  }
}
