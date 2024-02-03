import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {OrderProduct} from "../../models/OrderProduct";
import {Payment} from "../../models/Payment";
import {OrderProductService} from "../../services/order-product.service";
import {PaymentService} from "../../services/payment.service";
import {NotificationService} from "../../services/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Product} from "../../models/Product";
import {ProductService} from "../../services/product.service";

@Component({
  selector: 'app-current-order',
  templateUrl: './current-order.component.html',
  styleUrls: ['./current-order.component.css']
})
export class CurrentOrderComponent implements OnInit {

  paymentForm!: FormGroup;
  isOrderProductLoaded = false;
  isPaymentLoaded = false;
  orderProduct!: OrderProduct;
  payment!: Payment
  orderId!: number;
  totalAmountOrder = 0;
  productsOrder!: Product[];
  isProductsOrderLoaded = false;

  constructor(private orderService: OrderProductService,
              private paymentService: PaymentService,
              private productService: ProductService,
              private fb: FormBuilder,
              private notification: NotificationService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.orderId = params['id'];
      console.log(this.orderId);
    })
    this.orderService.getOrderProductByIdForCustomer(this.orderId)
      .subscribe(data => {
          console.log(data);
          this.orderProduct = data;

          this.getProductsOrder(this.orderId);
          this.isOrderProductLoaded = true;
        }
      );
    this.paymentForm = this.createPaymentForm();
  }

  getProductsOrder(orderId: number): void {
    this.productService.getAllProductsForOrderProductById(orderId)
      .subscribe(data => {
        console.log(data);
        this.productsOrder = data;
        console.log(this.productsOrder)
        this.productsOrder.forEach(p => {
          console.log(p.price);
          this.totalAmountOrder += parseInt(p.price);
          console.log(this.totalAmountOrder);
        });
        this.isProductsOrderLoaded = true;
      });
  }

  private createPaymentForm(): FormGroup {
    return this.fb.group({
      paymentType: ['', Validators.compose([Validators.required])],
      status: ['', Validators.compose([Validators.required])],
    });
  }

  submit(): void {
    this.paymentService.createPaymentForOrderProductById({
      paymentType: this.paymentForm.value.paymentType,
      status: this.paymentForm.value.status
    }, this.orderId).subscribe(data => {
      console.log(data);
      this.payment = data;
      this.paymentService.updateStatusPaymentForOrderProductById(this.orderId)
        .subscribe(data => {
          console.log(data);
          this.notification.showSnackBar("Order was successfully placed");
          this.router.navigate(['/profile/orders']);
        });
    })
  }
}
