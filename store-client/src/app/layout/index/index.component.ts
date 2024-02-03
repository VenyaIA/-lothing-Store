import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/Product";
import {Customer} from "../../models/Customer";
import {ProductService} from "../../services/product.service";
import {CustomerService} from "../../services/customer.service";
import {ReviewService} from "../../services/review.service";
import {NotificationService} from "../../services/notification.service";
import {ImageProductService} from "../../services/image-product.service";
import {CartService} from "../../services/cart.service";
import {Brand} from "../../models/Brand";
import {Category} from "../../models/Category";
import {BrandService} from "../../services/brand.service";
import {CategoryService} from "../../services/category.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  isProductLoaded = false;
  products!: Product[];
  isCustomerDataLoaded = false;
  customer!: Customer;
  path = 'http://localhost:4200/assets/images/products/';
  searchProduct = '';
  brands: any[] = [];
  categories: any[] = [];
  selectedBrands: number[] = [];
  selectedCategories: number[] = [];

  constructor(
    private productService: ProductService,
    private customerService: CustomerService,
    private reviewService: ReviewService,
    private brandService: BrandService,
    private categoryService: CategoryService,
    private notification: NotificationService,
    private imageProductService: ImageProductService,
    private cartService: CartService,
  ) {
  }

  ngOnInit(): void {
    this.brandService.getAllBrands()
      .subscribe(data => {
        let brandsData: Brand[] = data;
        brandsData.forEach(brandData => {
          this.brands.push({
            id: brandData.id,
            title: brandData.title,
            checked: false
          })
        })
      });
    this.categoryService.getAllCategories()
      .subscribe(data => {
        let categoriesData: Category[] = data;
        categoriesData.forEach(categoryData => {
          this.categories.push({
            id: categoryData.id,
            name: categoryData.name,
            checked: false
          })
        })
      });
    this.productService.getAllProducts()
      .subscribe(data => {
        console.log(data);
        this.products = data;
        this.getImagesToProducts(this.products);
        this.getReviewsToProducts(this.products);
        this.isProductLoaded = true;
      });

    this.customerService.getCurrentCustomer()
      .subscribe(data => {
        console.log(data);
        this.customer = data;
        this.isCustomerDataLoaded = true;
      });
  }


  getTotalCost(product: Product): number {
    let totalCost = parseInt(product.price);
    product.promotions?.forEach(p => {
      totalCost = (totalCost * p.discountPercent) / 100;
    });

    return parseInt(product.price) - totalCost;
  }

  getImagesToProducts(products: Product[]): void {
    products.forEach(product => {
      console.log(product.id);
      this.imageProductService.getAllImageProductById(product.id!)
        .subscribe(data => {
          console.log(data);
          for (const image of data) {
            image.url = this.path + image.url
          }
          product.imageProducts = data;
          product.imgCollection = [];
          product.imageProducts!.forEach(img => {
            product.imgCollection?.push({
              image: img.url,
              thumbImage: img.url,
              alt: 'alt of image',
              // title: 'title of image'
            })
          })
          console.log(product.imgCollection)
        })
    });
  }

  getReviewsToProducts(products: Product[]): void {
    products.forEach(p => {
      this.reviewService.getAllReviewForProduct(p.id!)
        .subscribe(date => {
          p.reviews = date;
        })
    })
  }

  addProductToCart(productId: number): void {
    this.cartService.addProductByIdInCurrentCart(productId)
      .subscribe(data => {
        console.log(data);
      });
  }

  applyFilters() {
    // Собираем выбранные бренды
    const selectedBrandIds = this.brands.filter(brand => brand.checked).map(brand => brand.id);
    // Обновляем свойство, используемое для фильтрации
    this.selectedBrands = selectedBrandIds;

    // Собираем выбранные категории
    const selectedCategories = this.categories.filter(category => category.checked).map(category => category.id);
    this.selectedCategories = selectedCategories;
  }

  protected readonly toString = toString;
}
