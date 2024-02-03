import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  public registerForm!: FormGroup;

  constructor(
    private authService: AuthService,
    private notification: NotificationService,
    private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.registerForm = this.createRegisterForm();
  }

  createRegisterForm(): FormGroup {
    return this.fb.group({
      email: ['', Validators.compose([Validators.required, Validators.email])],
      firstname: ['', Validators.compose([Validators.required])],
      lastname: ['', Validators.compose([Validators.required])],
      username: ['', Validators.compose([Validators.required])],
      phoneNumber: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required, Validators.min(6)])],
      confirmPassword: ['', Validators.compose([Validators.required, Validators.min(6)])]
    });
  }

  submit(): void {
    console.log(this.registerForm.value);

    this.authService.registration({
      email: this.registerForm.value.email,
      firstname: this.registerForm.value.firstname,
      lastname: this.registerForm.value.lastname,
      username: this.registerForm.value.username,
      phoneNumber: this.registerForm.value.phoneNumber,
      password: this.registerForm.value.password,
      confirmPassword: this.registerForm.value.confirmPassword,
    }).subscribe(date => {
      console.log(date);
      this.notification.showSnackBar("Successfully registered!");
    }, error => {
      console.log(error);
      this.notification.showSnackBar("Something went wrong during registration");
    });
  }
}
