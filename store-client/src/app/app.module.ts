import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MaterialModule} from "./material-module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {authInterceptorProviders} from "./helper/auth-interceptor.service";
import {authErrorInterceptorProvider} from "./helper/error-interceptor.service";
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { NavigationComponent } from './layout/navigation/navigation.component';
import { IndexComponent } from './layout/index/index.component';
import { ProfileComponent } from './customer/profile/profile.component';
import { CustomerCartComponent } from './customer/customer-cart/customer-cart.component';
import { CustomerOrdersComponent } from './customer/customer-orders/customer-orders.component';
import { EditCustomerComponent } from './customer/edit-customer/edit-customer.component';
import { AddProductComponent } from './customer/add/add-product/add-product.component';
import { ProductsComponent } from './customer/products/products.component';
import { ProductViewComponent } from './layout/product-view/product-view.component';
import { AddBrandComponent } from './customer/add/add-brand/add-brand.component';
import { AddCategoryComponent } from './customer/add/add-category/add-category.component';
import { AddColorComponent } from './customer/add/add-color/add-color.component';
import { AddSizeComponent } from './customer/add/add-size/add-size.component';
import { CurrentOrderComponent } from './customer/current-order/current-order.component';
import {NgImageSliderModule} from "ng-image-slider";
import { AddPromotionComponent } from './customer/add/add-promotion/add-promotion.component';
import {SearchPipe} from "./layout/pipes/search.pipe";
import {FilterByBrand} from "./layout/pipes/filter-by-brand";
import { EditProductComponent } from './customer/edit-product/edit-product.component';
import {FilterByCategory} from "./layout/pipes/filter-by-category";
import { IndexNotAuthComponent } from './layout/index-not-auth/index-not-auth.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    NavigationComponent,
    IndexComponent,
    ProfileComponent,
    CustomerCartComponent,
    CustomerOrdersComponent,
    EditCustomerComponent,
    AddProductComponent,
    ProductsComponent,
    ProductViewComponent,
    AddBrandComponent,
    AddCategoryComponent,
    AddColorComponent,
    AddSizeComponent,
    CurrentOrderComponent,
    AddPromotionComponent,
    SearchPipe,
    FilterByBrand,
    FilterByCategory,
    EditProductComponent,
    IndexNotAuthComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MaterialModule,
    HttpClientModule,
    FormsModule,
    NgImageSliderModule
  ],
  providers: [authInterceptorProviders, authErrorInterceptorProvider],
  bootstrap: [AppComponent]
})
export class AppModule { }
