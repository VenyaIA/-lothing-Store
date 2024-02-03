import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NotificationService} from "../../services/notification.service";
import {CustomerService} from "../../services/customer.service";
import {Customer} from "../../models/Customer";

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.css']
})
export class EditCustomerComponent implements OnInit{

  public profileForm!: FormGroup;

  constructor(private dialogRef: MatDialogRef<EditCustomerComponent>,
              private fb: FormBuilder,
              private notification: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private customerService: CustomerService) {
  }

  ngOnInit(): void {
    this.profileForm = this.createProfileForm();
  }

  private createProfileForm(): FormGroup {
    return this.fb.group({
      firstname: [
        this.data.customer.firstname,
        Validators.compose([Validators.required])
      ],
      lastname: [
        this.data.customer.lastname,
        Validators.compose([Validators.required])
      ],
      phoneNumber: [
        this.data.customer.phoneNumber,
        Validators.compose([Validators.required])
      ]
    });
  }

  submit(): void {
    this.customerService.updateCustomer(this.updateCustomer())
      .subscribe(() => {
        this.notification.showSnackBar("User updated successfully");
        this.dialogRef.close();
      });
  }

  private updateCustomer(): Customer {
    this.data.customer.firstname = this.profileForm.value.firstname;
    this.data.customer.lastname = this.profileForm.value.lastname;
    this.data.customer.phoneNumber = this.profileForm.value.phoneNumber;
    return this.data.customer;
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
