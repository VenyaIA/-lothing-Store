import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/Product";
import {Customer} from "../../models/Customer";
import {ProductService} from "../../services/product.service";
import {CustomerService} from "../../services/customer.service";
import {ReviewService} from "../../services/review.service";
import {NotificationService} from "../../services/notification.service";
import {ImageProductService} from "../../services/image-product.service";
import {CartService} from "../../services/cart.service";
import {ActivatedRoute} from "@angular/router";
@Component({
  selector: 'app-product-view',
  templateUrl: './product-view.component.html',
  styleUrls: ['./product-view.component.css']
})
export class ProductViewComponent implements OnInit {

  isProductLoaded = false;
  product!: Product;
  productId!: number;
  isCustomerDataLoaded = false;

  customer!: Customer;
  path = 'http://localhost:4200/assets/images/products/';

  constructor(
    private productService: ProductService,
    private customerService: CustomerService,
    private reviewService: ReviewService,
    private notification: NotificationService,
    private imageProductService: ImageProductService,
    private cartService: CartService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.productId = params['id'];
      console.log(this.productId);
    })

    this.productService.getProductById(this.productId)
      .subscribe(data => {
        console.log(data);
        this.product = data;
        this.getImagesToProduct(this.product);
        this.getReviewsToProduct(this.product);
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

  getImagesToProduct(product: Product): void {
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
  }

  getReviewsToProduct(product: Product): void {
    this.reviewService.getAllReviewForProduct(product.id!)
      .subscribe(date => {
        product.reviews = date;
      });
  }

  postReview(message: string): void {
    console.log(message);
    this.reviewService.createReview(message, this.product.id!)
      .subscribe(data => {
        console.log(data);
        this.product.reviews?.push(data);
      });
  }

  addProductToCart(): void {
    this.cartService.addProductByIdInCurrentCart(this.product.id!)
      .subscribe(data => {
        console.log(data);
      });
  }

}
