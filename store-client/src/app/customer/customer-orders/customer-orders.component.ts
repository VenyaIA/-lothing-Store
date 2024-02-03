import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/Product";
import {Customer} from "../../models/Customer";
import {ProductService} from "../../services/product.service";
import {CustomerService} from "../../services/customer.service";
import {ReviewService} from "../../services/review.service";
import {NotificationService} from "../../services/notification.service";
import {ImageProductService} from "../../services/image-product.service";
import {OrderProduct} from "../../models/OrderProduct";
import {OrderProductService} from "../../services/order-product.service";

@Component({
  selector: 'app-customer-orders',
  templateUrl: './customer-orders.component.html',
  styleUrls: ['./customer-orders.component.css']
})
export class CustomerOrdersComponent implements OnInit{

  isCustomerDataLoaded = false;
  orders!: OrderProduct[];
  isOrdersLoaded = false;


  customer!: Customer;
  path = 'http://localhost:4200/assets/images/products/';

  constructor(
    private productService: ProductService,
    private customerService: CustomerService,
    private reviewService: ReviewService,
    private orderService: OrderProductService,
    private notification: NotificationService,
    private imageProductService: ImageProductService
  ) {
  }

  ngOnInit(): void {
    this.orderService.getAllOrderProductByCurrentCustomer()
      .subscribe(data => {
        console.log(data);
        this.orders = data;
        this.orders?.forEach(order => {
          this.productService.getAllProductsForOrderProductById(order.id!)
            .subscribe(data => {
              console.log(data);
              order.products = data;
              this.getImagesToProducts(order.products!);
              this.getReviewsToProducts(order.products!);
              this.isOrdersLoaded = true;
            });
        });

      });

    this.customerService.getCurrentCustomer()
      .subscribe(data => {
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

  postReview(message: string, orderIndex: number, productId: number, productIndex: number): void {
    const product = this.orders[orderIndex].products![productIndex];

    console.log(product);
    console.log(message);
    this.reviewService.createReview(message, productId)
      .subscribe(data => {
        console.log(data);
        product.reviews?.push(data);
      });
  }

  deleteProductFromOrder(orderId: number, orderIndex: number, productId: number, productIndex: number): void {
    this.orderService.deleteProductByIdFromOrderProductById(orderId, productId)
      .subscribe(() => {
        this.orders[orderIndex].products!.splice(productIndex, 1);
        this.notification.showSnackBar("Product was deleted from Order");
      })
  }

  deleteOrder(orderId: number, orderIndex: number): void {
    this.orderService.deleteOrderProductById(orderId)
      .subscribe(() => {
        this.orders.splice(orderIndex, 1);
        this.notification.showSnackBar("Order was deleted");
      })
  }
}
