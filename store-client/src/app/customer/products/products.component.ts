import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/Product";
import {Customer} from "../../models/Customer";
import {ProductService} from "../../services/product.service";
import {CustomerService} from "../../services/customer.service";
import {ReviewService} from "../../services/review.service";
import {NotificationService} from "../../services/notification.service";
import {ImageProductService} from "../../services/image-product.service";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {EditProductComponent} from "../edit-product/edit-product.component";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  isProductLoaded = false;
  products!: Product[];
  isCustomerDataLoaded = false;

  customer!: Customer;
  path = 'http://localhost:4200/assets/images/products/';

  constructor(private dialog: MatDialog,
              private productService: ProductService,
              private customerService: CustomerService,
              private reviewService: ReviewService,
              private notification: NotificationService,
              private imageProductService: ImageProductService
  ) {
  }

  ngOnInit(): void {
    this.productService.getAllProducts()
      .subscribe(data => {
        this.products = data;
        this.getImagesToProducts(this.products);
        this.getReviewsToProducts(this.products);
        this.isProductLoaded = true;
      });

    this.customerService.getCurrentCustomer()
      .subscribe(data => {
        this.customer = data;
        this.isCustomerDataLoaded = true;
      });
  }

  openEditDialog(product: Product): void {
    const dialogCustomerEditConfig = new MatDialogConfig();
    dialogCustomerEditConfig.width = '500px';
    dialogCustomerEditConfig.data = {
      product: product
    };
    this.dialog.open(EditProductComponent, dialogCustomerEditConfig);
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

  postReview(message: string, productId: number, productIndex: number): void {
    const product = this.products[productIndex];

    console.log(product);
    console.log(message);
    this.reviewService.createReview(message, productId)
      .subscribe(data => {
        console.log(data);
        product.reviews?.push(data);
      });
  }

  deleteProduct(productId: number, productIndex: number): void {
    this.productService.deleteProductById(productId)
      .subscribe(() => {
        this.products.splice(productIndex, 1);
        this.notification.showSnackBar("Product was deleted");
      })
  }

  deleteReview(reviewId: number, productIndex: number, reviewIndex: number): void {
    const product = this.products[productIndex];

    this.reviewService.deleteReviewById(reviewId)
      .subscribe(() => {
        product.reviews?.splice(reviewIndex, 1);
        this.notification.showSnackBar("Comment was deleted");
      })
  }

}
