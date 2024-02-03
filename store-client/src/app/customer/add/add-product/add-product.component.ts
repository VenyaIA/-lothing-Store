import {Component, OnInit} from '@angular/core';
import {BrandService} from "../../../services/brand.service";
import {CategoryService} from "../../../services/category.service";
import {ColorService} from "../../../services/color.service";
import {ImageProductService} from "../../../services/image-product.service";
import {SizeService} from "../../../services/size.service";
import {ProductService} from "../../../services/product.service";
import {NotificationService} from "../../../services/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Product} from "../../../models/Product";
import {Router} from "@angular/router";
import {Brand} from "../../../models/Brand";
import {Category} from "../../../models/Category";
import {Color} from "../../../models/Color";
import {Size} from "../../../models/Size";
import {PromotionService} from "../../../services/promotion.service";
import {Promotion} from "../../../models/Promotion";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {

  public addProductForm!: FormGroup;
  isProductCreated = false;
  createdProduct!: Product;
  brands!: Brand[];
  categories!: Category[];
  colors!: Color[];
  promotions!: Promotion[];
  sizes!: Size[];
  selectedFiles: string[] = [];
  previewImgURLs: any = [];
  path = 'http://localhost:4200/assets/images/profiles/';

  constructor(private fb: FormBuilder,
              private brandService: BrandService,
              private categoryService: CategoryService,
              private colorService: ColorService,
              private imageProductService: ImageProductService,
              private productService: ProductService,
              private promotionService: PromotionService,
              private sizeService: SizeService,
              private notification: NotificationService,
              private router: Router
  ) {
  }

  ngOnInit(): void {
    this.brandService.getAllBrands()
      .subscribe(data => {
        this.brands = data;
      });
    this.categoryService.getAllCategories()
      .subscribe(data => {
        this.categories = data
      });
    this.colorService.getAllColors()
      .subscribe(data => {
        this.colors = data;
      });
    this.promotionService.getAllPromotions()
      .subscribe(data => {
        this.promotions = data;
      });
    this.sizeService.getAllSizes()
      .subscribe(data => {
        this.sizes = data;
      });
    this.addProductForm = this.createAddProduct();
  }

  private createAddProduct(): FormGroup {
    return this.fb.group({
      title: ['', Validators.compose([Validators.required])],
      description: ['', Validators.compose([Validators.required])],
      price: ['', Validators.compose([Validators.required])],
      brand: ['', Validators.compose([Validators.required])],
      category: ['', Validators.compose([Validators.required])],
      color: ['', Validators.compose([Validators.required])],
      promotion: [''],
      size: ['', Validators.compose([Validators.required])]
    });
  }

  submit(): void {
    console.log(this.addProductForm.value);

    this.productService.createProduct({
      title: this.addProductForm.value.title,
      description: this.addProductForm.value.description,
      price: this.addProductForm.value.price
    }).subscribe(data => {
      this.createdProduct = data;

      if (this.createdProduct.id != null) {
        console.log(this.addProductForm.value.brand);
        this.addBrand(this.addProductForm.value.brand, this.createdProduct.id);
        this.addColor(this.addProductForm.value.color, this.createdProduct.id);
        this.addSize(this.addProductForm.value.size, this.createdProduct.id);
        if (this.addProductForm.value.promotion !== '') {
          this.addPromotion(this.addProductForm.value.promotion, this.createdProduct.id);
        }
        this.addCategory(this.addProductForm.value.category, this.createdProduct.id);
        this.onUpload(this.createdProduct.id);
      }
    });
  }

  getFileNameFromPath(filePath: string): string {
    return filePath.split('\\').pop() || ''; // Для Windows
  }

  onFieldSelected(event: any): void {
    console.log(event)
    if (event.target.files && event.target.files[0]) {
      let filesAmount = event.target.files.length;
      for (let i = 0; i < filesAmount; i++) {
        console.log(event.target.files[i].name)
        this.selectedFiles?.push(this.getFileNameFromPath(event.target.files[i].name));
        let reader = new FileReader();
        reader.readAsDataURL(event.target.files[i]);
        reader.onload = (event:any) => {


          console.log(event.target.result);
          this.previewImgURLs.push(event.target.result);
          console.log(this.previewImgURLs);

        }
      }
    }
  }

  onUpload(productId: number): void {
    if (this.selectedFiles != null) {
      this.selectedFiles.forEach(sf => {
        this.imageProductService.uploadImageProduct(sf, productId)
          .subscribe(() => {
            this.isProductCreated = true;
            this.notification.showSnackBar("Product was created successfully");
            this.router.navigate(['/profile']);
          });
      })

    }
  }

  addBrand(brandId: number, productId: number): void {
    this.brandService.addProductByIdInBrand(brandId, productId)
      .subscribe(() => {
        console.log("Product added to brand");
      });
  }

  addCategory(categoryId: number, productId: number): void {
    this.categoryService.addProductByIdInCategoryById(categoryId, productId)
      .subscribe(() => {
        console.log("Product added to category");
      });
  }

  addColor(colorId: number, productId: number): void {
    this.colorService.addProductByIdInColor(colorId, productId)
      .subscribe(() => {
        console.log("Product added to color");
      });
  }

  addSize(sizeId: number, productId: number): void {
    this.sizeService.addProductByIdInSize(sizeId, productId)
      .subscribe(() => {
        console.log("Product added to size");
      });
  }

  private addPromotion(promotionId: number, productId: number): void {
    this.promotionService.addProductByIdInPromotion(promotionId, productId)
      .subscribe(() => {
        console.log("Product added to promotion")
      })
  }
}
