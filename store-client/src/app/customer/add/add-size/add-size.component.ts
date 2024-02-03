import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Brand} from "../../../models/Brand";
import {BrandService} from "../../../services/brand.service";
import {NotificationService} from "../../../services/notification.service";
import {Router} from "@angular/router";
import {SizeService} from "../../../services/size.service";
import {Size} from "../../../models/Size";

@Component({
  selector: 'app-add-size',
  templateUrl: './add-size.component.html',
  styleUrls: ['./add-size.component.css']
})
export class AddSizeComponent implements OnInit {
  public addSizeForm!: FormGroup;
  isSizeCreated = false;
  createdSize!: Size;

  constructor(private fb: FormBuilder,
              private sizeService: SizeService,
              private notification: NotificationService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.addSizeForm = this.createAddBrand();
  }

  private createAddBrand(): FormGroup {
    return this.fb.group({
      title: ['', Validators.compose([Validators.required])]
    });
  }


  submit(): void {
    console.log(this.addSizeForm.value);

    this.sizeService.createSize({
      title: this.addSizeForm.value.title
    }).subscribe(data => {
      this.isSizeCreated = true;
      this.createdSize = data;
      console.log(data);
      this.notification.showSnackBar("Size was created");
      this.router.navigate(['/profile']);
    });
  }
}
