import {Component, OnInit} from '@angular/core';
import {Customer} from "../../models/Customer";
import {TokenStorageService} from "../../services/token-storage.service";
import {CustomerService} from "../../services/customer.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit{

  isLoggedIn = false;
  isDataLoaded = false;
  customer!: Customer;

  constructor(private tokenService: TokenStorageService,
              private customerService: CustomerService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenService.getToken();

    if (this.isLoggedIn) {
      this.customerService.getCurrentCustomer()
        .subscribe(data => {
          this.customer = data;
          this.isDataLoaded = true;
        })
    }
  }

  logOut(): void {
    this.tokenService.logOut();
    this.router.navigate(['/login']);
  }

}
