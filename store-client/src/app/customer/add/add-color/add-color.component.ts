import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Brand} from "../../../models/Brand";
import {BrandService} from "../../../services/brand.service";
import {NotificationService} from "../../../services/notification.service";
import {Router} from "@angular/router";
import {Color} from "../../../models/Color";
import {ColorService} from "../../../services/color.service";

@Component({
  selector: 'app-add-color',
  templateUrl: './add-color.component.html',
  styleUrls: ['./add-color.component.css']
})
export class AddColorComponent implements OnInit {
  public addColorForm!: FormGroup;
  isColorCreated = false;
  createdColor!: Color;

  constructor(private fb: FormBuilder,
              private colorService: ColorService,
              private notification: NotificationService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.addColorForm = this.createAddBrand();
  }

  private createAddBrand(): FormGroup {
    return this.fb.group({
      name: ['', Validators.compose([Validators.required])],
      hexCode: ['', Validators.compose([Validators.required])]
    });
  }


  submit(): void {
    console.log(this.addColorForm.value);

    this.colorService.createColor({
      name: this.addColorForm.value.name,
      hexCode: this.addColorForm.value.hexCode,
    }).subscribe(data => {
      this.isColorCreated = true;
      this.createdColor = data;
      console.log(data);
      this.notification.showSnackBar("Color was created");
      this.router.navigate(['/profile']);
    });
  }
}
