import {Pipe, PipeTransform} from "@angular/core";
import {Product} from "../../models/Product";

@Pipe({
  name: 'filterByBrand'
})
export class FilterByBrand implements PipeTransform {
  transform(products: Product[], selectedBrandIds: number[]): any[] {
    if (!products || !selectedBrandIds || selectedBrandIds.length === 0) {
      return products;
    }

    return products.filter(product => selectedBrandIds.includes(product.brand?.id!));
  }

}
