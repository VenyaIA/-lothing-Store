import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NotificationService} from "../../services/notification.service";
import {ProductService} from "../../services/product.service";
import {Product} from "../../models/Product";
import {BrandService} from "../../services/brand.service";
import {CategoryService} from "../../services/category.service";
import {ColorService} from "../../services/color.service";
import {PromotionService} from "../../services/promotion.service";
import {SizeService} from "../../services/size.service";
import {Brand} from "../../models/Brand";
import {Category} from "../../models/Category";
import {Color} from "../../models/Color";
import {Promotion} from "../../models/Promotion";
import {Size} from "../../models/Size";

@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['./edit-product.component.css']
})
export class EditProductComponent implements OnInit{

  public productForm!: FormGroup;
  brands!: Brand[];
  categories!: Category[];
  colorList: Color[] = [];
  promotionList!: Promotion[];
  sizeList!: Size[];
  colors = new FormControl('');
  promotions = new FormControl('');
  sizes = new FormControl('');
  isProductDataLoaded = false;

  constructor(private dialogRef: MatDialogRef<EditProductComponent>,
              private fb: FormBuilder,
              private notification: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private productService: ProductService,
              private brandService: BrandService,
              private categoryService: CategoryService,
              private colorService: ColorService,
              private promotionService: PromotionService,
              private sizeService: SizeService
              ) {
  }

  ngOnInit(): void {
    this.brandService.getAllBrands()
      .subscribe(data => {
        this.brands = data;
        this.brands.forEach(brand => console.log(brand.title))
      });
    this.categoryService.getAllCategories()
      .subscribe(data => {
        this.categories = data
      });
    this.colorService.getAllColors()
      .subscribe(data => {
        this.colorList = data;
        console.log(this.colorList);
      });
    this.promotionService.getAllPromotions()
      .subscribe(data => {
        this.promotionList = data;
        console.log(this.promotionList);
      });
    this.sizeService.getAllSizes()
      .subscribe(data => {
        this.sizeList = data;
        console.log(this.sizeList);

        this.isProductDataLoaded = true;

      });
    this.productForm = this.createProfileForm();
  }

  private createProfileForm(): FormGroup {
    return this.fb.group({
      title: [
        this.data.product.title,
        Validators.compose([Validators.required])
      ],
      description: [
        this.data.product.description,
        Validators.compose([Validators.required])
      ],
      price: [
        this.data.product.price,
        Validators.compose([Validators.required])
      ],
      brand: new FormControl(this.data.product.brand.title),
      category: [
        this.data.product.category,
        Validators.compose([Validators.required])
      ],
      colors: [
        this.data.product.colors,
        Validators.compose([Validators.required])
      ],
      promotions: [
        this.data.product.promotions
      ],
      sizes: [
        this.data.product.sizes,
        Validators.compose([Validators.required])
      ]
    });
  }

  submit(): void {
    console.log(this.updateProduct())

    this.productService.updateProduct(this.updateProduct())
      .subscribe(() => {
        this.notification.showSnackBar("Product updated successfully");
        this.dialogRef.close();
      });
  }

  private updateProduct(): Product {
    this.data.product.title = this.productForm.value.title;
    this.data.product.description = this.productForm.value.description;
    this.data.product.price = this.productForm.value.price;
    this.data.product.brand = this.productForm.value.brand;
    this.data.product.category = this.productForm.value.category;
    this.data.product.colors = this.colorList.filter(color => {
      return this.colors.value?.includes(color.name);
    });
    this.data.product.promotions = this.promotionList.filter(promotion => {
      return this.promotions.value?.includes(promotion.title);
    });
    this.data.product.sizes = this.sizeList.filter(size => {
      return this.sizes.value?.includes(size.title);
    });
    return this.data.product;
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
