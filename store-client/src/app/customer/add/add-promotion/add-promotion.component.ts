import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Brand} from "../../../models/Brand";
import {BrandService} from "../../../services/brand.service";
import {NotificationService} from "../../../services/notification.service";
import {Router} from "@angular/router";
import {Promotion} from "../../../models/Promotion";
import {PromotionService} from "../../../services/promotion.service";

@Component({
  selector: 'app-add-promotion',
  templateUrl: './add-promotion.component.html',
  styleUrls: ['./add-promotion.component.css']
})
export class AddPromotionComponent implements OnInit {
  public addPromotionForm!: FormGroup;
  isPromotionCreated = false;
  createdPromotion!: Promotion;

  constructor(private fb: FormBuilder,
              private promotionService: PromotionService,
              private notification: NotificationService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.addPromotionForm = this.createAddPromotion();
  }

  private createAddPromotion(): FormGroup {
    return this.fb.group({
      title: ['', Validators.compose([Validators.required])],
      description: ['', Validators.compose([Validators.required])],
      discountPercent: ['', Validators.compose([Validators.required])],
    });
  }


  submit(): void {
    console.log(this.addPromotionForm.value);

    this.promotionService.createPromotion({
      title: this.addPromotionForm.value.title,
      description: this.addPromotionForm.value.description,
      discountPercent: this.addPromotionForm.value.discountPercent,
    }).subscribe(data => {
      this.isPromotionCreated = true;
      this.createdPromotion = data;
      console.log(data);
      this.notification.showSnackBar("Brand was created");
      this.router.navigate(['/profile']);
    });
  }
}
