import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {IndexComponent} from "./layout/index/index.component";
import {AuthGuardService} from "./helper/auth-guard.service";
import {CustomerCartComponent} from "./customer/customer-cart/customer-cart.component";
import {ProfileComponent} from "./customer/profile/profile.component";
import {AddProductComponent} from "./customer/add/add-product/add-product.component";
import {ProductsComponent} from "./customer/products/products.component";
import {ProductViewComponent} from "./layout/product-view/product-view.component";
import {AddBrandComponent} from "./customer/add/add-brand/add-brand.component";
import {AddCategoryComponent} from "./customer/add/add-category/add-category.component";
import {AddColorComponent} from "./customer/add/add-color/add-color.component";
import {AddSizeComponent} from "./customer/add/add-size/add-size.component";
import {CustomerOrdersComponent} from "./customer/customer-orders/customer-orders.component";
import {CurrentOrderComponent} from "./customer/current-order/current-order.component";
import {AddPromotionComponent} from "./customer/add/add-promotion/add-promotion.component";
import {IndexNotAuthComponent} from "./layout/index-not-auth/index-not-auth.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'main/not/auth', component: IndexNotAuthComponent},
  {path: 'main', component: IndexComponent, canActivate: [AuthGuardService]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService], children: [
      {path: '', component: ProductsComponent, canActivate: [AuthGuardService]},
      {path: 'orders', component: CustomerOrdersComponent, canActivate: [AuthGuardService]},
      {path: 'product/add', component: AddProductComponent, canActivate: [AuthGuardService]},
      {path: 'brand/add', component: AddBrandComponent, canActivate: [AuthGuardService]},
      {path: 'category/add', component: AddCategoryComponent, canActivate: [AuthGuardService]},
      {path: 'color/add', component: AddColorComponent, canActivate: [AuthGuardService]},
      {path: 'promotion/add', component: AddPromotionComponent, canActivate: [AuthGuardService]},
      {path: 'size/add', component: AddSizeComponent, canActivate: [AuthGuardService]},
    ]},
  {path: 'current/order/:id', component: CurrentOrderComponent, canActivate: [AuthGuardService]},
  {path: 'cart', component: CustomerCartComponent, canActivate: [AuthGuardService]},
  {path: 'product/:id', component: ProductViewComponent, canActivate: [AuthGuardService]},
  {path: '', redirectTo: 'main', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
