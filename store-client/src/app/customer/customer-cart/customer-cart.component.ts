import {Component, OnInit} from '@angular/core';
import {Cart} from "../../models/Cart";
import {Product} from "../../models/Product";
import {ProductService} from "../../services/product.service";
import {CartService} from "../../services/cart.service";
import {ReviewService} from "../../services/review.service";
import {ImageProductService} from "../../services/image-product.service";
import {NotificationService} from "../../services/notification.service";
import {OrderProductService} from "../../services/order-product.service";
import {OrderProduct} from "../../models/OrderProduct";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customer-cart',
  templateUrl: './customer-cart.component.html',
  styleUrls: ['./customer-cart.component.css']
})
export class CustomerCartComponent implements OnInit {

  isCartLoaded = false;
  isProductLoaded = false;
  cart!: Cart;
  products!: Product [];
  orderProduct!: OrderProduct;
  isOrderProductLoaded = false;
  path = 'http://localhost:4200/assets/images/products/';

  constructor(private productService: ProductService,
              private cartService: CartService,
              private reviewService: ReviewService,
              private imageProductService: ImageProductService,
              private notification: NotificationService,
              private orderService: OrderProductService,
              private router: Router) {

  }

  ngOnInit(): void {
    this.cartService.getCurrenCart()
      .subscribe(data => {
        console.log(data);
        this.cart = data;
        this.isCartLoaded = true;
      })

    this.productService.getAllProductsByCustomerCart()
      .subscribe(data => {
        console.log(data);
        this.products = data;
        this.getImagesToProducts(this.products);
        this.getReviewsToProducts(this.products);
        this.isProductLoaded = true;
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
    products.forEach(p => {
      console.log(p.id);
      this.imageProductService.getAllImageProductById(p.id!)
        .subscribe(data => {
          console.log(data);
          for (const image of data) {
            image.url = this.path + image.url
          }
          p.imageProducts = data;
          p.imgCollection = [];
          p.imageProducts!.forEach(img => {
            p.imgCollection?.push({
              image: img.url,
              thumbImage: img.url,
              alt: 'alt of image',
              // title: 'title of image'
            })
          })
          console.log(p.imgCollection)
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

  deleteProductFromCart(productId: number, productIndex: number): void {
    const product: Product = this.products[productIndex];
    console.log(product);

    this.cartService.deleteProductByIdFromCurrentCart(productId)
      .subscribe(() => {
        this.products.splice(productIndex, 1);
        this.notification.showSnackBar("Product was deleted");
      })
  }

  clearCart(): void {
    this.cartService.clearCart()
      .subscribe(() => {
        this.products = [];
        this.notification.showSnackBar("Cart was cleaned");
      })
  }

  addToOrder(productId: number) {
    this.orderService.createOrderProduct({
      status: "not paid"
    }).subscribe(data => {
      this.orderProduct = data
      this.orderService.addProductByIdInOrderProductById(this.orderProduct.id!, productId)
        .subscribe(data => {
          console.log(data);
          this.router.navigate([`current/order/${this.orderProduct.id!}`]);
        })
    });
  }
}
