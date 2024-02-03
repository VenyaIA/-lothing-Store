import {Component, OnInit} from '@angular/core';
import {Customer} from "../../models/Customer";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {NotificationService} from "../../services/notification.service";
import {CustomerService} from "../../services/customer.service";
import {ImageProfileService} from "../../services/image-profile.service";
import {EditCustomerComponent} from "../edit-customer/edit-customer.component";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  isCustomerDataLoaded = false;
  customer!: Customer;
  customerImageProfile!: File;
  selectedFile!: File;
  previewImgURL: any;
  path = 'http://localhost:4200/assets/images/profiles/';

  constructor(private dialog: MatDialog,
              private notification: NotificationService,
              private customerService: CustomerService,
              private imageProfileService: ImageProfileService
  ) {
  }

  ngOnInit(): void {
    this.customerService.getCurrentCustomer()
      .subscribe(date => {
        this.customer = date
        this.isCustomerDataLoaded = true;
      });
    this.imageProfileService.getImageProfileCurrentCustomer()
      .subscribe(data => {
        if (data !== null) {
          console.log(data);
          this.customerImageProfile = data.image;
          console.log(this.customerImageProfile);
        }
      });
  }

  openEditDialog(): void {
    const dialogCustomerEditConfig = new MatDialogConfig();
    dialogCustomerEditConfig.width = '500px';
    dialogCustomerEditConfig.data = {
      customer: this.customer
    };
    this.dialog.open(EditCustomerComponent, dialogCustomerEditConfig);
  }

  onFieldSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);


    const reader = new FileReader();
    reader.readAsDataURL(event.target.files[0]);
    reader.onload = (event: any) => {
      console.log(event.target.result);
      this.previewImgURL = reader.result;
      console.log(this.previewImgURL);
    }

  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  onUpload(): void {
    if (this.selectedFile != null) {
      this.imageProfileService.uploadImageProfileCurrentCustomer(this.selectedFile)
        .subscribe(() => {
          this.notification.showSnackBar("Profile Image updated successfully")
        })
    }
  }
}
